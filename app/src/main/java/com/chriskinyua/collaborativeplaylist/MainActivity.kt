package com.chriskinyua.collaborativeplaylist

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_scrolling.*

class MainActivity : AppCompatActivity() {

    private lateinit var state: GlobalState
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        state = application as GlobalState

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController( navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        playMusic()
    }

    private fun playMusic() {
        state.spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:2JkFKKEFIDK1YEPiA6Omw3"
            it.playerApi.play(playlistURI)
        }

    }

}
