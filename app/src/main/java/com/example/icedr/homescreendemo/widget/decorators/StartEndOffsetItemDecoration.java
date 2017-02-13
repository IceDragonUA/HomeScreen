package com.example.icedr.homescreendemo.widget.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StartEndOffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int mStartEndOffsetPx;

    /**
     * Constructor that takes in the size of the offset to be added to the start
     * and end of the RecyclerView.
     *
     * @param startEndOffsetPx The size of the offset to be added to the start and end
     *                         of the RecyclerView in pixels
     */
    public StartEndOffsetItemDecoration(int startEndOffsetPx) {
        mStartEndOffsetPx = startEndOffsetPx;
    }

    /**
     * Determines the size and location of the offset to be added to the start
     * and end of the RecyclerView.
     *
     * @param outRect The {@link Rect} of offsets to be added around the child view
     * @param view    The child view to be decorated with an offset
     * @param parent  The RecyclerView onto which dividers are being added
     * @param state   The current RecyclerView.State of the RecyclerView
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        boolean isFirstChild = parent.getChildAdapterPosition(view) == 0;
        boolean isLastChild = parent.getChildAdapterPosition(view) == state.getItemCount() - 1;

        if (!(isFirstChild || isLastChild)) {
            return;
        }

        if (isFirstChild) {
            outRect.left = mStartEndOffsetPx;
        } else {
            outRect.right = mStartEndOffsetPx;
        }
    }
}
