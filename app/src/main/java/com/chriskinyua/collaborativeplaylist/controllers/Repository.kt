package com.chriskinyua.collaborativeplaylist.controllers

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chriskinyua.collaborativeplaylist.data.SearchResults
import com.chriskinyua.collaborativeplaylist.data.TrackModel
import com.chriskinyua.collaborativeplaylist.network.SpotifyWebApi
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(private val application: GlobalState) {
    val playlist = MutableLiveData<MutableList<TrackModel>>()
    val searchResults = MutableLiveData<MutableList<TrackModel>>()
    private val TAG = Repository::class.java.simpleName

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl(SpotifyWebApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        application.spotifyWebApi = retrofit.create(SpotifyWebApi::class.java)

        playlist.value = mutableListOf()
        searchResults.value = mutableListOf()
    }

    fun performSearch (query: String){
        val searchTracks = application.spotifyWebApi?.search(application.spotifyHeaders, query, "track")

        searchTracks?.enqueue(object : Callback<SearchResults> {
            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                Log.e(TAG, "Couldn't perform search request", t)
            }

            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                val results = response.body()
                val tracks = results?.tracks?.items

                // Add to search results
                searchResults.value = tracks as MutableList<TrackModel>
            }
        })
    }

    fun addToQueue(track: TrackModel){
        val trackList = playlist.value
        trackList!!.add(track)
        playlist.value = trackList
        Log.d(TAG, "Track queued")
    }

    fun removeFromQueue(position: Int){
        playlist.value?.removeAt(position)
    }

    fun removeAllFromQueue(){
        playlist.value?.clear()
    }

}