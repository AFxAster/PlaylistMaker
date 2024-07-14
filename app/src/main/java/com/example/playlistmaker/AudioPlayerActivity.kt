package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private var playerState = PLAYER_STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playProgressTextView: TextView
    private lateinit var playButton: ImageView
    private val playProgressRefreshRunnable = object : Runnable {
        override fun run() {
            playProgressTextView.text =
                SimpleDateFormat(
                    "m:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, REFRESH_PLAY_PROGRESS_DELAY)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val backButton: ImageView = findViewById(R.id.back_from_audio_player_button)
        backButton.setOnClickListener { finish() }

        val jsonTrack = intent.getStringExtra(TRACK_KEY) ?: ""
        track = Gson().fromJson(jsonTrack, Track::class.java)

        initInfo()
        initPlayer()
    }

    private fun initPlayer() {
        playButton = findViewById(R.id.play_button)
        playProgressTextView = findViewById(R.id.play_progress)
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PLAYER_STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play)
            handler.removeCallbacks(playProgressRefreshRunnable)
            playProgressTextView.text = "0:00"
            playerState = PLAYER_STATE_PREPARED
        }

        playButton.setOnClickListener {
            when (playerState) {
                PLAYER_STATE_PLAYING -> pausePlayer()
                PLAYER_STATE_PREPARED, PLAYER_STATE_PAUSED -> resumePlayer()
            }
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play)
        handler.removeCallbacks(playProgressRefreshRunnable)
        playerState = PLAYER_STATE_PAUSED
    }

    private fun resumePlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause)
        handler.postDelayed(playProgressRefreshRunnable, REFRESH_PLAY_PROGRESS_DELAY)
        playerState = PLAYER_STATE_PLAYING
    }

    private fun initInfo() {
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

    override fun onStop() {
        super.onStop()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private companion object {
        const val TRACK_KEY = "TRACK_KEY"
        const val PLAYER_STATE_DEFAULT = 0
        const val PLAYER_STATE_PREPARED = 1
        const val PLAYER_STATE_PLAYING = 2
        const val PLAYER_STATE_PAUSED = 3
        const val REFRESH_PLAY_PROGRESS_DELAY = 300L
    }
}