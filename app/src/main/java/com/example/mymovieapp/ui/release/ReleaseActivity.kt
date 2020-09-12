package com.example.mymovieapp.ui.release

import android.os.Bundle
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.mymovieapp.R
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.model.movies.MovieResponseModel
import kotlinx.android.synthetic.main.activity_release.*

class ReleaseActivity : LocalizationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release)

        setSupportActionBar(toolbarReleaseMovie)

        val releaseAdapter = ReleaseAdapter()
        rvReleaseMovie.adapter = releaseAdapter

        val movie = intent.getParcelableExtra<MovieResponseModel?>("movie")

        releaseAdapter.setData(movie?.results)
    }
}
