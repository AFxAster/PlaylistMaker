package com.example.playlistmaker.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class ClearHistoryAdapter(private val onClearListener: OnClearListener) :
    RecyclerView.Adapter<ClearHistoryAdapter.ClearHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClearHistoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.clear_search_history_button, parent, false)
        return ClearHistoryHolder(view)
    }

    override fun onBindViewHolder(holder: ClearHistoryHolder, position: Int) {
        holder.clearButton.setOnClickListener {
            onClearListener.onClear()
        }
    }

    override fun getItemCount() = 1

    class ClearHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clearButton: Button = itemView.findViewById(R.id.clear_history_button)
    }

    fun interface OnClearListener {
        fun onClear()
    }
}