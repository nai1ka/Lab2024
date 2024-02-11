package ru.ndevelop.tinkofflab2024.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ndevelop.tinkofflab2024.models.Movie

object Repository {
    val repositoryScope = CoroutineScope(Dispatchers.IO)

    fun getPopularFilms(onSuccess: (List<Movie>) -> Unit, onError: () -> Unit) {
        val filmsInCache = CacheManager.retrievedMoviesCache
        if (filmsInCache.isEmpty()) {
            WebRepository.getPopularFilms(onSuccess, onError)
        } else {
            onSuccess(filmsInCache)
        }
    }

    fun getFavouriteFilms(onSuccess: (List<Movie>) -> Unit){
        repositoryScope.launch {
            onSuccess(LocalRepository.getListOfFavouriteMovies()
                .map { film -> Movie(film).also { it.isFavourite = true } })
        }

    }

    fun getFilmAbout(filmId:Int, onSuccess: (Movie) -> Unit, onError: () -> Unit){
        val cachedMovieInfo = CacheManager.getCachedMovieInfo(filmId)
        if(cachedMovieInfo==null){

            WebRepository.getFilmAbout(filmId,onSuccess, onError)
        }
        else{
            onSuccess(cachedMovieInfo)
        }
    }
}