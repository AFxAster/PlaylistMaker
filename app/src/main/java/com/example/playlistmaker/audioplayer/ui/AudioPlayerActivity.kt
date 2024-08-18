package com.example.playlistmaker.audioplayer.ui

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.presentation.model.PlayerTrackUI
import com.example.playlistmaker.audioplayer.presentation.state.AudioPlayerState
import com.example.playlistmaker.audioplayer.presentation.state.PlayingStatus
import com.example.playlistmaker.audioplayer.presentation.viewmodel.AudioPlayerViewModel

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var content: View
    private lateinit var loading: ProgressBar
    private lateinit var noInternetStub: ViewStub

    private lateinit var playProgressTextView: TextView
    private lateinit var playButton: ImageView

    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val backButton: ImageView = findViewById(R.id.back_from_audio_player_button)
        backButton.setOnClickListener { finish() }

        content = findViewById(R.id.content)
        loading = findViewById(R.id.progress_bar)
        noInternetStub = findViewById(R.id.no_internet_stub)
        noInternetStub.setOnInflateListener { _, view ->
            val refreshButton: Button = view.findViewById(R.id.refresh_button)
            refreshButton.setOnClickListener { viewModel.refresh() }
        }
        playProgressTextView = findViewById(R.id.play_progress)
        playButton = findViewById(R.id.play_button)
        playButton.setOnClickListener {
            if (viewModel.getPlayingStatus().value is PlayingStatus.Playing)
                viewModel.pause()
            else
                viewModel.play()
        }

        val trackId = intent.getStringExtra(TRACK_ID_KEY) ?: ""

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(trackId)
        )[AudioPlayerViewModel::class.java]

        viewModel.getState().observe(this, ::render)

        viewModel.getPlayingStatus().observe(this, ::renderPlayingStatus)
    }

    private fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.Loading -> {
                showLoading()
            }

            is AudioPlayerState.Error -> {
                showError()
            }

            is AudioPlayerState.Content -> {
                showContent(state.data)
            }
        }
    }

    private fun renderPlayingStatus(status: PlayingStatus) {
        when (status) {
            is PlayingStatus.Playing -> {
                playButton.setImageResource(R.drawable.ic_pause)
                playProgressTextView.text = status.position
            }

            is PlayingStatus.Paused -> {
                playButton.setImageResource(R.drawable.ic_play)
                playProgressTextView.text = status.position
            }

            is PlayingStatus.Completed -> {
                playButton.setImageResource(R.drawable.ic_play)
                playProgressTextView.text = "0:00"
            }
        }
    }

    private fun showContent(trackUI: PlayerTrackUI) {
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

        content.isVisible = true

        loading.isVisible = false
        noInternetStub.isVisible = false
    }

    private fun showLoading() {
        loading.isVisible = true

        noInternetStub.isVisible = false
        content.isVisible = false
    }

    private fun showError() {
        noInternetStub.isVisible = true

        loading.isVisible = false
        content.isVisible = false
    }

    override fun onStop() {
        super.onStop()
        viewModel.pause()
    }

    private companion object {
        const val TRACK_ID_KEY = "TRACK_ID_KEY"
    }
}