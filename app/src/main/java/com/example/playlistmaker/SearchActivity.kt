package com.example.playlistmaker

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var notFoundStub: ViewStub
    private lateinit var noInternetStub: ViewStub

    private var searchInput = SEARCH_INPUT_DEFAULT
    private var lastQuery = ""
    private val tracksAdapter = TracksAdapter()
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

        val clearButton: ImageView = findViewById(R.id.clear_search_field_button)
        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            tracksAdapter.trackList = emptyList()
            notFoundStub.visibility = View.GONE
            noInternetStub.visibility = View.GONE
            lastQuery = ""
        }

        searchEditText = findViewById(R.id.search_edit_text)
        val searchFieldTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable) {
                searchInput = s.toString()
            }
        }
        searchEditText.addTextChangedListener(searchFieldTextWatcher)

        notFoundStub = findViewById(R.id.not_found_stub)
        noInternetStub = findViewById(R.id.no_internet_stub)
        noInternetStub.setOnInflateListener { _, view ->
            val refreshButton: Button = view.findViewById(R.id.refresh_button)
            refreshButton.setOnClickListener {
                request(lastQuery)
            }
        }
        tracksRecyclerView = findViewById(R.id.track_list_recycler_view)
        tracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = tracksAdapter
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request(searchInput)
                true
            } else
                false
        }
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
                        tracksAdapter.trackList = response.body()!!.results
                        notFoundStub.visibility = View.GONE
                        noInternetStub.visibility = View.GONE
                        tracksRecyclerView.scrollToPosition(0)
                    } else {
                        tracksAdapter.trackList = emptyList()
                        notFoundStub.visibility = View.VISIBLE
                        noInternetStub.visibility = View.GONE
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

    private fun showNoInternetPlaceholder() {
        tracksAdapter.trackList = emptyList()
        noInternetStub.visibility = View.VISIBLE
        notFoundStub.visibility = View.GONE
    }

    private companion object {
        const val SEARCH_INPUT_KEY = "SEARCH_REQUEST"
        const val LAST_QUERY_KEY = "LAST_QUERY"
        const val SEARCH_INPUT_DEFAULT = ""
        const val BASE_URL = "https://itunes.apple.com"
    }
}