package ru.ndevelop.tinkofflab2024.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ndevelop.tinkofflab2024.data.LocalRepository
import ru.ndevelop.tinkofflab2024.data.Repository
import ru.ndevelop.tinkofflab2024.models.Movie

class FavouriteViewModel : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>().apply {
        value = listOf()
    }
    val movieList: LiveData<List<Movie>> = _movieList

    fun loadData() {
        Repository.getFavouriteFilms {
            _movieList.postValue(it)
        }

    }
}
