package com.example.playlistmaker.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.presentation.AudioPlayerActivity
import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.common.presentation.TracksAdapter
import com.example.playlistmaker.common.utils.debounceWithFirstCall
import com.example.playlistmaker.common.utils.toFormattedTime
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.playlist.presentation.mapper.toPlaylistUI
import com.example.playlistmaker.playlist.presentation.model.PlaylistUI
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlist.presentation.viewmodel.PlaylistViewModel
import com.example.playlistmaker.playlistmenu.presentation.PlaylistMenuFragment
import com.example.playlistmaker.search.presentation.TrackViewHolder
import com.example.playlistmaker.search.presentation.mapper.toTrackUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.ceil

class PlaylistFragment : Fragment() {
    private val id by lazy { requireArguments().getLong(PLAYLIST_ID_KEY) }
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(id)
    }
    private val tracksAdapter = TracksAdapter()
    private val debounceClick = debounceWithFirstCall<String>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        action = ::onTrackClick
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter.onTrackClickListener = TrackViewHolder.OnTrackClickListener(debounceClick)
        tracksAdapter.onTrackLongClickListener =
            TrackViewHolder.OnTrackClickListener(::onTrackLongClick)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
//            toolbar.navigationIcon = ResourcesCompat.getDrawable(
//                resources,
//                R.drawable.ic_arrow_back,
//                requireContext().theme
//            ) // todo
            toolbar.navigationIcon?.setTint(
                resources.getColor(
                    R.color.YP_black,
                    requireContext().theme
                )
            )
            tracksRecyclerView.adapter = tracksAdapter
            share.setOnClickListener {
                if (tracksAdapter.trackList.isEmpty())
                    showEmptyToast()
                else
                    sharePlaylist()
            }
            menu.setOnClickListener {
                val playlistMenuFragment = PlaylistMenuFragment()
                playlistMenuFragment.arguments =
                    PlaylistMenuFragment.createBundle(id)
                playlistMenuFragment.show(
                    parentFragmentManager,
                    playlistMenuFragment.tag
                )
            }
        }

        viewModel.getState().observe(viewLifecycleOwner, ::render)
        viewModel.getTracks().observe(viewLifecycleOwner) { tracks -> renderTracks(tracks) }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Loading -> renderLoading()
            is PlaylistState.Content -> {
                if (state.playlist == null)
                    findNavController().navigateUp()
                else
                    renderContent(state.playlist.toPlaylistUI())
            }
        }
    }

    private fun renderLoading() {
        // todo
    }

    private fun renderContent(playlist: PlaylistUI) {
        with(binding) {
            name.text = playlist.name
            if (playlist.description.isNotEmpty()) {
                description.isVisible = true
                description.text = playlist.description
            }

            Glide.with(requireContext())
                .load(playlist.artworkPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }
    }

    private fun renderTracks(tracks: List<Track>) {
        tracksAdapter.trackList = tracks.map { it.toTrackUI() }

        val tracksNumber = tracks.size
        val tracksTime = ceil(tracks.sumOf { it.trackTimeMillis } / 60000.0).toInt()

        with(binding) {
            number.text = resources.getQuantityString(
                R.plurals.track,
                tracksNumber,
                tracksNumber
            )

            time.text = resources.getQuantityString(
                R.plurals.minute,
                tracksTime,
                tracksTime
            )
        }
    }

    private fun onTrackClick(trackId: String) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerActivity,
            AudioPlayerActivity.createBundleOf(trackId)
        )
    }

    private fun onTrackLongClick(trackId: String) {
        showDeleteDialog(trackId)
    }

    private fun showDeleteDialog(trackId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.remove_track)
            .setMessage(R.string.ask_remove_track_confirmation)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteTrack(trackId)
            }.show()
    }


    private fun showEmptyToast() {
        Toast.makeText(
            requireContext(),
            "В этом плейлисте нет списка треков, которым можно поделиться",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun sharePlaylist() {
        val playlist = (viewModel.getState().value as? PlaylistState.Content)?.playlist!!
        val sb = StringBuilder(
            "${playlist.name}\n${
                resources.getQuantityString(
                    R.plurals.track,
                    playlist.tracksNumber,
                    playlist.tracksNumber
                )
            }\n"
        )
        viewModel.getTracks().value?.forEachIndexed { index, track ->
            sb.append(
                "${index + 1}. ${track.artistName} - ${track.trackName} ${track.trackTimeMillis.toFormattedTime()}\n"
            )
        }
        viewModel.sharePlaylist(sb.toString())
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        fun createBundle(id: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to id)
        }
    }
}