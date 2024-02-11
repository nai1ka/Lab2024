package ru.ndevelop.tinkofflab2024.ui.favourite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.ndevelop.tinkofflab2024.ApiClient
import ru.ndevelop.tinkofflab2024.App
import ru.ndevelop.tinkofflab2024.Repository
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.MovieEntity
import ru.ndevelop.tinkofflab2024.models.Response


class FavouriteViewModel() : ViewModel() {


    val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val _movieList = MutableLiveData<List<Movie>>().apply {
        value = listOf()
    }
    val movieList: LiveData<List<Movie>> = _movieList

    fun loadData() {
        viewModelScope.launch {
            val movies = Repository.getListOfFavouriteMovies()
            _movieList.postValue(movies.map { Movie(it) })
        }

    }

}
