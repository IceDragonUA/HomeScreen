package com.example.icedr.homescreendemo.widget.managers;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.icedr.homescreendemo.R;

public class HorizontalSmoothScrolledLayoutManager extends LinearLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 20f;

    private Context mContext;

    public HorizontalSmoothScrolledLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state,
                                       int position) {

        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return HorizontalSmoothScrolledLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Override
            public int calculateDxToMakeVisible(View view, int snapPreference) {
                final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
                    return 0;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int left = layoutManager.getDecoratedLeft(view) - params.leftMargin;
                final int right = layoutManager.getDecoratedRight(view) + params.rightMargin;
                final int start = layoutManager.getPaddingLeft() + (int) mContext.getResources().getDimension(R.dimen.child_offset);
                final int end = layoutManager.getWidth() - layoutManager.getPaddingRight() - (int) mContext.getResources().getDimension(R.dimen.child_offset);
                return calculateDtToFit(left, right, start, end, snapPreference);
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
