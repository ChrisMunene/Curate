package com.chriskinyua.collaborativeplaylist

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chriskinyua.collaborativeplaylist.data.RecommendationResults
import com.chriskinyua.collaborativeplaylist.data.TrackModel
import com.chriskinyua.collaborativeplaylist.network.RecommendationsApi
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.chriskinyua.collaborativeplaylist.ui.SharedViewModel
import com.chriskinyua.collaborativeplaylist.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), RecommendationDialog.RecommendationHandler {

    private lateinit var state: GlobalState
    private var queueInitialized = false
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var recommendationsApi: RecommendationsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        sharedViewModel =
            ViewModelProviders.of(this).get(SharedViewModel::class.java)

        state = application as GlobalState

        state.socket?.connect()

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController( navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val retrofit = Retrofit.Builder()
            .baseUrl(RecommendationsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recommendationsApi = retrofit.create(RecommendationsApi::class.java)

    }

    fun addToQueue(track: TrackModel){

        sharedViewModel.addToQueue(track)

        if(!queueInitialized){
            state.spotifyAppRemote?.playerApi?.play(track.uri)
            queueInitialized = true

        } else {
            val handler = Handler()
            handler.postDelayed({
                state.spotifyAppRemote?.playerApi?.queue(track.uri)
            },
                1000
            )

        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun getRecommendations(seed: String) {
        Log.d(TAG, "Getting Recommendations")
        val generate = recommendationsApi.getRecommendations(seed, state.ACCESS_TOKEN!!)
        val navHost = supportFragmentManager.primaryNavigationFragment
        val homeFragment = navHost?.childFragmentManager?.primaryNavigationFragment as HomeFragment?
        homeFragment?.showProgressBar()
        generate.enqueue(object : Callback<RecommendationResults> {
            override fun onFailure(call: Call<RecommendationResults>, t: Throwable) {
                Log.e(TAG, "Couldn't perform recommendation request", t)
                homeFragment?.hideProgressBar()
            }

            override fun onResponse(
                call: Call<RecommendationResults>,
                response: Response<RecommendationResults>
            ) {
                val results = response.body()

                val recommendations = results?.recommendations

                Log.d(TAG, "Recommendations ${recommendations?.size}")

                homeFragment?.hideProgressBar()

                recommendations?.forEach{recommendation ->
                    addToQueue(recommendation.track)
                }


            }

        })
    }

    fun removeFromQueue(position: Int){
        Log.d(TAG, "Removed at $position")
        sharedViewModel.removeFromQueue(position)
    }

    fun removeAllFromQueue(){
        sharedViewModel.removeAllFromQueue()
    }

    fun removeFromSearchResults(position: Int){
        sharedViewModel.removeFromSearchResults(position)
    }

    fun removeAllFromSearchResults(){
        sharedViewModel.removeAllFromSearchResults()
    }

}
