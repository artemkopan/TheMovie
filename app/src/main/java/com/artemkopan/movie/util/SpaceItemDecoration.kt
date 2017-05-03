package com.artemkopan.movie.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View


class SpaceItemDecoration(
        val spacing: Int,
        val isHorizontal: Boolean,
        val addLast: Boolean = false,
        val addFirst: Boolean = false) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: State?) {
        if (parent?.getChildAdapterPosition(view) == 0 && addFirst) {
            if (isHorizontal) {
                outRect?.left = spacing
            } else {
                outRect?.top = spacing
            }
        }

        if (!addLast) {
            if (parent?.getChildAdapterPosition(view) != parent?.adapter?.itemCount?.minus(1)) {
                if (isHorizontal) {
                    outRect?.right = spacing
                } else {
                    outRect?.bottom = spacing
                }
            }

        } else {
            if (isHorizontal) {
                outRect?.right = spacing
            } else {
                outRect?.bottom = spacing
            }
        }
    }

}