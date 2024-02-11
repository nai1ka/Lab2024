package ru.ndevelop.tinkofflab2024.ui.popular

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.ndevelop.tinkofflab2024.adapters.MovieListAdapter
import ru.ndevelop.tinkofflab2024.ui.SearchableFragment
import ru.ndevelop.tinkofflab2024.databinding.FragmentMovieListBinding
import ru.ndevelop.tinkofflab2024.models.Movie

class MovieListFragment : SearchableFragment() {
    private var isError = false
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
        if(filteredList.isEmpty() && !isError && text.isNotEmpty()){
          showNoDataInfo()
        }
        else{
            hideNoDataInfo()
        }
        movieListAdapter.setData(filteredList)
        movieListAdapter.notifyDataSetChanged()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("On attach")
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

        movieListAdapter = MovieListAdapter(onMovieClickListener)
        val recyclerView = binding.rvMovieList
        recyclerView.adapter = movieListAdapter
        movieListViewModel.movieList.observe(viewLifecycleOwner) {
            if(it.size>0){
                binding.loadingSpinner.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
                dataSet = it
                movieListAdapter.setData(dataSet)
                movieListAdapter.notifyDataSetChanged()
            }
        }

        movieListViewModel.error.observe(viewLifecycleOwner) { isError ->
            this.isError = isError
            if (isError) {
                binding.loadingSpinner.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorLayout.llError.visibility = View.VISIBLE
            } else {
                binding.errorLayout.llError.visibility = View.GONE
            }

        }

        binding.errorLayout.btnRetry.setOnClickListener {
            binding.errorLayout.llError.visibility = View.GONE
            binding.loadingSpinner.visibility = View.VISIBLE
            movieListViewModel.loadData()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            movieListViewModel.loadData()
        }
        movieListViewModel.loadData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun showNoDataInfo() {
        binding.notFoundInfo.visibility = View.VISIBLE
    }

    override fun hideNoDataInfo() {
        binding.notFoundInfo.visibility = View.GONE
    }

}
