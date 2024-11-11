package com.example.playlistmaker.playlistLibrary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.presentation.PlaylistViewHolder
import com.example.playlistmaker.common.presentation.PlaylistsAdapter
import com.example.playlistmaker.common.presentation.state.PlaylistsViewState
import com.example.playlistmaker.databinding.FragmentPlaylistLibraryBinding
import com.example.playlistmaker.playlist.presentation.PlaylistFragment
import com.example.playlistmaker.playlistLibrary.presentation.mapper.toPlaylistItemUI
import com.example.playlistmaker.playlistLibrary.presentation.model.PlaylistItemUI
import com.example.playlistmaker.playlistLibrary.presentation.state.PlaylistLibraryState
import com.example.playlistmaker.playlistLibrary.presentation.viewmodel.PlaylistLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistLibraryFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistLibraryBinding
    private val viewModel: PlaylistLibraryViewModel by viewModel()
    private val playlistsAdapter = PlaylistsAdapter(PlaylistsViewState.Grid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlaylistLibraryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        playlistsAdapter.onPlaylistClickListener =
            PlaylistViewHolder.OnPlaylistClickListener { id ->
                findNavController().navigate(
                    R.id.action_libraryFragment_to_playlistFragment,
                    PlaylistFragment.createBundle(id)
                )
            }

        with(binding) {
            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
            }
            playlistsRecyclerView.adapter = playlistsAdapter
        }
    }

    private fun render(state: PlaylistLibraryState) {
        when (state) {
            is PlaylistLibraryState.Empty -> {
                showEmpty()
            }

            is PlaylistLibraryState.Content -> {
                showContent(state.playlists.map { it.toPlaylistItemUI() })
            }
        }
    }

    private fun showContent(playlists: List<PlaylistItemUI>) {
        playlistsAdapter.playlists = playlists
        binding.playlistsRecyclerView.isVisible = true

        binding.emptyViewStub.isVisible = false
    }

    private fun showEmpty() {
        binding.emptyViewStub.isVisible = true

        binding.playlistsRecyclerView.isVisible = false
    }

    companion object {
        fun newInstance(): PlaylistLibraryFragment {
            return PlaylistLibraryFragment()
        }
    }
}