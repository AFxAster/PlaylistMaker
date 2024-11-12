package com.example.playlistmaker.editplaylist.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.editplaylist.presentation.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.newplaylist.presentation.NewPlaylistFragment
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : NewPlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel {
        val id = requireArguments().getLong(PLAYLIST_ID_KEY)
        parametersOf(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.title = getString(R.string.edit)
            createButton.text = getText(R.string.save)
            createButton.setOnClickListener {
                val playlist =
                    (viewModel.getState().value as PlaylistState.Content).playlist!!.copy(
                        name = nameEditText.text.toString().trim(),
                        description = descriptionEditText.text!!.let {
                            if (it.isNotBlank()) it.toString().trim() else ""
                        },
                        artworkPath = lastUri?.toString()
                    )
                viewModel.updatePlaylist(playlist)
                back()
            }
        }

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            if (state is PlaylistState.Content) {
                state.playlist?.let { renderContent(it) }
            }
        }
    }

    private fun renderContent(playlist: Playlist) {
        with(binding) {
            nameEditText.setText(playlist.name)
            descriptionEditText.setText(playlist.description)

            Glide.with(requireContext())
                .load(playlist.artworkPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }
        lastUri = Uri.parse(playlist.artworkPath)
    }

    override fun back() {
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        fun createBundle(id: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to id)
        }
    }
}