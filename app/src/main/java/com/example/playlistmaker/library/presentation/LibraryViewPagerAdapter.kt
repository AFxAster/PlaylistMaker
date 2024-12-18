package com.example.playlistmaker.library.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.favourite.presentation.FavouriteFragment
import com.example.playlistmaker.playlistLibrary.presentation.PlaylistLibraryFragment

class LibraryViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteFragment.newInstance()
            else -> PlaylistLibraryFragment.newInstance()
        }
    }
}