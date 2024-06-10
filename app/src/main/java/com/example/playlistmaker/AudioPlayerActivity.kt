package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val backButton: ImageView = findViewById(R.id.back_from_audio_player_button)
        backButton.setOnClickListener { finish() }

        val jsonTrack = intent.getStringExtra(TRACK_KEY) ?: ""
        val track: Track = Gson().fromJson(jsonTrack, Track::class.java)

        val artworkImageView: ImageView = findViewById(R.id.artwork)
        Glide.with(this)
            .load(track.getArtworkUrl512())
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkImageView)

        val trackNameTextView: TextView = findViewById(R.id.track_name)
        trackNameTextView.text = track.trackName

        val artistNameTextView: TextView = findViewById(R.id.artist_name)
        artistNameTextView.text = track.artistName

        val trackTimeTextView: TextView = findViewById(R.id.track_time)
        trackTimeTextView.text = track.getFormattedTime()

        val trackReleaseTextView: TextView = findViewById(R.id.track_release_year)
        trackReleaseTextView.text = track.getReleaseYear()

        val trackGenreTextView: TextView = findViewById(R.id.track_genre)
        trackGenreTextView.text = track.primaryGenreName

        val trackCountryTextView: TextView = findViewById(R.id.track_country)
        trackCountryTextView.text = track.country

        if (track.collectionName != null) {
            val trackCollectionTextView: TextView = findViewById(R.id.track_collection)
            trackCollectionTextView.text = track.collectionName
            findViewById<Group>(R.id.collection_section).isVisible = true
        }
    }

    private companion object {
        const val TRACK_KEY = "TRACK_KEY"
    }
}