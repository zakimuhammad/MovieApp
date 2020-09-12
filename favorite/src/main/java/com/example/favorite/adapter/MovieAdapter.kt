package com.example.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.favorite.R
import com.example.favorite.model.Movie
import kotlinx.android.synthetic.main.item_movies.view.*

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listMovie: MutableList<Movie?> = mutableListOf()

    fun setData(movie: MutableList<Movie?>) {
        listMovie.clear()
        listMovie.addAll(movie)
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
        private val imageBaseUrl = "https://image.tmdb.org/t/p/w185"

        fun bind(movie: Movie?) {
            var desc = movie?.desc
            if (desc?.length!! > 100) desc = desc.substring(0, 100)

            var year: String? = movie?.releaseDate
            year = if (year.isNullOrEmpty()) {
                "Unknown"
            } else {
                movie?.releaseDate?.substring(0, 4)
            }

            itemView.tv_title.text = String.format("%s (%s)", movie?.title, year)
            itemView.tv_rating.text = String.format("%s/10", movie?.rating)
            itemView.tv_desc.text = String.format("%s...", desc)
            Glide.with(itemView.context)
                .load(imageBaseUrl + movie?.poster)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                .into(itemView.img_poster)
        }
    }
}