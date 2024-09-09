package com.example.playlistmaker.search.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.presentation.AudioPlayerActivity
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.presentation.model.TrackUI
import com.example.playlistmaker.search.presentation.state.SearchState
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: ActivitySearchBinding

    private val searchedTracksAdapter = TracksAdapter()
    private val tracksHistoryAdapter = TracksAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
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

        binding.noInternetStub.setOnInflateListener { _, view ->
            val refreshButton: Button = view.findViewById(R.id.refresh_button)
            refreshButton.setOnClickListener { viewModel.refresh() }
        }

        initRecyclerViews()
        initSearchField()

        viewModel.getState().observe(viewLifecycleOwner, ::render)
    }

    private fun initSearchField() {
        binding.clearSearchFieldButton.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.clearSearchFieldButton.windowToken,
                0
            )
        }
        binding.searchEditText.setText(viewModel.lastQuery)
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val searchInput = text?.toString() ?: ""
            binding.clearSearchFieldButton.isVisible = searchInput.isNotEmpty()
            viewModel.debounceRequest(searchInput)
        }
    }

    private fun initRecyclerViews() {
        searchedTracksAdapter.onTrackClickListener =
            TrackViewHolder.OnTrackClickListener(::onTrackClick)

        binding.searchedTracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchedTracksAdapter
        }

        val clearHistoryAdapter = ClearHistoryAdapter {
            viewModel.clearHistory()
        }

        tracksHistoryAdapter.onTrackClickListener =
            TrackViewHolder.OnTrackClickListener(::onTrackClick)

        binding.tracksHistoryRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter =
                ConcatAdapter(HistoryHeaderAdapter(), tracksHistoryAdapter, clearHistoryAdapter)
        }
    }

    private fun onTrackClick(trackId: String) {
        if (!isClickDebounceAllowed())
            return
        viewModel.addTrackToHistory(trackId)
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerActivity,
            AudioPlayerActivity.createBundleOf(trackId)
        )
    }

    private fun isClickDebounceAllowed(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.Error -> {
                showError()
            }

            is SearchState.History -> {
                showHistory(state.data)
            }

            is SearchState.Content -> {
                showContent(state.data)
            }

            is SearchState.Empty -> {
                showEmpty()
            }
        }
    }

    private fun showLoading() {
        binding.loading.isVisible = true

        binding.tracksHistoryRecyclerView.isVisible = false
        binding.searchedTracksRecyclerView.isVisible = false
        binding.noInternetStub.isVisible = false
        binding.notFoundStub.isVisible = false
    }

    private fun showError() {
        binding.noInternetStub.isVisible = true

        binding.tracksHistoryRecyclerView.isVisible = false
        binding.searchedTracksRecyclerView.isVisible = false
        binding.notFoundStub.isVisible = false
        binding.loading.isVisible = false
    }

    private fun showHistory(tracks: List<TrackUI>) {
        tracksHistoryAdapter.trackList = tracks
        binding.tracksHistoryRecyclerView.isVisible = tracksHistoryAdapter.trackList.isNotEmpty()
        binding.tracksHistoryRecyclerView.scrollToPosition(0)

        binding.searchedTracksRecyclerView.isVisible = false
        binding.noInternetStub.isVisible = false
        binding.notFoundStub.isVisible = false
        binding.loading.isVisible = false
    }

    private fun showContent(tracks: List<TrackUI>) {
        searchedTracksAdapter.trackList = tracks
        binding.searchedTracksRecyclerView.isVisible = true
        binding.searchedTracksRecyclerView.scrollToPosition(0)

        binding.tracksHistoryRecyclerView.isVisible = false
        binding.noInternetStub.isVisible = false
        binding.notFoundStub.isVisible = false
        binding.loading.isVisible = false
    }

    private fun showEmpty() {
        binding.notFoundStub.isVisible = true

        binding.tracksHistoryRecyclerView.isVisible = false
        binding.searchedTracksRecyclerView.isVisible = false
        binding.noInternetStub.isVisible = false
        binding.loading.isVisible = false
    }

    private companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}