package com.example.playlistmaker.playlistmenu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.common.utils.toFormattedTime
import com.example.playlistmaker.databinding.FragmentPlaylistMenuBinding
import com.example.playlistmaker.editplaylist.presentation.EditPlaylistFragment
import com.example.playlistmaker.playlist.presentation.mapper.toPlaylistUI
import com.example.playlistmaker.playlist.presentation.model.PlaylistUI
import com.example.playlistmaker.playlistmenu.presentation.viewmodel.PlaylistMenuViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistMenuFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPlaylistMenuBinding? = null
    private val binding get() = _binding!!
    private val id by lazy { requireArguments().getLong(PLAYLIST_ID_KEY) }
    private val viewModel: PlaylistMenuViewModel by viewModel {
        parametersOf(id)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.share.setOnClickListener {
            if (viewModel.tracks.isNotEmpty())
                sharePlaylist()
            else
                showEmptyToast()
        }
        binding.deletePlaylist.setOnClickListener {
            showDeleteDialog()
        }
        binding.editInfo.setOnClickListener {
            dismiss()
            parentFragmentManager.commit {
                replace<EditPlaylistFragment>(
                    R.id.fragment_container,
                    EditPlaylistFragment.createBundle(id)
                )
                addToBackStack(null)
            }
        }

        viewModel.getPlaylist().observe(viewLifecycleOwner) { playlist ->
            if (playlist == null)
                dismiss()
            else
                renderPlaylist(playlist.toPlaylistUI())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderPlaylist(playlistUI: PlaylistUI) {
        with(binding.playlist) {
            name.text = playlistUI.name
            tracksNumber.text = resources.getQuantityString(
                R.plurals.track,
                playlistUI.tracksNumber,
                playlistUI.tracksNumber
            )

            Glide.with(requireContext())
                .load(playlistUI.artworkPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmationDialog)
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.ask_remove_playlist_confirmation)
            .setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteThisPlaylist()
            }
            .show()
    }

    private fun showEmptyToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.empty_tracks_for_share),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun sharePlaylist() {
        val playlist = viewModel.getPlaylist().value!!
        val sb = StringBuilder("${playlist.name}\n")
        sb.append("${playlist.description}\n")
        sb.append(
            "${
                resources.getQuantityString(
                    R.plurals.track,
                    playlist.tracksNumber,
                    playlist.tracksNumber
                )
            }\n"
        )
        viewModel.tracks.forEachIndexed { index, track ->
            sb.append(
                "${index + 1}. ${track.artistName} - ${track.trackName} ${track.trackTimeMillis.toFormattedTime()}\n"
            )
        }
        viewModel.sharePlaylist(sb.toString())
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        fun createBundle(id: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to id)
        }
    }
}