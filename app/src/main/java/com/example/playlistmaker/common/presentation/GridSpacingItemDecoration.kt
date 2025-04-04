package com.example.playlistmaker.common.presentation

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childCount = parent.adapter?.itemCount ?: 0

        val spanSizeLookup = (parent.layoutManager as GridLayoutManager).spanSizeLookup
        val position = parent.getChildAdapterPosition(view)
        val spanSize = spanSizeLookup.getSpanSize(position)

        val rowCount = spanSizeLookup.getSpanGroupIndex(childCount - 1, spanCount) + 1
        val column = spanSizeLookup.getSpanIndex(position, spanCount)
        val row = spanSizeLookup.getSpanGroupIndex(position, spanCount)

        when (row) {
            0 -> {
                outRect.top = 0
                outRect.bottom = (verticalSpacing / 2).toPx(view.resources)
            }

            rowCount - 1 -> {
                outRect.top = (verticalSpacing / 2).toPx(view.resources)
                outRect.bottom = 0
            }

            else -> {
                outRect.top = (verticalSpacing / 2).toPx(view.resources)
                outRect.bottom = (verticalSpacing / 2).toPx(view.resources)
            }
        }

        if (spanSize == spanCount) return

        when (column) {
            0 -> {
                outRect.left = 0
                outRect.right = (horizontalSpacing / 2).toPx(view.resources)
            }

            spanCount - 1 -> {
                outRect.left = (horizontalSpacing / 2).toPx(view.resources)
                outRect.right = 0
            }

            else -> {
                outRect.left = (horizontalSpacing / 2).toPx(view.resources)
                outRect.right = (horizontalSpacing / 2).toPx(view.resources)
            }
        }
    }
}

private fun Int.toPx(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
    ).toInt()
}