package com.chriskinyua.collaborativeplaylist.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.collaborativeplaylist.data.SearchResult
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var state: GlobalState
    private val TAG = DashboardFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        state = activity?.application as GlobalState

        val searchTracks = state.spotifyWebApi?.search(state.spotifyHeaders, "Tetema", "track")

        searchTracks?.enqueue(object : Callback<SearchResult>{
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                Log.e(TAG, "Couldn't perform search request", t)
                textView.text = "Couldn't perform search request"
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                var results = response.body()
                var tracks = results?.tracks?.items
                textView.text = "Recieved ${tracks?.size} results"
            }

        })

        return root
    }
}