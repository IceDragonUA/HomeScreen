package com.example.icedr.homescreendemo.widget.managers;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.icedr.homescreendemo.R;

public class VerticalSmoothScrolledLayoutManager extends LinearLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 50f;

    private Context mContext;

    public VerticalSmoothScrolledLayoutManager(Context context) {
        super(context, LinearLayoutManager.VERTICAL, false);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state,
                                       int position) {

        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return VerticalSmoothScrolledLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Override
            public int calculateDyToMakeVisible(View view, int snapPreference) {
                final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollVertically()) {
                    return 0;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int top = layoutManager.getDecoratedTop(view) - params.topMargin;
                final int bottom = layoutManager.getDecoratedBottom(view) + params.bottomMargin;
                final int start = layoutManager.getPaddingTop() + (int) mContext.getResources().getDimension(R.dimen.parent_offset);
                final int end = layoutManager.getHeight() - layoutManager.getPaddingBottom() - (int) mContext.getResources().getDimension(R.dimen.parent_offset);
                return calculateDtToFit(top, bottom, start, end, snapPreference);
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
