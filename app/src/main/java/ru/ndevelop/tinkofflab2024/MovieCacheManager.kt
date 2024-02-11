package ru.ndevelop.tinkofflab2024

import android.graphics.Bitmap

object MovieCacheManager {
    private val previewCache: HashMap<Int, Bitmap> = HashMap()

    fun clearPreviewCache() {
        previewCache.clear()
    }

    fun getCachedPreview(filmId: Int) = previewCache[filmId]
    fun addPreview(filmId: Int, bitmap: Bitmap) {
        previewCache[filmId] = bitmap
    }

    fun isInCache(filmId: Int) = previewCache.containsKey(filmId)
}