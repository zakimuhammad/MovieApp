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
import com.example.favorite.model.TvShow
import kotlinx.android.synthetic.main.item_movies.view.*

class TvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listTv: MutableList<TvShow?> = mutableListOf()

    fun setData(tvshow: MutableList<TvShow?>) {
        listTv.clear()
        listTv.addAll(tvshow)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movies, parent, false
            )
        )
    }

    override fun getItemCount(): Int = listTv.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(listTv[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageBaseUrl = "https://image.tmdb.org/t/p/w185"

        fun bind(tvShow: TvShow?) {
            with(itemView) {
                var desc = tvShow?.desc
                if (desc?.length!! > 100) desc = desc.substring(0, 100)

                var year: String? = tvShow?.releaseDate
                year = if (year.isNullOrEmpty()) {
                    "Unknown"
                } else {
                    tvShow?.releaseDate?.substring(0, 4)
                }

                tv_title.text = String.format("%s (%s)", tvShow?.title, year)
                tv_rating.text = String.format("%s/10", tvShow?.rating)
                tv_desc.text = String.format("%s...", desc)
                Glide.with(context)
                    .load(imageBaseUrl + tvShow?.poster)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                    .into(img_poster)
            }
        }
    }
}
