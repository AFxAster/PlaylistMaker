package com.example.playlistmaker.released.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.common.presentation.GridSpacingItemDecoration
import com.example.playlistmaker.databinding.DialogSelectArtistBinding
import com.example.playlistmaker.databinding.FragmentReleasedBinding
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.presentation.state.ArtistState
import com.example.playlistmaker.released.presentation.state.ReleasedState
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReleasedFragment : Fragment() {
    private var _binding: FragmentReleasedBinding? = null
    private val binding get() = _binding!!

    private var _dialogBinding: DialogSelectArtistBinding? = null
    private val dialogBinding get() = _dialogBinding!!

    private val adapter = ReleaseAdapter()
    private val itemDecoration = GridSpacingItemDecoration(
        spanCount = 2,
        horizontalSpacing = 8,
        verticalSpacing = 16,
        horizontalEdgeSpacing = 16,
        verticalEdgeSpacing = 0
    )

    private val artistAdapter = ArtistAdapter()

    private val viewModel: ReleasedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReleasedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            releasedRecyclerView.adapter = adapter
            releasedRecyclerView.addItemDecoration(itemDecoration)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.follow_artist -> showAddArtistDialog()
                }
                true
            }
        }

        artistAdapter.artists = listOf(
            Artist(
                id = "1564157271",
                name = "bastiense"
            ),
            Artist(
                id = "1595600259",
                name = "MONRAU"
            ),
        )



        viewModel.getState().observe(viewLifecycleOwner, ::render)
        // TODO ченкуть про соотношение в разметке для элемента
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ReleasedState) {
        when (state) {
            is ReleasedState.Content -> renderContent(state.releases)
            else -> {}
        }
    }

    private fun renderContent(releases: List<Release>) {
        adapter.releases = releases
    }

    private fun showAddArtistDialog() {
        _dialogBinding = DialogSelectArtistBinding.inflate(layoutInflater)
        with(dialogBinding) {
            clearButton.setOnClickListener {
                autocompleteInput.setText("")
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(
                    clearButton.windowToken,
                    0
                )
            }
            autocompleteInput.setAdapter(artistAdapter)
            autocompleteInput.doOnTextChanged { text, start, before, count ->
                val searchInput = text?.toString() ?: ""

                clearButton.isVisible = searchInput.isNotBlank()
                viewModel.debounceRequest(searchInput)

            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle(R.string.add_artist)
            .setPositiveButton(R.string.ok) { dialog, id ->
                TODO()
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
            .setOnDismissListener {
                viewModel.getArtistState().removeObservers(viewLifecycleOwner)
                _dialogBinding = null
            }
            .create()
            .show()

        viewModel.getArtistState().observe(viewLifecycleOwner, ::renderArtistDialog)
    }

    private fun renderArtistDialog(state: ArtistState) {
        when (state) {
            is ArtistState.Content -> renderArtistContent(state.artists)
            is ArtistState.Loading -> renderArtistLoading()
            else -> {}
        }
    }

    private fun renderArtistContent(artists: List<Artist>) {
        artistAdapter.artists = artists
        with(dialogBinding) {
            loading.isVisible = false
        }
    }

    private fun renderArtistLoading() {
        with(dialogBinding) {
            loading.isVisible = true
        }
    }
}