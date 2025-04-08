package com.example.playlistmaker.released.presentation

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ReleasedGridItemBinding
import com.example.playlistmaker.databinding.TierHeaderBinding
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.entity.ReleaseType

class TierReleaseAdapter(
    val tier: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var releases: List<Release> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var isCollapsed = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ReleaseAdapter.TIER_HEADER_TYPE -> TierHeaderViewHolder(
                TierHeaderBinding.inflate(layoutInflater, parent, false)
            )

            ReleaseAdapter.RELEASE_TYPE -> ReleaseViewHolder(
                ReleasedGridItemBinding.inflate(layoutInflater, parent, false)
            )

            else -> error("UNKNOWN TYPE")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> (holder as TierHeaderViewHolder).bind(tier, isCollapsed) { toggleExpandButton() }
            else -> (holder as ReleaseViewHolder).bind(releases[position - 1])
        }
    }

    override fun getItemCount(): Int = if (isCollapsed) 1 else releases.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ReleaseAdapter.TIER_HEADER_TYPE
            else -> ReleaseAdapter.RELEASE_TYPE
        }
    }

    private fun toggleExpandButton() {
        isCollapsed = !isCollapsed
        notifyDataSetChanged()
    }

    class ReleaseViewHolder(
        private val binding: ReleasedGridItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Release) {
            with(binding) {
                releaseName.text = model.releaseName
                artistName.text = model.artistName
                type.text = getStringType(model.type, binding.root.resources)

                Glide.with(itemView)
                    .load(model.artworkUrl512)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(artwork)
            }
        }

        private fun getStringType(type: ReleaseType, resources: Resources): String {
            return when (type) {
                ReleaseType.Single -> resources.getString(R.string.single)
                ReleaseType.Album -> resources.getString(R.string.album)
            }
        }
    }

    class TierHeaderViewHolder(
        private val binding: TierHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tier: Int, isCollapsed: Boolean, onClick: (Int) -> Unit) {
            with(binding) {
                val tierString = root.resources.getString(R.string.tier)
                tierText.text = "$tierString $tier"
                expandButton.rotation = if (isCollapsed) 0f else 90f
                root.setOnClickListener {
                    onClick(tier)
                }
            }
        }
    }
}

