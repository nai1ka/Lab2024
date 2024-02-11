package ru.ndevelop.tinkofflab2024.data

import android.graphics.Bitmap
import ru.ndevelop.tinkofflab2024.models.Movie

object CacheManager {
    private val previewCache: HashMap<Int, Bitmap> = HashMap()

    private val movieInfoCache: HashMap<Int, Movie> = HashMap()

    var retrievedMoviesCache: List<Movie> = listOf()

    fun getCachedPreview(filmId: Int) = previewCache[filmId]
    fun addPreview(filmId: Int, bitmap: Bitmap) {
        previewCache[filmId] = bitmap
    }

    fun addMovieInfo(filmId: Int, movie: Movie) {
        movieInfoCache[filmId] = movie
    }

    fun getCachedMovieInfo(filmId: Int) = movieInfoCache[filmId]

    fun isPreviewInCache(filmId: Int) = previewCache.containsKey(filmId)
}