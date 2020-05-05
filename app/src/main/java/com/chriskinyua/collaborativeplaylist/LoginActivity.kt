package com.chriskinyua.collaborativeplaylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "23435e1e97574bbab9f95fb2923baefe"
    private val REQUEST_CODE = 1337
    private val REDIRECT_URI = "https://com.chriskinyua.collaborativeplaylist/callback"
    private lateinit var state: GlobalState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        state = application as GlobalState

        spotifyLoginBtn.setOnClickListener {
            initiateLogin()
        }

    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                state.spotifyAppRemote = appRemote
                tvStatus.text = "Connected! Yay"
                Log.d("LoginActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote

            }

            override fun onFailure(throwable: Throwable) {
                tvStatus.text = "Something Went Wrong"
                tvStatus.setTextColor(resources.getColor(R.color.colorAccent))
                Log.e("LoginActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response =
                AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    state.ACCESS_TOKEN = response.accessToken
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                }
                AuthenticationResponse.Type.ERROR -> {
                    tvStatus.text = response.error
                    tvStatus.setTextColor(resources.getColor(R.color.colorAccent))
                    Log.e("LoginActivity", response.error)
                }
                else -> {
                    tvStatus.text = "Something went wrong. Please try again"
                    tvStatus.setTextColor(resources.getColor(R.color.colorAccent))
                    Log.e("LoginActivity", response.error)
                }
            }
        }
    }

    private fun initiateLogin(){
        val builder = AuthenticationRequest.Builder(
            CLIENT_ID,
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )

        builder.setScopes(arrayOf("user-read-email", "user-read-private"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }
}

