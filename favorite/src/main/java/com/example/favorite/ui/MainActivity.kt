package com.example.favorite.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favorite.R
import com.example.favorite.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                supportFragmentManager
            )
        sectionsPagerAdapter.addFragment(
            MovieFragment(), getString(
                R.string.title_movies
            )
        )
        sectionsPagerAdapter.addFragment(
            TvShowFragment(), getString(
                R.string.title_tvshow
            )
        )

        vp_favorite.adapter = sectionsPagerAdapter
        tl_favorit.setupWithViewPager(vp_favorite)
    }
}
