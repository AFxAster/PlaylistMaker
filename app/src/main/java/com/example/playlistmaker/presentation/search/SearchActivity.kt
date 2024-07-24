package com.example.playlistmaker.presentation.search

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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerActivity
import com.example.playlistmaker.presentation.mapper.toTrackPresentation
import com.example.playlistmaker.presentation.model.TrackUI
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchedTracksRecyclerView: RecyclerView
    private lateinit var tracksHistoryRecyclerView: RecyclerView
    private lateinit var notFoundStub: ViewStub
    private lateinit var noInternetStub: ViewStub
    private lateinit var progressBar: ProgressBar
    private lateinit var refreshButton: Button

    private val searchedTracksAdapter = TracksAdapter()
    private val tracksHistoryAdapter = TracksAdapter()

    private var searchInput = SEARCH_INPUT_DEFAULT

    private val handler = Handler(Looper.getMainLooper())
    private val requestRunnable = Runnable {
        getTracksInteractor.getTracks(searchInput, tracksConsumer)
    }
    private var isClickAllowed = true

    private val getTracksInteractor: GetTracksInteractor =
        Creator.provideGetTracksInteractor()
    private val searchHistoryInteractor: SearchHistoryInteractor =
        Creator.provideSearchHistoryInteractor()
    private val tracksConsumer: GetTracksInteractor.TracksConsumer =
        object : GetTracksInteractor.TracksConsumer {
            override fun onTracksSuccess(tracks: List<Track>) {
                runOnUiThread {
                    if (tracks.isNotEmpty())
                        showFoundTracks(tracks.map { track -> track.toTrackPresentation() })
                    else
                        showNotFoundStub()
                }
            }

            override fun onError() {
                runOnUiThread {
                    showNoInternetPlaceholder(onRefresh = {
                        showProgressBar()
                        getTracksInteractor.getTracks(searchInput, consumer = this)
                    })
                }
            }
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
            refreshButton = view.findViewById(R.id.refresh_button)
        }
        progressBar = findViewById(R.id.progress_bar)

        initSearchField()
        initRecyclerViews()

        showHistory()
    }

    private fun initSearchField() {
        val clearSearchFieldButton: ImageView = findViewById(R.id.clear_search_field_button)
        clearSearchFieldButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearSearchFieldButton.windowToken, 0)
            showHistory()
        }

        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.doOnTextChanged { text, _, _, _ ->
            searchInput = text?.toString() ?: ""
            clearSearchFieldButton.isVisible = searchInput.isNotEmpty()
            if (searchInput.isBlank()) {
                showHistory()
            } else {
                showProgressBar()
                debounceRequest()
            }
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
            searchHistoryInteractor.clear()
            tracksHistoryRecyclerView.isVisible = false
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

    private fun showNoInternetPlaceholder(onRefresh: () -> Unit) {
        noInternetStub.isVisible = true
        progressBar.isVisible = false
        searchedTracksRecyclerView.isVisible = false
        tracksHistoryRecyclerView.isVisible = false
        refreshButton.setOnClickListener { onRefresh() }
    }

    private fun showHistory() {
        tracksHistoryAdapter.trackList =
            searchHistoryInteractor.getTracks().map { it.toTrackPresentation() }
        tracksHistoryRecyclerView.isVisible = tracksHistoryAdapter.trackList.isNotEmpty()
        tracksHistoryRecyclerView.scrollToPosition(0)

        searchedTracksRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        notFoundStub.isVisible = false
        progressBar.isVisible = false
    }

    private fun showFoundTracks(trackList: List<TrackUI>) {
        searchedTracksAdapter.trackList = trackList
        searchedTracksRecyclerView.isVisible = true
        searchedTracksRecyclerView.scrollToPosition(0)
        progressBar.isVisible = false
    }

    private fun showNotFoundStub() {
        notFoundStub.isVisible = true
        progressBar.isVisible = false
    }

    private fun showProgressBar() {
        progressBar.isVisible = true
        notFoundStub.isVisible = false
        searchedTracksRecyclerView.isVisible = false
        tracksHistoryRecyclerView.isVisible = false
        noInternetStub.isVisible = false
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

    private fun debounceRequest() {
        handler.removeCallbacks(requestRunnable)
        handler.postDelayed(requestRunnable, REQUEST_DELAY)
    }

    private fun onTrackClick(trackId: String) {
        if (!isClickDebounceAllowed())
            return
        val onTrackDetailsResponse = object : GetTracksInteractor.TrackByIdConsumer {
            override fun onTrackByIdSuccess(tracks: Track) {
                searchHistoryInteractor.addTrack(tracks)
                runOnUiThread {
                    progressBar.isVisible = false
                    if (searchInput.isBlank()) {
                        tracksHistoryRecyclerView.isVisible = true
                    } else {
                        searchedTracksRecyclerView.isVisible = true
                    }
                }

                val audioPlayerIntent =
                    Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                audioPlayerIntent.putExtra(TRACK_KEY, Gson().toJson(tracks))
                startActivity(audioPlayerIntent)
            }

            override fun onError() {
                runOnUiThread {
                    showNoInternetPlaceholder(onRefresh = {
                        showProgressBar()
                        getTracksInteractor.getTrackById(trackId, consumer = this)
                    })
                }
            }
        }

        getTracksInteractor.getTrackById(trackId, onTrackDetailsResponse)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT_KEY, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(SEARCH_INPUT_KEY, SEARCH_INPUT_DEFAULT)
        searchEditText.setText(searchInput)
        searchEditText.setSelection(searchInput.length)
    }

    private companion object {
        const val SEARCH_INPUT_KEY = "SEARCH_REQUEST"
        const val SEARCH_INPUT_DEFAULT = ""
        const val TRACK_KEY = "TRACK_KEY"
        const val REQUEST_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}