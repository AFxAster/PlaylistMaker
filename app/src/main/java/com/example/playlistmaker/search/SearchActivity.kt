package com.example.playlistmaker.search

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
import com.example.playlistmaker.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.TrackViewHolder
import com.example.playlistmaker.TracksAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchedTracksRecyclerView: RecyclerView
    private lateinit var tracksHistoryRecyclerView: RecyclerView
    private lateinit var notFoundStub: ViewStub
    private lateinit var noInternetStub: ViewStub
    private lateinit var progressBar: ProgressBar

    private lateinit var searchHistory: SearchHistory
    private val searchedTracksAdapter = TracksAdapter()
    private val tracksHistoryAdapter = TracksAdapter()

    private var searchInput = SEARCH_INPUT_DEFAULT

    private val iTunesService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private val requestRunnable = Runnable { request(searchInput) }
    private var isClickAllowed = true

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
            refreshButton.setOnClickListener {
                request(searchInput)
            }
        }
        progressBar = findViewById(R.id.progress_bar)

        initSearchField()
        initRecyclerViews()

        showHistory()
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
        searchHistory = SearchHistory(getSharedPreferences(SEARCH_HISTORY_FILE, MODE_PRIVATE))

        val onTrackClickListener: TrackViewHolder.OnTrackClickListener =
            TrackViewHolder.OnTrackClickListener {
                if (!isClickDebounceAllowed())
                    return@OnTrackClickListener

                val audioPlayerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                audioPlayerIntent.putExtra(TRACK_KEY, Gson().toJson(it))
                startActivity(audioPlayerIntent)
                searchHistory.add(it)
            }

        searchedTracksAdapter.onTrackClickListener = onTrackClickListener
        searchedTracksRecyclerView = findViewById(R.id.searched_tracks_recycler_view)
        searchedTracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = searchedTracksAdapter
        }

        val clearHistoryAdapter = ClearHistoryAdapter {
            searchHistory.clear()
            tracksHistoryRecyclerView.isVisible = false
        }

        tracksHistoryAdapter.onTrackClickListener = onTrackClickListener
        tracksHistoryRecyclerView = findViewById(R.id.tracks_history_recycler_view)
        tracksHistoryRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter =
                ConcatAdapter(HistoryHeaderAdapter(), tracksHistoryAdapter, clearHistoryAdapter)
        }
    }

    private fun showNoInternetPlaceholder() {
        noInternetStub.isVisible = true
        progressBar.isVisible = false
    }

    private fun showHistory() {
        tracksHistoryAdapter.trackList = searchHistory.getHistoryList()
        tracksHistoryRecyclerView.isVisible = tracksHistoryAdapter.trackList.isNotEmpty()
        tracksHistoryRecyclerView.scrollToPosition(0)

        searchedTracksRecyclerView.isVisible = false
        noInternetStub.isVisible = false
        notFoundStub.isVisible = false
        progressBar.isVisible = false
    }

    private fun showFoundTracks(trackList: List<Track>) {
        searchedTracksRecyclerView.isVisible = true
        progressBar.isVisible = false
        searchedTracksAdapter.trackList = trackList
        searchedTracksRecyclerView.scrollToPosition(0)
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

    private fun request(query: String) {
        if (query.isBlank()) return
        showProgressBar()
        iTunesService.getTracks(query).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        showFoundTracks(response.body()!!.results)
                    } else {
                        showNotFoundStub()
                    }
                } else {
                    showNoInternetPlaceholder()
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                showNoInternetPlaceholder()
            }
        })
    }

    private companion object {
        const val SEARCH_INPUT_KEY = "SEARCH_REQUEST"
        const val SEARCH_INPUT_DEFAULT = ""
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_FILE = "SEARCH_HISTORY_FILE"
        const val TRACK_KEY = "TRACK_KEY"
        const val REQUEST_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}