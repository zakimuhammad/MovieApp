package com.example.mymovieapp.ui.favorite


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.EmptyResultSetException
import com.example.mymovieapp.R
import com.example.mymovieapp.database.persistance.tvshow.InjectionTv
import com.example.mymovieapp.database.tv.TvShowsDAO
import com.example.mymovieapp.ui.favorite.viewmodel.tv.FavTvShowViewModel
import com.example.mymovieapp.ui.favorite.viewmodel.tv.TvShowViewModelFactory
import com.example.mymovieapp.ui.tvshow.TVAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tvshow.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTVFragment : Fragment() {

    private lateinit var adapter: TVAdapter
    private lateinit var favTvShowViewModelFactory: TvShowViewModelFactory
    private val viewModel: FavTvShowViewModel by viewModels { favTvShowViewModelFactory }
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favTvShowViewModelFactory = InjectionTv.provideViewModelFactory(requireContext())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_tvshow.setHasFixedSize(true)

        adapter = TVAdapter()
        adapter.notifyDataSetChanged()

        rv_tvshow.layoutManager = LinearLayoutManager(context)
        rv_tvshow.adapter = adapter

        setViewModel()
    }

    override fun onStart() {
        super.onStart()
        setViewModel()
    }

    private fun setViewModel() {
        showLoading(true)

        try {
            disposable.add(
                viewModel.getTvShows()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            val listItems = ArrayList<TvShowsDAO>()

                            for (t in it) {
                                val data = TvShowsDAO(
                                    t.id,
                                    t.title,
                                    t.rating,
                                    t.releaseDate,
                                    t.poster,
                                    t.desc,
                                    t.backdrop
                                )
                                listItems.add(data)
                            }
                            adapter.setData(listItems)
                            showLoading(false)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_tvshow.visibility = View.VISIBLE
        } else {
            pb_tvshow.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
