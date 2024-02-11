package ru.ndevelop.tinkofflab2024

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ndevelop.tinkofflab2024.models.Movie
import java.lang.Exception
import java.net.URL


class MovieListAdapter(
    private val onMovieClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private var dataSet: List<Movie> = listOf()
    val adapterScope = CoroutineScope(Dispatchers.Default)

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

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.single_movie, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.shimmer.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        viewHolder.tvMovieName.text = dataSet[position].nameRu
        viewHolder.tvMovieGenre.text = "${dataSet[position].genre} (${dataSet[position].year})"

        val filmId = dataSet[position].filmId
        viewHolder.favouriteImage.visibility =
            if (dataSet[position].isFavourite) View.VISIBLE else View.GONE

        if (MovieCacheManager.isInCache(filmId)) {
            viewHolder.previewImage.setImageBitmap(MovieCacheManager.getCachedPreview(filmId))
        } else {
            if (dataSet[position].posterUrlPreview != null) {
                val networkScope = CoroutineScope(Dispatchers.IO)
                // Launch a new coroutine in the scope
                networkScope.launch {
                    try {
                        val url = URL(dataSet[position].posterUrlPreview)
                        val imageData = url.readBytes()
                        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                        MovieCacheManager.addPreview(filmId, bitmap)
                        withContext(Dispatchers.Main) {
                            viewHolder.previewImage.setImageBitmap(bitmap)
                            viewHolder.shimmer.apply {
                                stopShimmer()
                                visibility=View.GONE
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
            onMovieClick(position)
        }
        viewHolder.itemView.setOnLongClickListener {

            adapterScope.launch {
                val wasFavourite = Repository.isFavourite(dataSet[position].filmId)
                if(wasFavourite){
                    Repository.deleteMovie(filmId)
                }
                else{
                    Repository.updateMovie(dataSet[position])
                }
                withContext(Dispatchers.Main) {
                    if (wasFavourite) {
                        viewHolder.favouriteImage.visibility = View.GONE
                    } else {
                        viewHolder.favouriteImage.visibility = View.VISIBLE
                    }
                }
            }
            true
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setData(data: List<Movie>) {
        this.dataSet = data
    }
}
