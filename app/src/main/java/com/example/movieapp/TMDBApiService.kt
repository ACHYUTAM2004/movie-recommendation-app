package com.example.movieapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {
    @GET("discover/movie")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String?,
        @Query("primary_release_year") releaseYear: String? = null,
        @Query("vote_average.gte") minRating: String? = null,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String? = "popularity.desc" // Default: High to Low popularity
    ): Call<TMDBResponse>
}


