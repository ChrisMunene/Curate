package com.chriskinyua.collaborativeplaylist.state

import android.app.Application
import com.spotify.android.appremote.api.SpotifyAppRemote

class GlobalState : Application() {
     var spotifyAppRemote: SpotifyAppRemote? = null
     var ACCESS_TOKEN: String? = null

     override fun onTerminate() {
          super.onTerminate()
          if(spotifyAppRemote != null){
               spotifyAppRemote?.let {
                    SpotifyAppRemote.disconnect(it)
               }
          }
     }
}