package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class HistoryHeaderAdapter : RecyclerView.Adapter<HistoryHeaderAdapter.HistoryHeaderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHeaderHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_history_header, parent, false)
        return HistoryHeaderHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryHeaderHolder, position: Int) {}

    override fun getItemCount() = 1

    class HistoryHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
