package com.example.playlistmaker.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.playlist.presentation.mapper.toPlaylistUI
import com.example.playlistmaker.playlist.presentation.model.PlaylistUI
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlist.presentation.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel() {
        val id = requireArguments().getLong(PLAYLIST_ID_KEY)
        parametersOf(id)
    }

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

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.navigationIcon?.setTint(resources.getColor(R.color.YP_black))// todo ??
        }
        viewModel.getState().observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Loading -> renderLoading()
            is PlaylistState.Content -> renderContent(state.playlist.toPlaylistUI())
        }
    }

    private fun renderLoading() {

    }

    private fun renderContent(playlist: PlaylistUI) {
        with(binding) {

            name.text = playlist.name
            if (playlist.description.isNotEmpty()) {
                description.isVisible = true
                description.text = playlist.description
            }
            number.text = resources.getQuantityString(
                R.plurals.track,
                playlist.tracksNumber,
                playlist.tracksNumber
            )
            time.text = resources.getQuantityString(
                R.plurals.minute,
                playlist.tracksNumber,
                playlist.tracksNumber
            ) // todo на время поменять

            Glide.with(requireContext())
                .load(playlist.artworkPath)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }
    }


    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        fun createBundle(id: Long): Bundle {
            return bundleOf(PLAYLIST_ID_KEY to id)
        }
    }
}