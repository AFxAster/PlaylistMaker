package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.ui.AudioPlayerActivity
import com.example.playlistmaker.search.presentation.model.TrackUI
import com.example.playlistmaker.search.presentation.state.SearchState
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var searchedTracksRecyclerView: RecyclerView
    private lateinit var tracksHistoryRecyclerView: RecyclerView
    private lateinit var notFoundStub: ViewStub
    private lateinit var noInternetStub: ViewStub
    private lateinit var loading: ProgressBar

    private val searchedTracksAdapter = TracksAdapter()
    private val tracksHistoryAdapter = TracksAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton: ImageView = findViewById(R.id.back_from_search_button)
        backButton.setOnClickListener {
            finish()
        }

        notFoundStub = findViewById(R.id.not_found_stub)
        noInternetStub = findViewById(R.id.no_internet_stub)
        noInternetStub.setOnInflateListener { _, view ->
            val refreshButton: Button = view.findViewById(R.id.refresh_button)
            refreshButton.setOnClickListener { viewModel.refresh() }
        }
        loading = findViewById(R.id.progress_bar)

        initRecyclerViews()
        initSearchField()

        viewModel.getState().observe(this, ::render)
    }



    private fun initSearchField() {
        val searchEditText: EditText = findViewById(R.id.search_edit_text)
        val clearSearchFieldButton: ImageView = findViewById(R.id.clear_search_field_button)
        clearSearchFieldButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearSearchFieldButton.windowToken, 0)
        }
        searchEditText.setText(viewModel.lastQuery)
        searchEditText.doOnTextChanged { text, _, _, _ ->
            val searchInput = text?.toString() ?: ""
            clearSearchFieldButton.isVisible = searchInput.isNotEmpty()
            viewModel.debounceRequest(searchInput)
        }
    }

    private fun initRecyclerViews() {
        searchedTracksAdapter.onTrackClickListener =
            TrackViewHolder.OnTrackClickListener(::onTrackClick)

        searchedTracksRecyclerView = findViewById(R.id.searched_tracks_recycler_view)
        searchedTracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = searchedTracksAdapter
        }

        val clearHistoryAdapter = ClearHistoryAdapter {
            viewModel.clearHistory()
        }

        tracksHistoryAdapter.onTrackClickListener =
            TrackViewHolder.OnTrackClickListener(::onTrackClick)

        tracksHistoryRecyclerView = findViewById(R.id.tracks_history_recycler_view)
        tracksHistoryRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter =
                ConcatAdapter(HistoryHeaderAdapter(), tracksHistoryAdapter, clearHistoryAdapter)
        }
    }

    private fun onTrackClick(trackId: String) {
        if (!isClickDebounceAllowed())
            return
        viewModel.addTrackToHistory(trackId)
        val audioPlayerIntent =
            Intent(this@SearchActivity, AudioPlayerActivity::class.java)
        audioPlayerIntent.putExtra(TRACK_ID_KEY, trackId)
        startActivity(audioPlayerIntent)
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
        loading.isVisible = true

        tracksHistoryRecyclerView.isVisible = false
        searchedTracksRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        notFoundStub.isVisible = false
    }

    private fun showError() {
        noInternetStub.isVisible = true

        tracksHistoryRecyclerView.isVisible = false
        searchedTracksRecyclerView.isVisible = false
        notFoundStub.isVisible = false
        loading.isVisible = false
    }

    private fun showHistory(tracks: List<TrackUI>) {
        tracksHistoryAdapter.trackList = tracks
        tracksHistoryRecyclerView.isVisible = tracksHistoryAdapter.trackList.isNotEmpty()
        tracksHistoryRecyclerView.scrollToPosition(0)

        searchedTracksRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        notFoundStub.isVisible = false
        loading.isVisible = false
    }

    private fun showContent(tracks: List<TrackUI>) {
        searchedTracksAdapter.trackList = tracks
        searchedTracksRecyclerView.isVisible = true
        searchedTracksRecyclerView.scrollToPosition(0)

        tracksHistoryRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        notFoundStub.isVisible = false
        loading.isVisible = false
    }

    private fun showEmpty() {
        notFoundStub.isVisible = true

        tracksHistoryRecyclerView.isVisible = false
        searchedTracksRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        loading.isVisible = false
    }


    private companion object {
        const val TRACK_ID_KEY = "TRACK_ID_KEY"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}