package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var genreInput: EditText
    private lateinit var filtersButton: Button
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Initialize views
        genreInput = findViewById(R.id.genreInput)
        searchButton = findViewById(R.id.searchButton)


        // Search button click listener
        searchButton.setOnClickListener {
            val genre = genreInput.text.toString().trim()

            if (genre.isBlank()) {
                genreInput.error = "Please enter a genre"
                return@setOnClickListener
            }

            // Log the genre being passed to verify
            Toast.makeText(this, "Genre passed: $genre", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MovieListActivity::class.java)
            intent.putExtra("genre", genre)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if user is logged in; if not, redirect to LoginActivity
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
