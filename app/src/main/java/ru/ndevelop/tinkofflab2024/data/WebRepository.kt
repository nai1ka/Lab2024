package ru.ndevelop.tinkofflab2024.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.Response

object WebRepository {
    lateinit var applicationContext: Application
    private val retrofitClient = ApiClient.apiService
    val webRepositoryScope = CoroutineScope(Dispatchers.IO)

    fun getPopularFilms(onSuccess: (List<Movie>) -> Unit, onError: () -> Unit) {
        if(!isInternetAvailable()){
            onError()
            return
        }
        retrofitClient.getTop("TOP_100_POPULAR_FILMS").enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val films = response.body()!!.films
                    CacheManager.retrievedMoviesCache = films
                    onSuccess(films)
                } else {
                    onError()
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                onError()
            }
        })
    }

    fun getFilmAbout(filmId: Int, onSuccess: (Movie) -> Unit, onError: () -> Unit) {
        if(!isInternetAvailable()){
            onError()
            return
        }
        retrofitClient.getFilmInfo(filmId).enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: retrofit2.Response<Movie>
            ) {
                if (response.isSuccessful) {
                    val movie = response.body()!!
                    movie.filmId = filmId
                    CacheManager.addMovieInfo(filmId, movie)
                    onSuccess(movie)
                } else {
                    onError()
                }

            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                onError()
            }
        })
    }
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
