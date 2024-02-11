package ru.ndevelop.tinkofflab2024.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.ndevelop.tinkofflab2024.MovieListAdapter
import ru.ndevelop.tinkofflab2024.SearchableFragment
import ru.ndevelop.tinkofflab2024.databinding.FragmentMovieListBinding
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.ui.aboutMovie.AboutMovieActivity


class MovieListFragment : SearchableFragment() {

    private var _binding: FragmentMovieListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var dataSet: List<Movie> = listOf()
    private lateinit var movieListAdapter: MovieListAdapter
    override fun onSearchTextChanged(text: String) {

        val filteredList = dataSet.filter { item ->
            if (item.nameRu == null) false
            else
                item.nameRu.lowercase().contains(text.lowercase())
        }
        movieListAdapter.setData(filteredList)
        //TODO change
        movieListAdapter.notifyDataSetChanged()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val movieListViewModel =
            ViewModelProvider(
                this
            )[MovieListViewModel::class.java]

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        movieListAdapter = MovieListAdapter { index ->
            val myIntent = Intent(
                context,
                AboutMovieActivity::class.java
            )
            myIntent.putExtra("filmId", dataSet[index].filmId)
            startActivity(myIntent)
        }

        val recyclerView = binding.rvMovieList
        recyclerView.adapter = movieListAdapter
        movieListViewModel.movieList.observe(viewLifecycleOwner) {
            binding.loadingSpinner.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
            dataSet = it
            movieListAdapter.setData(dataSet)
            movieListAdapter.notifyDataSetChanged()
        }

        movieListViewModel.error.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                binding.errorLayout.llError.visibility = View.VISIBLE
            } else {
                binding.errorLayout.llError.visibility = View.GONE
            }
            binding.loadingSpinner.visibility = View.GONE
        }

        binding.errorLayout.btnRetry.setOnClickListener {
            binding.errorLayout.llError.visibility = View.GONE
            binding.loadingSpinner.visibility = View.VISIBLE
            movieListViewModel.loadData()
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            movieListViewModel.fetchDataFromApi()
        }
        movieListViewModel.loadData()
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

enum class MovieListFragmentType {
    POPULAR, FAVOURITE
}