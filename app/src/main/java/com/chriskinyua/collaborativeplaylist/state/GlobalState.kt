package com.chriskinyua.collaborativeplaylist.state

import android.app.Application
import com.chriskinyua.collaborativeplaylist.network.SpotifyWebApi
import com.spotify.android.appremote.api.SpotifyAppRemote

class GlobalState : Application() {
     var spotifyAppRemote: SpotifyAppRemote? = null
     var spotifyWebApi: SpotifyWebApi? = null
     var ACCESS_TOKEN: String? = null
     var spotifyHeaders: HashMap<String, String> = HashMap()

     override fun onCreate() {
          super.onCreate()
          spotifyHeaders["Accept"] = "application/json"
          spotifyHeaders["Content-Type"] = "application/json"
     }

     override fun onTerminate() {
          super.onTerminate()
          if(spotifyAppRemote != null){
               spotifyAppRemote?.let {
                    SpotifyAppRemote.disconnect(it)
               }
          }
     }
}