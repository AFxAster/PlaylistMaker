package com.example.playlistmaker.playlistLibrary.presentation

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PlaylistGridItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val column = parent.getChildAdapterPosition(view) % 2
        val spacingBetween =
            (parent.measuredWidth - 2 * 160.toPx(view.resources) - 2 * 16.toPx(view.resources))

        if (column == 0) {
            outRect.left = 16.toPx(view.resources)
            outRect.right = spacingBetween / 2
        } else {
            outRect.left = spacingBetween / 2
            outRect.right = 16.toPx(view.resources)
        }
        outRect.top = 16.toPx(view.resources)
    }
}

private fun Int.toPx(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
    ).toInt()
}