package com.chriskinyua.collaborativeplaylist.network

import com.chriskinyua.collaborativeplaylist.data.SearchResult
import retrofit2.Call
import retrofit2.http.*

interface SpotifyWebApi{

    companion object{
        const val BASE_URL: String = "https://api.spotify.com/v1/"
    }

    @GET("search")
    fun search(@HeaderMap headers: Map<String, String>,
                     @Query("q") query: String,
               @Query("type") type: String): Call<SearchResult>
}