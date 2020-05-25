package com.chriskinyua.collaborativeplaylist.network

import com.chriskinyua.collaborativeplaylist.data.RecommendationResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendApi {

    companion object{
        const val BASE_URL: String = "https://spotify-colab.herokuapp.com"
    }

    @GET("/rooms/create")
    fun createRoom(
        @Query("name") name: String): Call<RecommendationResults>
}