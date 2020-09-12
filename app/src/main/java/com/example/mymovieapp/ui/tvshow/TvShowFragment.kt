package com.example.mymovieapp.ui.tvshow

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
import kotlinx.android.synthetic.main.fragment_tvshow.*

class TvShowFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var adapter: TVAdapter
    private lateinit var viewModel: TVViewModel
    private var edtSearch: SearchView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TVAdapter()
        adapter.notifyDataSetChanged()
        rv_tvshow.layoutManager = LinearLayoutManager(context)
        rv_tvshow.setHasFixedSize(true)
        rv_tvshow.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TVViewModel::class.java)

        viewModel.getStatus().observe(this, Observer { t ->
            if (t != false) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        })

        viewModel.setTvShow(BuildConfig.API_KEY, getLocale())

        viewModel.getTvShows().observe(viewLifecycleOwner, Observer { items ->
            if (items != null) {
                adapter.setData(items)
                showLoading(false)
            }
        })

        edtSearch = view.findViewById(R.id.sv_tvshow) as SearchView
        edtSearch?.setOnQueryTextListener(this)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_tvshow.visibility = View.VISIBLE
        } else {
            pb_tvshow.visibility = View.GONE
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        if (query.isNotEmpty()) {
            viewModel.searchTv(BuildConfig.API_KEY, getLocale(), query)
            viewModel.getTvShows().observe(viewLifecycleOwner, Observer { items ->
                if (items != null) {
                    adapter.setData(items)
                    showLoading(false)
                }
            })
        } else {
            viewModel.setTvShow(BuildConfig.API_KEY, getLocale())
            viewModel.getTvShows().observe(viewLifecycleOwner, Observer { items ->
                if (items != null) {
                    adapter.setData(items)
                    showLoading(false)
                }
            })
        }
        return false
    }

}