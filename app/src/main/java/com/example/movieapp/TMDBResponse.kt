package com.example.movieapp

data class TMDBResponse(
    val page: Int, // Current page number
    val total_pages: Int, // Total number of pages available
    val results: List<Movie> // List of movies for the current page
)

data class Movie(
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String?,
    val overview: String,
    val popularity: Double
)
