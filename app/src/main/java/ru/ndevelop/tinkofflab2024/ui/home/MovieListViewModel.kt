package ru.ndevelop.tinkofflab2024.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import ru.ndevelop.tinkofflab2024.ApiClient
import ru.ndevelop.tinkofflab2024.Repository
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.Response


class MovieListViewModel : ViewModel() {

    val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val _movieList = MutableLiveData<List<Movie>>().apply {
        value = listOf()
    }

    private val _error = MutableLiveData<Boolean>().apply {
        value = false
    }

    val movieList: LiveData<List<Movie>> = _movieList
    val error: LiveData<Boolean> = _error

    val retrofitClient = ApiClient.apiService
    private var setOfFavouriteFilms: HashSet<Int> = HashSet()

    fun loadData() {

        viewModelScope.launch {

            val movies = Repository.getListOfFavouriteMovies()
            for (movie in movies) {
               setOfFavouriteFilms.add(movie.filmId)
            }
            fetchDataFromApi()
        }
    }
    fun fetchDataFromApi() {
        retrofitClient.getTop("TOP_100_POPULAR_FILMS").enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val films = response.body()!!.films
                    viewModelScope.launch {
                        for (film in films) {
                            if (setOfFavouriteFilms.contains(film.filmId)) {
                                film.isFavourite = true
                            }
                        }
                    }

                    _movieList.postValue(films)
                    _error.postValue(false)
                } else {
                    _error.postValue(true)
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _error.postValue(true)
            }
        })
    }
}

