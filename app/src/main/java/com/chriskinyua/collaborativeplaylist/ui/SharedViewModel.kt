package com.chriskinyua.collaborativeplaylist.ui


import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.chriskinyua.collaborativeplaylist.controllers.Repository
import com.chriskinyua.collaborativeplaylist.data.TrackModel
import com.chriskinyua.collaborativeplaylist.state.GlobalState


class SharedViewModel(application: Application) : AndroidViewModel(application){
    private val repository = Repository(application as GlobalState)
    val searchResult = repository.searchResults
    val playlist = repository.playlist

    fun addToQueue(track: TrackModel){
        repository.addToQueue(track)
    }

    fun performSearch(query: String){
        repository.performSearch(query)
    }
}