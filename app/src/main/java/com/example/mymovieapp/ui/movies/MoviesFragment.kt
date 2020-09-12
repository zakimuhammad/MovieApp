package com.example.mymovieapp.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.BuildConfig
import com.example.mymovieapp.R
import com.example.mymovieapp.util.getLocale
import kotlinx.android.synthetic.main.fragment_favorite_movie.*


class MoviesFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel
    private var edtSearch: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()
        rv_movies.layoutManager = LinearLayoutManager(context)
        rv_movies.setHasFixedSize(true)
        rv_movies.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)

        viewModel.getStatus().observe(this, Observer { t ->
            if (t != false) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        })

        viewModel.setMovie(BuildConfig.API_KEY, getLocale())

        viewModel.getMovie().observe(viewLifecycleOwner, Observer { items ->
            if (items != null) {
                adapter.setData(items)
                showLoading(false)
            }
        })

        edtSearch = view.findViewById(R.id.sv_movie) as SearchView
        edtSearch?.setOnQueryTextListener(this)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_movies.visibility = View.VISIBLE
        } else {
            pb_movies.visibility = View.GONE
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        if (query.isEmpty()) {
            viewModel.setMovie(BuildConfig.API_KEY, getLocale())
            viewModel.getMovie().observe(viewLifecycleOwner, Observer { items ->
                if (items != null) {
                    adapter.setData(items)
                    showLoading(false)
                }
            })
        } else {
            viewModel.searchMovie(BuildConfig.API_KEY, getLocale(), query)
            viewModel.getMovie().observe(viewLifecycleOwner, Observer { items ->
                if (items != null) {
                    adapter.setData(items)
                    showLoading(false)
                }
            })
        }
        return false
    }
}
