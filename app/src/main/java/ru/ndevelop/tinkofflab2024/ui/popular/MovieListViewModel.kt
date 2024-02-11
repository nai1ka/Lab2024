package ru.ndevelop.tinkofflab2024.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import ru.ndevelop.tinkofflab2024.data.ApiClient
import ru.ndevelop.tinkofflab2024.data.LocalRepository
import ru.ndevelop.tinkofflab2024.data.Repository
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.Response


class MovieListViewModel : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>().apply {
        value = listOf()
    }

    private val _error = MutableLiveData<Boolean>().apply {
        value = false
    }

    val movieList: LiveData<List<Movie>> = _movieList
    val error: LiveData<Boolean> = _error

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var setOfFavouriteFilms: HashSet<Int> = HashSet()

    fun loadData() {
        setOfFavouriteFilms.clear()
        Repository.getPopularFilms(onSuccess = {
            scope.launch {
                for(film in LocalRepository.getListOfFavouriteMovies()){
                    setOfFavouriteFilms.add(film.filmId)
                }
                for(film in it){
                    film.isFavourite = setOfFavouriteFilms.contains(film.filmId)
                }
                _movieList.postValue(it)
                _error.postValue(false)
            }

        },
            onError = {
                _error.postValue(true)
            })
    }

}

