package com.example.bizpole

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bizpole.databinding.ActivitySongDetailBinding

class SongDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySongDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val trackName = bundle?.getString("trackName")
        val artistName = bundle?.getString("artistName")
        val price = bundle?.getDouble("price")
        val genre = bundle?.getString("primaryGenreName")
        val description = bundle?.getString("longDescription","Description is Not available")
        val image = bundle?.getString("artworkUrl100")

        binding.textViewArtistName.text = "Artist Name : $artistName"
        binding.textViewGenre.text = "Genre : $genre"
        binding.textViewDescription.text = description
        binding.textViewPrice.text = "Price : $price"
        binding.textViewTrackName.text = "Track Name : $trackName"

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.gray_background)
            .into(binding.imageViewArtwork)

    }
}