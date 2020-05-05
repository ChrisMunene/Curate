package com.chriskinyua.collaborativeplaylist.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.chriskinyua.shoppinglist.touch.ListRecyclerTouchCallback
import com.chriskinyua.weatherapp.adapter.PlaylistAdapter
import com.spotify.protocol.types.Track

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var state: GlobalState
    private lateinit var recyclerPlaylist: RecyclerView
    private val TAG: String = HomeFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        recyclerPlaylist = root.findViewById(R.id.recyclerPlaylist)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        state = activity?.application as GlobalState

        initRecyclerView()
        setPlaybackListener()
        return root
    }

    private fun initRecyclerView() {

        playlistAdapter = PlaylistAdapter(context!!, state.spotifyAppRemote!!)
        recyclerPlaylist.adapter = playlistAdapter
        recyclerPlaylist.layoutManager = LinearLayoutManager(activity)
        val touchCallbackList = ListRecyclerTouchCallback(playlistAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(recyclerPlaylist)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        recyclerPlaylist.addItemDecoration(itemDecoration)
    }

    private fun setPlaybackListener() {
        state.spotifyAppRemote?.let {
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d(TAG, track.name + " by " + track.artist.name)
                playlistAdapter.addListItem(track)
            }.setErrorCallback { throwable ->
                Log.e(TAG, "Encountered Playback Error", throwable)
            }
        }

    }


}