package ru.ndevelop.tinkofflab2024.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.ndevelop.tinkofflab2024.adapters.MovieListAdapter
import ru.ndevelop.tinkofflab2024.ui.SearchableFragment
import ru.ndevelop.tinkofflab2024.databinding.FragmentMovieListBinding
import ru.ndevelop.tinkofflab2024.models.Movie


class FavouriteListFragment : SearchableFragment() {

    private var _binding: FragmentMovieListBinding? = null
    var onMovieClickListener: ((Int) -> Unit)? = null
    private val binding get() = _binding!!

    private var dataSet: List<Movie> = listOf()
    private lateinit var movieListAdapter: MovieListAdapter
    override fun onSearchTextChanged(text: String) {
        val filteredList = dataSet.filter { item ->
            if (item.nameRu == null) false
            else
                item.nameRu.lowercase().contains(text.lowercase())
        }
        if (filteredList.isEmpty() && text.isNotEmpty()) {
            showNoDataInfo()
        } else {
            hideNoDataInfo()
        }
        movieListAdapter.setData(filteredList)
        movieListAdapter.notifyDataSetChanged()
    }

    override fun showNoDataInfo() {
        binding.notFoundInfo.visibility = View.VISIBLE
    }

    override fun hideNoDataInfo() {
        binding.notFoundInfo.visibility = View.GONE
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
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.swipeRefreshLayout.isEnabled = false
        binding.errorLayout.llError.visibility = View.GONE
        movieListAdapter = MovieListAdapter(onMovieClickListener)

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
