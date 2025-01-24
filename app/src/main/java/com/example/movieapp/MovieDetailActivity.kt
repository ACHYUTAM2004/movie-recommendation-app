package com.example.movieapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var moviePoster: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        moviePoster = findViewById(R.id.moviePoster)
        movieTitle = findViewById(R.id.movieTitle)
        movieReleaseDate = findViewById(R.id.movieReleaseDate)
        movieRating = findViewById(R.id.movieRating)
        movieSummary = findViewById(R.id.movieSummary)

        // Get the movie data from the intent
        val posterPath = intent.getStringExtra("posterPath")
        val title = intent.getStringExtra("title")
        val releaseDate = intent.getStringExtra("releaseDate")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val summary = intent.getStringExtra("summary")

        // Load poster image using Glide or Picasso
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/$posterPath") // Full image URL
            .into(moviePoster)

        // Set the other movie details
        movieTitle.text = title
        movieReleaseDate.text = "Release Date: $releaseDate"
        movieRating.text = "Rating: $rating"
        movieSummary.text = summary
    }
}
