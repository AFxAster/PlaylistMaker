package com.example.playlistmaker.released.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.common.presentation.GridSpacingItemDecoration
import com.example.playlistmaker.databinding.FragmentReleasedBinding
import com.example.playlistmaker.released.domain.entity.Release
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReleasedFragment : Fragment() {
    private var _binding: FragmentReleasedBinding? = null
    private val binding get() = _binding!!

    private val adapter = ReleaseAdapter()
    private val itemDecoration = GridSpacingItemDecoration(
        spanCount = 2,
        horizontalSpacing = 8,
        verticalSpacing = 16,
        horizontalEdgeSpacing = 16,
        verticalEdgeSpacing = 0
    )

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
}