package com.example.playlistmaker.released.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.playlistmaker.R
import com.example.playlistmaker.common.presentation.GridSpacingItemDecoration
import com.example.playlistmaker.databinding.DialogSelectArtistBinding
import com.example.playlistmaker.databinding.FragmentReleasedBinding
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.presentation.state.ArtistState
import com.example.playlistmaker.released.presentation.state.ReleasedState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReleasedFragment : Fragment() {
    private var _binding: FragmentReleasedBinding? = null
    private val binding get() = _binding!!

    private var _dialogBinding: DialogSelectArtistBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    private val dialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmationDialog)
            .setTitle(R.string.add_artist)
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .setOnDismissListener {
                viewModel.getArtistState().removeObservers(viewLifecycleOwner)
                viewModel.clearArtists()
                _dialogBinding = null
            }
    }

    private val adapter = ReleaseAdapter()
    private val itemDecoration = GridSpacingItemDecoration(
        spanCount = 2,
        horizontalSpacing = 8,
        verticalSpacing = 16,
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

            val glm = GridLayoutManager(requireContext(), 2)
            glm.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.isHeader(position)) {
                        true -> 2

                        false -> 1
                    }
                }
            }
            releasedRecyclerView.layoutManager = glm

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.follow_artist -> showAddArtistDialog()
                }
                true
            }
        }

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
        initDialog()

        val dialog = dialogBuilder.setView(dialogBinding.root).show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (requireHaveTier()) {
                val tier = dialogBinding.tierEditText.text.toString().toInt()
                viewModel.followSelectedArtist(tier)
                dialog.dismiss()
            }
        }

        viewModel.getArtistState().observe(viewLifecycleOwner, ::renderArtistDialog)
    }

    private fun initDialog() {
        _dialogBinding = DialogSelectArtistBinding.inflate(layoutInflater)

        with(dialogBinding) {
            clearButton.setOnClickListener {
                autocompleteInput.setText("")
                hideKeyboard(clearButton.windowToken)
            }
            autocompleteInput.setAdapter(artistAdapter)
        }

        with(dialogBinding.autocompleteInput) {
            val textWatcher = doOnTextChanged { text, start, before, count ->
                val searchInput = text?.toString() ?: ""

                dialogBinding.clearButton.isVisible = searchInput.isNotBlank()
                viewModel.debounceRequest(searchInput)
            }

            artistAdapter.onArtistClickListener = ArtistAdapter.OnArtistClickListener {
                removeTextChangedListener(textWatcher)
                setText(it.name)
                addTextChangedListener(textWatcher)
                dismissDropDown()
                clearFocus()
                hideKeyboard(windowToken)

                viewModel.selectedArtist = it
            }
        }
    }

    private fun requireHaveTier(): Boolean {
        with(dialogBinding) {
            val input = tierEditText.text.toString()
            if (input.isBlank()) {
                tierEditText.setHintTextColor(
                    resources.getColor(R.color.YP_red, requireContext().theme)
                )
                return false
            } else {
                return true
            }
        }
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
            autocompleteInput.showDropDown()
            loading.isVisible = false
        }
    }

    private fun renderArtistLoading() {
        with(dialogBinding) {
            loading.isVisible = true
        }
    }

    private fun hideKeyboard(windowToken: IBinder) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            windowToken,
            0
        )
    }
}