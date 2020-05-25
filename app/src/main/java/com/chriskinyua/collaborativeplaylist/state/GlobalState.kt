package com.chriskinyua.collaborativeplaylist.state

import android.app.Application
import android.util.Log
import com.chriskinyua.collaborativeplaylist.network.BackendApi
import com.chriskinyua.collaborativeplaylist.network.SpotifyWebApi
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.spotify.android.appremote.api.SpotifyAppRemote
import java.net.URISyntaxException

class GlobalState : Application() {
     var spotifyAppRemote: SpotifyAppRemote? = null
     var spotifyWebApi: SpotifyWebApi? = null
     var ACCESS_TOKEN: String? = null
     var spotifyHeaders: HashMap<String, String> = HashMap()
     var socket: Socket? = null
     val TAG = GlobalState::class.java.simpleName

     override fun onCreate() {
          super.onCreate()
          spotifyHeaders["Accept"] = "application/json"
          spotifyHeaders["Content-Type"] = "application/json"
          try {
               socket = IO.socket(BackendApi.BASE_URL)
          } catch (e: URISyntaxException) {
               Log.e(TAG, "Couldn't initialize socket", e)
          }

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