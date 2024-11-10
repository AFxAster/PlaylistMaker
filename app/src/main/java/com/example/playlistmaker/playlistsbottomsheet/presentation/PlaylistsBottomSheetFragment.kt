package com.example.playlistmaker.playlistsbottomsheet.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.common.presentation.PlaylistViewHolder
import com.example.playlistmaker.common.presentation.PlaylistsAdapter
import com.example.playlistmaker.common.presentation.state.PlaylistsViewState
import com.example.playlistmaker.common.utils.debounceWithFirstCall
import com.example.playlistmaker.databinding.FragmentPlaylistsBottomSheetBinding
import com.example.playlistmaker.newplaylist.presentation.NewPlaylistFragment
import com.example.playlistmaker.playlistLibrary.presentation.mapper.toPlaylistItemUI
import com.example.playlistmaker.playlistsbottomsheet.presentation.state.AddingStatus
import com.example.playlistmaker.playlistsbottomsheet.presentation.viewmodel.PlaylistsBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPlaylistsBottomSheetBinding

    private val playlistsAdapter = PlaylistsAdapter(PlaylistsViewState.Linear)

    private val trackId by lazy { requireArguments().getString(TRACK_ID_KEY) }
    private val viewModel: PlaylistsBottomSheetViewModel by viewModel {
        parametersOf(trackId)
    }

    private val debounceClick = debounceWithFirstCall<Long>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        action = ::onPlaylistClick
    )

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlaylistsBottomSheetBinding.inflate(layoutInflater)
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

        with(binding) {
            newPlaylistButton.setOnClickListener {
                dismiss()
                parentFragmentManager.commit {
                    replace<NewPlaylistFragment>(R.id.fragment_container)
                    addToBackStack(null)
                }
            }

            playlistsRecyclerView.adapter = playlistsAdapter
        }
        playlistsAdapter.onPlaylistClickListener =
            PlaylistViewHolder.OnPlaylistClickListener(debounceClick)

        viewModel.getPlaylists().observe(viewLifecycleOwner) { list ->
            playlistsAdapter.playlists = list.map { it.toPlaylistItemUI() }
        }

        viewModel.getAddingStatus().observe(viewLifecycleOwner) { status ->
            showAddedDialog(status)
        }
    }


    private fun onPlaylistClick(playlistId: Long) {
        viewModel.addToPlaylist(playlistId)

    }

    private fun showAddedDialog(status: AddingStatus) {
        when (status) {
            is AddingStatus.WasAdded -> {
                dismiss()
                showWasAddedToast(status.playlistName)
            }

            is AddingStatus.AlreadyAdded -> showAlreadyAddedToast(status.playlistName)
        }
    }

    private fun showWasAddedToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.was_added)} $playlistName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showAlreadyAddedToast(playlistName: String) {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.already_added)} $playlistName",
            Toast.LENGTH_LONG
        )
            .show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TRACK_ID_KEY = "TRACK_ID_KEY"

        fun createBundleOf(trackId: String): Bundle {
            return bundleOf(TRACK_ID_KEY to trackId)
        }
    }
}