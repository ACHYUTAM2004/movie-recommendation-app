package com.example.movieapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titleText)
        val yearText: TextView = view.findViewById(R.id.yearText)
        val popularityText: TextView = view.findViewById(R.id.ratingText) // Change from rating to popularity
        val posterImage: ImageView = view.findViewById(R.id.posterImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.titleText.text = movie.title
        holder.yearText.text = movie.release_date
        holder.popularityText.text = "Popularity: ${movie.popularity}"

        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(holder.posterImage)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
