package com.example.movieapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter
    private val apiKey = "0f5bf508ac7138552661be0c6761c6e3" // Replace with your TMDB API key
    private var currentPage = 1
    private var isLastPage = false
    private var genreId: Int? = null
    private var sortByPopularity: String? = "popularity.desc" // Default sorting by popularity
    private var sortByReleaseDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        val recyclerView = findViewById<RecyclerView>(R.id.movieRecyclerView)
        val prevPageButton = findViewById<Button>(R.id.prevPageButton)
        val nextPageButton = findViewById<Button>(R.id.nextPageButton)
        val popularitySpinner = findViewById<Spinner>(R.id.sortByRatingSpinner)
        val releaseDateSpinner = findViewById<Spinner>(R.id.sortByReleaseDateSpinner)

        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(emptyList())
        recyclerView.adapter = movieAdapter

        val genre = intent.getStringExtra("genre") ?: ""
        genreId = getGenreId(genre)

        if (genreId == null) {
            Toast.makeText(this, "Invalid genre", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch the first page with default sorting
        fetchMovies()

        // Set up the popularity spinner
        val popularityOptions = arrayOf("High to Low", "Low to High")
        val popularityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            popularityOptions
        )
        popularityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        popularitySpinner.adapter = popularityAdapter

        popularitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Change sorting based on popularity selection
                sortByPopularity = when (position) {
                    0 -> "popularity.desc" // High to Low
                    1 -> "popularity.asc"  // Low to High
                    else -> null
                }
                fetchMovies()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set up the release date spinner
        val releaseDateOptions = arrayOf("New to Old", "Old to New")
        val releaseDateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            releaseDateOptions
        )
        releaseDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        releaseDateSpinner.adapter = releaseDateAdapter

        releaseDateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Change sorting based on release date selection
                sortByReleaseDate = when (position) {
                    0 -> "primary_release_date.desc" // New to Old
                    1 -> "primary_release_date.asc"  // Old to New
                    else -> null
                }
                fetchMovies()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Pagination logic
        prevPageButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                fetchMovies()
                nextPageButton.isEnabled = true
            }
            if (currentPage == 1) {
                prevPageButton.isEnabled = false
            }
        }

        nextPageButton.setOnClickListener {
            if (!isLastPage) {
                currentPage++
                fetchMovies()
                prevPageButton.isEnabled = true
            }
        }
    }

    private fun fetchMovies() {
        if (genreId == null) {
            Toast.makeText(this, "Invalid genre", Toast.LENGTH_SHORT).show()
            return
        }

        // Determine the sorting option based on the selected filters
        var sortBy: String? = null

        // First check if popularity sorting is selected
        if (sortByPopularity != null) {
            sortBy = sortByPopularity
        }

        // Then, check if release date sorting is selected
        if (sortByReleaseDate != null) {
            sortBy = sortByReleaseDate
        }

        // If neither is selected, default to popularity descending
        if (sortBy == null) {
            sortBy = "popularity.desc"
        }

        // Show a Toast to debug sorting option
        Toast.makeText(this, "Sorting by: $sortBy", Toast.LENGTH_SHORT).show()

        // Make API call with the selected sort option
        RetrofitInstance.api.getMovies(
            apiKey = apiKey,
            genreId = genreId.toString(),
            releaseYear = null, // No specific year filter applied here, can add if needed
            minRating = null,   // No rating filter applied here, can add if needed
            page = currentPage,
            sortBy = sortBy
        ).enqueue(object : Callback<TMDBResponse> {
            override fun onResponse(call: Call<TMDBResponse>, response: Response<TMDBResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        movieAdapter.updateMovies(it.results)
                        isLastPage = it.page == it.total_pages
                    }
                } else {
                    Toast.makeText(this@MovieListActivity, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                Toast.makeText(this@MovieListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getGenreId(genre: String): Int? {
        val genreMap = mapOf(
            "Action" to 28,
            "Adventure" to 12,
            "Animation" to 16,
            "Comedy" to 35,
            "Crime" to 80,
            "Documentary" to 99,
            "Drama" to 18,
            "Family" to 10751,
            "Fantasy" to 14,
            "History" to 36,
            "Horror" to 27,
            "Music" to 10402,
            "Mystery" to 9648,
            "Romance" to 10749,
            "Science Fiction" to 878,
            "TV Movie" to 10770,
            "Thriller" to 53,
            "War" to 10752,
            "Western" to 37
        )
        return genreMap[genre]
    }
}
