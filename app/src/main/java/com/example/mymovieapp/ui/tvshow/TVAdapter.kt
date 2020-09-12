package com.example.mymovieapp.ui.tvshow

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
import com.example.mymovieapp.database.tv.TvShowsDAO
import com.example.mymovieapp.ui.detail.DetailTvActivity
import kotlinx.android.synthetic.main.item_movies.view.*

class TVAdapter : RecyclerView.Adapter<TVAdapter.TVViewHolder>() {

    private val listItems = ArrayList<TvShowsDAO>()

    fun setData(tvShow: ArrayList<TvShowsDAO>) {
        listItems.clear()
        listItems.addAll(tvShow)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        return TVViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    inner class TVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShowsDAO) {
            with(itemView) {
                var desc = tvShow.desc
                if (desc?.length!! > 100) desc = desc.substring(0, 100)

                var year: String? = tvShow.releaseDate
                year = if (year.isNullOrEmpty()) {
                    "Unknown"
                } else {
                    tvShow.releaseDate?.substring(0, 4)
                }

                tv_title.text = String.format("%s (%s)", tvShow.title, year)
                tv_rating.text = String.format("%s/10", tvShow.rating)
                tv_desc.text = String.format("%s...", desc)
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + tvShow.poster)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                    .into(img_poster)

                itemView.setOnClickListener {
                    val tv = TvShowsDAO(
                        tvShow.id,
                        tvShow.title,
                        tvShow.rating,
                        tvShow.releaseDate,
                        tvShow.poster,
                        tvShow.desc,
                        tvShow.backdrop
                    )

                    val moveWithObjectIntent = Intent(context, DetailTvActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailTvActivity.EXTRA_TV, tv)
                    context.startActivity(moveWithObjectIntent)
                }
            }
        }
    }
}
