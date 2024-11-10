package com.example.playlistmaker.audioplayer.presentation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.presentation.mapper.toPlayerTrackUI
import com.example.playlistmaker.audioplayer.presentation.model.PlayerTrackUI
import com.example.playlistmaker.audioplayer.presentation.state.AudioPlayerState
import com.example.playlistmaker.audioplayer.presentation.state.PlayingStatus
import com.example.playlistmaker.audioplayer.presentation.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.playlistsbottomsheet.presentation.PlaylistsBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    private val trackId by lazy { intent.getStringExtra(TRACK_ID_KEY) ?: "" }
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(trackId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            noInternetStub.setOnInflateListener { _, view ->
                val refreshButton: Button = view.findViewById(R.id.refresh_button)
                refreshButton.setOnClickListener { viewModel.refresh() }
            }

            playButton.setOnClickListener {
                if (viewModel.getPlayingStatus().value is PlayingStatus.Playing)
                    viewModel.pause()
                else
                    viewModel.play()
            }

            addToFavouriteButton.setOnClickListener {
                viewModel.changeFavourite()
            }

            addToPlaylistButton.setOnClickListener {
                val playlistsBottomSheetFragment = PlaylistsBottomSheetFragment().apply {
                    arguments = PlaylistsBottomSheetFragment.createBundleOf(trackId)
                }

                playlistsBottomSheetFragment.show(
                    supportFragmentManager,
                    playlistsBottomSheetFragment.tag
                )
            }

        }

        viewModel.getIsFavourite().observe(this, ::renderFavouriteButton)

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
                showContent(state.data.toPlayerTrackUI())
            }
        }
    }

    private fun renderPlayingStatus(status: PlayingStatus) {
        when (status) {
            is PlayingStatus.Playing -> {
                binding.playButton.setImageResource(R.drawable.ic_pause)
                binding.playProgress.text = status.position
            }

            is PlayingStatus.Paused -> {
                binding.playButton.setImageResource(R.drawable.ic_play)
                binding.playProgress.text = status.position
            }

            is PlayingStatus.Completed -> {
                binding.playButton.setImageResource(R.drawable.ic_play)
                binding.playProgress.text = "0:00"
            }
        }
    }

    private fun renderFavouriteButton(isFavourite: Boolean) {
        if (isFavourite)
            binding.addToFavouriteButton.setImageResource(R.drawable.ic_favourite)
        else
            binding.addToFavouriteButton.setImageResource(R.drawable.ic_outlined_favourite)
    }

    private fun showContent(trackUI: PlayerTrackUI) {
        Glide.with(this)
            .load(trackUI.artworkUrl512)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.artwork)

        binding.trackName.text = trackUI.trackName

        binding.artistName.text = trackUI.artistName

        binding.trackTime.text = trackUI.trackTime

        binding.trackReleaseYear.text = trackUI.releaseYear

        binding.trackGenre.text = trackUI.primaryGenreName

        binding.trackCountry.text = trackUI.country

        if (trackUI.collectionName != null) {
            binding.trackCollection.text = trackUI.collectionName
            binding.collectionSection.isVisible = true
        }

        binding.content.isVisible = true

        binding.loading.isVisible = false
        binding.noInternetStub.isVisible = false
    }

    private fun showLoading() {
        binding.loading.isVisible = true

        binding.noInternetStub.isVisible = false
        binding.content.isVisible = false
    }

    private fun showError() {
        binding.noInternetStub.isVisible = true

        binding.loading.isVisible = false
        binding.content.isVisible = false
    }

    override fun onStop() {
        super.onStop()
        viewModel.pause()
    }

    companion object {
        private const val TRACK_ID_KEY = "TRACK_ID_KEY"
        fun createBundleOf(trackId: String): Bundle {
            return bundleOf(TRACK_ID_KEY to trackId)
        }
    }
}