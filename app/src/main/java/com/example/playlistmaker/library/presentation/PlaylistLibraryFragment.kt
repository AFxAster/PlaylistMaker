package com.example.playlistmaker.library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistLibraryBinding
import com.example.playlistmaker.library.presentation.state.PlaylistLibraryState
import com.example.playlistmaker.library.presentation.viewmodel.PlaylistLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistLibraryFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistLibraryBinding
    private val viewModel: PlaylistLibraryViewModel by viewModel()

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
    }

    private fun render(state: PlaylistLibraryState) {
        when (state) {
            is PlaylistLibraryState.Empty -> {
                showEmpty()
            }

            is PlaylistLibraryState.Content -> {}
        }
    }

    private fun showEmpty() {
        binding.emptyViewStub.isVisible = true
    }

    companion object {
        fun newInstance(): PlaylistLibraryFragment {
            return PlaylistLibraryFragment()
        }
    }
}