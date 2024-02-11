package ru.ndevelop.tinkofflab2024.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ndevelop.tinkofflab2024.R
import ru.ndevelop.tinkofflab2024.data.CacheManager
import ru.ndevelop.tinkofflab2024.data.LocalRepository
import ru.ndevelop.tinkofflab2024.models.Movie
import java.lang.Exception
import java.net.URL


class MovieListAdapter(
    private val onMovieClick: ((Int) -> Unit)?
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private var dataSet: List<Movie> = listOf()
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMovieName: TextView
        val tvMovieGenre: TextView
        val previewImage: ImageView
        val favouriteImage: ImageView
        val shimmer: ShimmerFrameLayout

        init {
            tvMovieName = view.findViewById(R.id.tv_movie_name)
            tvMovieGenre = view.findViewById(R.id.tv_movie_genre)
            previewImage = view.findViewById(R.id.iv_movie_preview)
            favouriteImage = view.findViewById(R.id.iv_favourite)
            shimmer = view.findViewById(R.id.shimmer_preview)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.single_movie, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.previewImage.setImageBitmap(null)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.shimmer.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        viewHolder.tvMovieName.text = dataSet[position].nameRu
        viewHolder.tvMovieGenre.text = "${dataSet[position].genre} (${dataSet[position].year})"

        val filmId = dataSet[position].filmId
        viewHolder.favouriteImage.visibility =
            if (dataSet[position].isFavourite) View.VISIBLE else View.GONE

        if (CacheManager.isPreviewInCache(filmId)) {
            viewHolder.previewImage.setImageBitmap(CacheManager.getCachedPreview(filmId))
        } else {
            if (dataSet[position].posterUrlPreview != null) {
                val networkScope = CoroutineScope(Dispatchers.IO)
                networkScope.launch {
                    try {
                        val url = URL(dataSet[position].posterUrlPreview)
                        val imageData = url.readBytes()
                        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                        CacheManager.addPreview(filmId, bitmap)
                        withContext(Dispatchers.Main) {
                            viewHolder.previewImage.setImageBitmap(bitmap)
                            viewHolder.shimmer.apply {
                                stopShimmer()
                                visibility = View.GONE
                            }
                        }
                    } catch (e: Exception) {
                        //TODO check
                        println(e.message)
                    }

                }
            }
        }

        viewHolder.itemView.setOnClickListener {
            if (onMovieClick != null) {
                onMovieClick!!.invoke(dataSet[position].filmId)
            }
        }
        viewHolder.itemView.setOnLongClickListener {
            adapterScope.launch {
                val wasFavourite = LocalRepository.isFavourite(dataSet[position].filmId)
                dataSet[position].isFavourite = !wasFavourite
                if (wasFavourite) {
                    LocalRepository.deleteMovie(filmId)
                } else {
                    LocalRepository.inserMovie(dataSet[position])
                }
            }
            notifyItemChanged(position)
            true
        }
    }
    override fun getItemCount() = dataSet.size

    fun setData(data: List<Movie>) {
        this.dataSet = data
    }
}
