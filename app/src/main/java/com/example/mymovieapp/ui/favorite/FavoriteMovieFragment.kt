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
import com.example.mymovieapp.database.movie.MoviesDAO
import com.example.mymovieapp.database.persistance.movie.InjectionMovie
import com.example.mymovieapp.ui.favorite.viewmodel.movie.FavMovieViewModel
import com.example.mymovieapp.ui.favorite.viewmodel.movie.FavMovieViewModelFactory
import com.example.mymovieapp.ui.movies.MovieAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorite_movie.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {
    private lateinit var adapter: MovieAdapter
    private lateinit var favMovieViewModelFactory: FavMovieViewModelFactory
    private val viewModel: FavMovieViewModel by viewModels { favMovieViewModelFactory }
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favMovieViewModelFactory = InjectionMovie.provideViewModelFactory(requireContext())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_movies.setHasFixedSize(true)

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(context)
        rv_movies.adapter = adapter

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
                viewModel.getMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            val listItems = ArrayList<MoviesDAO>()

                            for (t in it) {
                                val data = MoviesDAO(
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
            pb_movies.visibility = View.VISIBLE
        } else {
            pb_movies.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
