package com.example.mymovieapp.ui.movies

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
import com.example.mymovieapp.ui.detail.DetailMovieActivity
import kotlinx.android.synthetic.main.item_movies.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val listMovie = ArrayList<MoviesDAO>()

    fun setData(movie: ArrayList<MoviesDAO>) {
        listMovie.clear()
        listMovie.addAll(movie)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: MoviesDAO) {
            with(itemView) {
                var desc = movie.desc
                if (desc?.length!! > 100) desc = desc.substring(0, 100)

                var year: String? = movie.releaseDate
                year = if (year.isNullOrEmpty()) {
                    "Unknown"
                } else {
                    movie.releaseDate?.substring(0, 4)
                }

                itemView.tv_title.text = String.format("%s (%s)", movie.title, year)
                itemView.tv_rating.text = String.format("%s/10", movie.rating)
                itemView.tv_desc.text = String.format("%s...", desc)
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + movie.poster)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                    .into(itemView.img_poster)

                itemView.setOnClickListener {
                    val movies = MoviesDAO(
                        movie.id,
                        movie.title,
                        movie.rating,
                        movie.releaseDate,
                        movie.poster,
                        movie.desc,
                        movie.backdrop
                    )

                    val moveWithObjectIntent = Intent(context, DetailMovieActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies)
                    context.startActivity(moveWithObjectIntent)
                }
            }
        }
    }
}
