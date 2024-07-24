package com.example.playlistmaker.presentation.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.mapper.toPlayerTrackUI
import com.example.playlistmaker.presentation.model.PlayerTrackUI
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var trackUI: PlayerTrackUI
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playProgressTextView: TextView
    private lateinit var playButton: ImageView
    private val playProgressRefreshRunnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, REFRESH_PLAY_PROGRESS_DELAY)
            val position = audioPlayerInteractor.getCurrentPosition() ?: return
            playProgressTextView.text =
                SimpleDateFormat(
                    "m:ss",
                    Locale.getDefault()
                ).format(position)
        }
    }

    private val audioPlayerInteractor: AudioPlayerInteractor =
        Creator.provideAudioPlayerInteractor()
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val backButton: ImageView = findViewById(R.id.back_from_audio_player_button)
        backButton.setOnClickListener { finish() }

        val jsonTrack = intent.getStringExtra(TRACK_KEY) ?: ""
        trackUI = Gson().fromJson(jsonTrack, Track::class.java).toPlayerTrackUI()
        showInfo()

        playProgressTextView = findViewById(R.id.play_progress)
        playButton = findViewById(R.id.play_button)
        playButton.apply {
            setOnClickListener {
                if (isPlaying) pausePlayer()
                else resumePlayer()
            }
            isClickable = false
        }

        initPlayer()
        handler.postDelayed(playProgressRefreshRunnable, REFRESH_PLAY_PROGRESS_DELAY)
    }

    private fun initPlayer() {
        audioPlayerInteractor.initWithSource(
            src = trackUI.previewUrl,
            onPrepared = {
                playButton.isClickable = true
            })
        audioPlayerInteractor.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play)
            isPlaying = false
            playProgressTextView.text = "0:00"
        }
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pause()
        playButton.setImageResource(R.drawable.ic_play)
        isPlaying = false
    }

    private fun resumePlayer() {
        audioPlayerInteractor.start()
        playButton.setImageResource(R.drawable.ic_pause)
        isPlaying = true
    }

    private fun showInfo() {
        val artworkImageView: ImageView = findViewById(R.id.artwork)
        Glide.with(this)
            .load(trackUI.artworkUrl512)
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkImageView)

        val trackNameTextView: TextView = findViewById(R.id.track_name)
        trackNameTextView.text = trackUI.trackName

        val artistNameTextView: TextView = findViewById(R.id.artist_name)
        artistNameTextView.text = trackUI.artistName

        val trackTimeTextView: TextView = findViewById(R.id.track_time)
        trackTimeTextView.text = trackUI.trackTime

        val trackReleaseTextView: TextView = findViewById(R.id.track_release_year)
        trackReleaseTextView.text = trackUI.releaseYear

        val trackGenreTextView: TextView = findViewById(R.id.track_genre)
        trackGenreTextView.text = trackUI.primaryGenreName

        val trackCountryTextView: TextView = findViewById(R.id.track_country)
        trackCountryTextView.text = trackUI.country

        if (trackUI.collectionName != null) {
            val trackCollectionTextView: TextView = findViewById(R.id.track_collection)
            trackCollectionTextView.text = trackUI.collectionName
            findViewById<Group>(R.id.collection_section).isVisible = true
        }
    }

    override fun onStop() {
        super.onStop()
        pausePlayer()
        handler.removeCallbacks(playProgressRefreshRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.release()
    }

    private companion object {
        const val TRACK_KEY = "TRACK_KEY"
        const val REFRESH_PLAY_PROGRESS_DELAY = 300L
    }
}