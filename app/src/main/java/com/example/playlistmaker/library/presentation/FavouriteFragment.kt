package com.example.playlistmaker.library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouriteBinding
import com.example.playlistmaker.library.presentation.state.FavouriteState
import com.example.playlistmaker.library.presentation.viewmodel.FavouriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavouriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
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
        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavouriteState) {
        when (state) {
            is FavouriteState.Empty -> {
                showEmpty()
            }

            is FavouriteState.Content -> {}
        }
    }

    private fun showEmpty() {
        binding.emptyViewStub.isVisible = true
    }

    companion object {
        fun newInstance(): FavouriteFragment {
            return FavouriteFragment()
        }
    }
}