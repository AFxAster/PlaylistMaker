package com.example.playlistmaker.released.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.playlistmaker.databinding.SelectArtistItemBinding
import com.example.playlistmaker.released.domain.entity.Artist

class TestArtistAdapter : BaseAdapter(), Filterable {
    var artists: List<Artist> = listOf()

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            return FilterResults().apply {
                values = artists
//                count = artists.size
            }
        }

        override fun publishResults(p0: CharSequence, p1: FilterResults) {
//            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun getCount(): Int = artists.size

    override fun getItem(position: Int): Any = artists[position]

    override fun getItemId(position: Int): Long = artists[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SelectArtistItemBinding.inflate(layoutInflater)
        binding.name.text = artists[position].name
        return binding.root
    }


}