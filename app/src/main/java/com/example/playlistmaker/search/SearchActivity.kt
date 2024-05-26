package com.example.playlistmaker.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.TracksAdapter
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

    private lateinit var searchHistory: SearchHistory
    private val searchedTracksAdapter = TracksAdapter()
    private val tracksHistoryAdapter = TracksAdapter()

    private var searchInput = SEARCH_INPUT_DEFAULT
    private var lastQuery = ""
    private val SEARCH_HISTORY_FILE = "SEARCH_HISTORY_FILE"
    private val iTunesService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)

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
                request(lastQuery)
            }
        }

        initSearchField()
        initRecyclerViews()

        showHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT_KEY, searchInput)
        outState.putString(LAST_QUERY_KEY, lastQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(SEARCH_INPUT_KEY, SEARCH_INPUT_DEFAULT)
        searchEditText.setText(searchInput)
        searchEditText.setSelection(searchInput.length)
        lastQuery = savedInstanceState.getString(LAST_QUERY_KEY, SEARCH_INPUT_DEFAULT)
        request(lastQuery)
    }

    private fun initSearchField() {
        val clearSearchFieldButton: ImageView = findViewById(R.id.clear_search_field_button)
        clearSearchFieldButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearSearchFieldButton.windowToken, 0)
            lastQuery = ""
            showHistory()
        }

        searchEditText = findViewById(R.id.search_edit_text)
        val searchFieldTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                clearSearchFieldButton.visibility = if (s.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable) {
                searchInput = s.toString()
            }
        }
        searchEditText.addTextChangedListener(searchFieldTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request(searchInput)
                true
            } else
                false
        }
    }

    private fun initRecyclerViews() {
        searchHistory = SearchHistory(getSharedPreferences(SEARCH_HISTORY_FILE, MODE_PRIVATE))

        searchedTracksAdapter.onTrackClick = { searchHistory.add(it) }
        searchedTracksRecyclerView = findViewById(R.id.searched_tracks_recycler_view)
        searchedTracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = searchedTracksAdapter
        }

        val clearHistoryAdapter = ClearHistoryAdapter {
            searchHistory.clear()
            tracksHistoryRecyclerView.visibility = View.GONE
        }

        tracksHistoryAdapter.onTrackClick = {
            searchHistory.add(it)
            trackList = searchHistory.getHistoryList()
        }
        tracksHistoryRecyclerView = findViewById(R.id.tracks_history_recycler_view)
        tracksHistoryRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter =
                ConcatAdapter(HistoryHeaderAdapter(), tracksHistoryAdapter, clearHistoryAdapter)
        }
    }

    private fun showNoInternetPlaceholder() {
        noInternetStub.visibility = View.VISIBLE
        searchedTracksRecyclerView.visibility = View.GONE
        tracksHistoryRecyclerView.visibility = View.GONE
        notFoundStub.visibility = View.GONE
    }

    private fun showHistory() {
        tracksHistoryAdapter.trackList = searchHistory.getHistoryList()
        if (tracksHistoryAdapter.trackList.isEmpty()) {
            tracksHistoryRecyclerView.visibility = View.GONE
        } else {
            tracksHistoryRecyclerView.visibility = View.VISIBLE
        }
        tracksHistoryRecyclerView.scrollToPosition(0)
        searchedTracksRecyclerView.visibility = View.GONE
        noInternetStub.visibility = View.GONE
        notFoundStub.visibility = View.GONE
    }

    private fun showFoundTracks(trackList: List<Track>) {
        searchedTracksRecyclerView.visibility = View.VISIBLE
        searchedTracksAdapter.trackList = trackList
        tracksHistoryRecyclerView.visibility = View.GONE
        notFoundStub.visibility = View.GONE
        noInternetStub.visibility = View.GONE
        searchedTracksRecyclerView.scrollToPosition(0)
    }

    private fun showNotFoundStub() {
        notFoundStub.visibility = View.VISIBLE
        searchedTracksRecyclerView.visibility = View.GONE
        tracksHistoryRecyclerView.visibility = View.GONE
        noInternetStub.visibility = View.GONE
    }

    private fun request(query: String) {
        if (query.isBlank()) return
        lastQuery = query
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
        const val LAST_QUERY_KEY = "LAST_QUERY"
        const val SEARCH_INPUT_DEFAULT = ""
        const val BASE_URL = "https://itunes.apple.com"
    }
}