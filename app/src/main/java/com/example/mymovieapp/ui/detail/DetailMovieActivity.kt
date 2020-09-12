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
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.database.persistance.movie.InjectionMovie
import com.example.mymovieapp.model.movies.MovieModel
import com.example.mymovieapp.ui.favorite.viewmodel.movie.FavMovieViewModel
import com.example.mymovieapp.ui.favorite.viewmodel.movie.FavMovieViewModelFactory
import com.example.mymovieapp.ui.movies.MovieViewModel
import com.example.mymovieapp.util.getLocale
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.layout_rating.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_RELEASE = "extra_release"
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieViewModelFactory: FavMovieViewModelFactory

    private val viewModelMovie: FavMovieViewModel by viewModels { movieViewModelFactory }
    private val disposable = CompositeDisposable()

    private var id: Int? = null
    private var listMovie = ArrayList<MoviesDAO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setUp()

        movieViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)

        setMovie()
        setReleaseDetail()
        movieViewModelFactory = InjectionMovie.provideViewModelFactory(this)

        setFavorite()
        favoriteState()

        fab_favorite.setOnClickListener(this)
    }

    private fun setMovie() {
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MoviesDAO?

        id = movie?.id

        showLoading(true)

        id?.let { movieViewModel.setDetailMovie(it, BuildConfig.API_KEY, getLocale()) }
        movieViewModel.getDetailMovie().observe(this, Observer { items ->
            if (items != null) {
                listMovie.clear()

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
                listMovie.add(items)
            }
        })
    }

    private fun setReleaseDetail() {
        val movie = intent.getParcelableExtra(EXTRA_RELEASE) as MovieModel?

        id = movie?.id

        showLoading(true)

        id?.let { movieViewModel.setDetailMovie(it, BuildConfig.API_KEY, getLocale()) }
        movieViewModel.getDetailMovie().observe(this, Observer { items ->
            if (items != null) {
                listMovie.clear()

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
                listMovie.add(items)
            }
        })
    }

    private fun addToFavorite() {
        try {
            disposable.add(
                viewModelMovie.updateMovie(listMovie[0])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isFavorite = true
                            setFavorite()
                            Snackbar.make(
                                detail_movie_layout,
                                "Added to favorite",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        },
                        { error ->
                            Log.e("Error DB", "Unable to insert movie", error)
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
                viewModelMovie.deleteMovie(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isFavorite = false
                            setFavorite()
                            Snackbar.make(
                                detail_movie_layout,
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
                viewModelMovie.detailMovie(id)
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
