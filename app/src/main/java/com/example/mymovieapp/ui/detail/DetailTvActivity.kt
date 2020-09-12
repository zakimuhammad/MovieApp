package com.example.mymovieapp.ui.detail


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.EmptyResultSetException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mymovieapp.BuildConfig
import com.example.mymovieapp.R
import com.example.mymovieapp.database.persistance.tvshow.InjectionTv
import com.example.mymovieapp.database.tv.TvShowsDAO
import com.example.mymovieapp.ui.favorite.viewmodel.tv.FavTvShowViewModel
import com.example.mymovieapp.ui.favorite.viewmodel.tv.TvShowViewModelFactory
import com.example.mymovieapp.ui.tvshow.TVViewModel
import com.example.mymovieapp.util.getLocale
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_tv.*
import kotlinx.android.synthetic.main.layout_rating.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailTvActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_TV = "extra_tv"
    }

    private lateinit var tvViewModel: TVViewModel
    private lateinit var tvShowViewModelFactory: TvShowViewModelFactory

    private val viewModel: FavTvShowViewModel by viewModels { tvShowViewModelFactory }
    private val disposable = CompositeDisposable()

    private var id: Int? = null
    private var listTv = ArrayList<TvShowsDAO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv)

        setUp()

        tvViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(TVViewModel::class.java)

        setTvSHow()
        tvShowViewModelFactory = InjectionTv.provideViewModelFactory(this)

        setFavorite()
        favoriteState()

        fab_favorite.setOnClickListener(this)
    }

    private fun setTvSHow() {
        val tv = intent.getParcelableExtra(DetailTvActivity.EXTRA_TV) as TvShowsDAO

        id = tv.id

        showLoading(true)

        id?.let { tvViewModel.setDetailTvShow(it, BuildConfig.API_KEY, getLocale()) }
        tvViewModel.getDetailTvShow().observe(this, Observer { items ->
            if (items != null) {
                listTv.clear()

                var year: String? = items.releaseDate
                year = if (year.isNullOrEmpty()) {
                    getString(R.string.unknown)
                } else {
                    items.releaseDate?.substring(0, 4)
                }

                tv_detail_rating.text = String.format("%s/10", items.rating.toString())
                tv_detail_date.text = items.releaseDate?.let { formatDate(it) }
                tv_detail_desc.text = items.desc
                collapsing_toolbar.title = String.format("%s (%s)", items.title, year)

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + items.poster)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(6)))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(img_poster_detail)

                showLoading(false)
                listTv.add(items)
            }
        })
    }

    private fun addToFavorite() {
        try {
            disposable.add(
                viewModel.updateTvShow(listTv[0])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isFavorite = true
                            setFavorite()
                            Snackbar.make(detail_layout, "Added to favorite", Snackbar.LENGTH_SHORT)
                                .show()
                        },
                        { error ->
                            Log.e("Error DB", "Unable to insert tvshow", error)
                        }
                    )
            )
        } catch (e: EmptyResultSetException) {
            Log.e("Error DB", e.toString())
        }
    }

    private fun removeFromFavorite() {
        try {
            disposable.add(
                viewModel.deleteTvShow(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isFavorite = false
                            setFavorite()
                            Snackbar.make(
                                detail_layout,
                                "Removed from favorite",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        },
                        { error ->
                            Log.e("Error DB", "Unable to delete movie", error)
                        }
                    )
            )
        } catch (e: EmptyResultSetException) {
            Log.e("Error DB", e.toString())
        }
    }

    private fun favoriteState() {
        try {
            disposable.add(
                viewModel.detailTvShow(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            if (it.isNotEmpty()) isFavorite = true
                            setFavorite()
                        },
                        { error ->
                            Log.e("Error DB", "Unable to get movie", error)
                        }
                    )
            )
        } catch (e: EmptyResultSetException) {
            Log.e("Error DB", e.toString())
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_favorite) {
            favoriteState()
            setFavorite()
            if (isFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }

            isFavorite = !isFavorite
            setFavorite()
        }
    }

    private fun setUp() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded)
        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed)
    }

    private fun formatDate(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val sdfResult = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())

        var date: Date? = Date()
        try {
            date = sdf.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdfResult.format(date)
    }

    private fun setFavorite() {
        if (!isFavorite)
            fab_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart))
        else
            fab_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart_red))
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_detail.visibility = View.VISIBLE
        } else {
            pb_detail.visibility = View.GONE
        }
    }
}
