package ru.ndevelop.tinkofflab2024.ui.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ndevelop.tinkofflab2024.MovieCacheManager
import ru.ndevelop.tinkofflab2024.MovieListAdapter
import ru.ndevelop.tinkofflab2024.SearchableFragment
import ru.ndevelop.tinkofflab2024.databinding.FragmentMovieListBinding
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.ui.aboutMovie.AboutMovieActivity


class FavouriteListFragment : SearchableFragment() {

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
        val homeViewModel =
            ViewModelProvider(
                this
            )[FavouriteViewModel::class.java]

        //TODO ???
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.swipeRefreshLayout.isEnabled = false

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
        homeViewModel.movieList.observe(viewLifecycleOwner) {
            binding.loadingSpinner.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
            dataSet = it
            movieListAdapter.setData(dataSet)
            movieListAdapter.notifyDataSetChanged()
        }


        homeViewModel.loadData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

