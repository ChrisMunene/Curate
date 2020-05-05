package com.chriskinyua.collaborativeplaylist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.google.android.material.snackbar.Snackbar
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    private lateinit var state: GlobalState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        state = application as GlobalState
        Log.d("ScrollingActivity", state.spotifyAppRemote.toString())

        fab.setOnClickListener { view ->
            playMusic()
            Snackbar.make(view, "Playing music...", Snackbar.LENGTH_LONG)
                .setAction("Stop", null).show()
        }
    }

    private fun playMusic() {
        state.spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:2JkFKKEFIDK1YEPiA6Omw3"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("ScrollingActivity", track.name + " by " + track.artist.name)
            }
        }

    }
}
