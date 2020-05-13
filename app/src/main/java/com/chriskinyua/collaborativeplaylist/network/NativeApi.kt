package com.chriskinyua.collaborativeplaylist.network

import com.chriskinyua.collaborativeplaylist.data.Recommendation
import com.chriskinyua.collaborativeplaylist.data.RecommendationResults
import com.chriskinyua.collaborativeplaylist.data.SearchResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface NativeApi{

    companion object{
        const val BASE_URL: String = "https://api-starter-spotify.herokuapp.com/"
    }

    @GET("recommendations")
    fun getRecommendations(
               @Query("seed") seed: String,
               @Query("token") token: String): Call<RecommendationResults>
}