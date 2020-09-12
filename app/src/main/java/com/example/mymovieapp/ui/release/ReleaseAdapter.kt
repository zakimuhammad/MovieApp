package com.example.mymovieapp.ui.release

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mymovieapp.R
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.model.movies.MovieModel
import com.example.mymovieapp.ui.detail.DetailMovieActivity
import kotlinx.android.synthetic.main.item_movies.view.*

class ReleaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listMovie: MutableList<MovieModel?> = mutableListOf()

    fun setData(movie: MutableList<MovieModel?>?) {
        listMovie.clear()
        if (movie != null) {
            listMovie.addAll(movie)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movies,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(listMovie[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieModel?) {
            with(itemView) {
                var desc = movie?.overview
                if (desc != null) {
                    if (desc.length > 100) desc = desc.substring(0, 100)
                }

                var year: String? = movie?.releaseDate
                year = if (year.isNullOrEmpty()) {
                    "Unknown"
                } else {
                    movie?.releaseDate?.substring(0, 4)
                }

                itemView.tv_title.text = String.format("%s (%s)", movie?.title, year)
                itemView.tv_rating.text = String.format("%s/10", movie?.voteAverage)
                itemView.tv_desc.text = String.format("%s...", desc)
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + movie?.posterPath)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                    .into(itemView.img_poster)

                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, DetailMovieActivity::class.java)
                    intentToDetail.putExtra(
                        DetailMovieActivity.EXTRA_RELEASE, MovieModel(
                            movie?.id,
                            movie?.overview,
                            movie?.posterPath,
                            movie?.releaseDate,
                            movie?.title,
                            movie?.voteAverage,
                            movie?.backdropPath
                        )
                    )
                    context.startActivity(intentToDetail)
                }
            }
        }
    }

}
