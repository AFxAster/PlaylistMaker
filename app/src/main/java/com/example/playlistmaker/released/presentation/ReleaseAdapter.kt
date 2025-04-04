package com.example.playlistmaker.released.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.released.domain.entity.Release

class ReleaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var concatAdapter = ConcatAdapter().apply {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                this@ReleaseAdapter.notifyDataSetChanged()
                super.onChanged()
            }
        })
    }

    var releases: List<Release> = emptyList()
        set(value) {
            val groupedReleases = value.groupBy { it.tier }
            val adapters = concatAdapter.adapters
            groupedReleases.forEach { (tier, tierReleases) ->
                val indexOfPresent =
                    adapters.indexOfFirst { (it as TierReleaseAdapter).tier == tier }
                if (indexOfPresent == -1) {
                    val tierReleaseAdapter =
                        TierReleaseAdapter(tier)
                    concatAdapter.addAdapter(tierReleaseAdapter)
                    tierReleaseAdapter.releases = tierReleases
                } else {
                    (adapters[indexOfPresent] as TierReleaseAdapter).releases = releases
                }
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        concatAdapter.onCreateViewHolder(parent, viewType)

    override fun getItemCount(): Int = concatAdapter.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        concatAdapter.onBindViewHolder(holder, position)

    override fun getItemViewType(position: Int): Int = concatAdapter.getItemViewType(position)

    fun isHeader(position: Int): Boolean {
        var cursor = 0
        concatAdapter.adapters.forEach { adapter ->
            if (position == cursor) return true
            cursor += adapter.itemCount
        }
        return false
    }

    companion object {
        const val TIER_HEADER_TYPE = 0
        const val RELEASE_TYPE = 1
    }
}

