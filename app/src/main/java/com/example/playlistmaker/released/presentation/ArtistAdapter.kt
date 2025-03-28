package com.example.playlistmaker.released.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.playlistmaker.databinding.SelectArtistItemBinding
import com.example.playlistmaker.released.domain.entity.Artist

class ArtistAdapter : BaseAdapter(), Filterable {

    var artists: List<Artist> = emptyList()
    var onArtistClickListener: OnArtistClickListener? = null

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults) {
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = artists.size

    override fun getItem(position: Int): Any = artists[position]

    override fun getItemId(position: Int): Long = artists[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null)
            SelectArtistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        else
            SelectArtistItemBinding.bind(convertView)

        bind(binding, artists[position])
        return binding.root
    }

    override fun getFilter(): Filter {
        return filter
    }

    private fun bind(binding: SelectArtistItemBinding, model: Artist) {
        binding.name.text = model.name

        binding.root.setOnClickListener {
            onArtistClickListener?.onArtistClick(model)
        }
    }

    fun interface OnArtistClickListener {
        fun onArtistClick(artist: Artist)
    }
}