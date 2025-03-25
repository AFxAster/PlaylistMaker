package com.example.playlistmaker.common.presentation

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val horizontalEdgeSpacing: Int,
    private val verticalEdgeSpacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val rowCount = parent.childCount / spanCount
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        val row = position / spanCount
        when (column) {
            0 -> {
                outRect.left = horizontalEdgeSpacing.toPx(view.resources)
                outRect.right = (horizontalSpacing / 2).toPx(view.resources)
            }

            spanCount - 1 -> {
                outRect.left = (horizontalSpacing / 2).toPx(view.resources)
                outRect.right = horizontalEdgeSpacing.toPx(view.resources)
            }

            else -> {
                outRect.left = (horizontalSpacing / 2).toPx(view.resources)
                outRect.right = (horizontalSpacing / 2).toPx(view.resources)
            }
        }
        when (row) {
            0 -> {
                outRect.top = verticalEdgeSpacing
                outRect.bottom = verticalSpacing / 2
            }

            rowCount - 1 -> {
                outRect.top = verticalSpacing / 2
                outRect.bottom = verticalEdgeSpacing
            }

            else -> {
                outRect.top = verticalSpacing / 2
                outRect.bottom = verticalSpacing / 2
            }
        }
    }
}

private fun Int.toPx(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
    ).toInt()
}