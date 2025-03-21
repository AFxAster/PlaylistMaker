package com.example.playlistmaker.favourite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.presentation.AudioPlayerActivity
import com.example.playlistmaker.common.presentation.TracksAdapter
import com.example.playlistmaker.common.utils.debounceWithFirstCall
import com.example.playlistmaker.databinding.FragmentFavouriteBinding
import com.example.playlistmaker.favourite.presentation.state.FavouriteState
import com.example.playlistmaker.favourite.presentation.viewmodel.FavouriteViewModel
import com.example.playlistmaker.search.presentation.TrackViewHolder
import com.example.playlistmaker.search.presentation.mapper.toTrackUI
import com.example.playlistmaker.search.presentation.model.TrackUI
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val tracksAdapter = TracksAdapter()
    private val viewModel: FavouriteViewModel by viewModel()

    private val debounceClick = debounceWithFirstCall<String>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        action = ::onTrackClick
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = tracksAdapter
        }

        tracksAdapter.onTrackClickListener =
            TrackViewHolder.OnTrackClickListener(debounceClick)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavouriteState) {
        when (state) {
            is FavouriteState.Empty -> {
                showEmpty()
            }

            is FavouriteState.Content -> {
                showContent(state.data.map { it.toTrackUI() })
            }
        }
    }

    private fun showContent(trackList: List<TrackUI>) {
        tracksAdapter.trackList = trackList

        binding.tracksRecyclerView.isVisible = true

        binding.emptyViewStub.isVisible = false
    }

    private fun showEmpty() {
        binding.emptyViewStub.isVisible = true

        binding.tracksRecyclerView.isVisible = false
    }

    private fun onTrackClick(trackId: String) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_audioPlayerActivity,
            AudioPlayerActivity.createBundleOf(trackId)
        )
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance(): FavouriteFragment {
            return FavouriteFragment()
        }
    }
}