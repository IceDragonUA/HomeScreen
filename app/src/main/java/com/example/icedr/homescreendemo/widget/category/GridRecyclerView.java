package com.example.icedr.homescreendemo.widget.category;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.widget.constants.Motion;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerView extends RecyclerView {

    private static final String TAG = GridRecyclerView.class.getCanonicalName();

    private int mSelectedPosition = 0;

    private ArrayList<Integer> mCoordinator = new ArrayList<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mSelectedPosition + Motion.UP >= 0) {
                    if (event.getRepeatCount() == 0){
                        smoothScrollToPositionByDirection(Motion.UP);
                    } else {
                        // TODO: 18.02.2017 implement logic of scrolling
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mSelectedPosition + Motion.DOWN < getAdapter().getItemCount()) {
                    if (event.getRepeatCount() == 0){
                        smoothScrollToPositionByDirection(Motion.DOWN);
                    } else {
                        // TODO: 18.02.2017 implement logic of scrolling
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) setSelectedPosition();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) setSelectedPosition();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new GridRecyclerView.VerticalSmoothScrolledLayoutManager(context, (int) context.getResources().getDimension(R.dimen.parent_offset)));
        setHasFixedSize(true);
        setAnimation(null);
        setNestedScrollingEnabled(false);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    postOnAnimationDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSelectedPosition();
                        }
                    }, 10);
                }
            }
        });
    }

    @Override
    public GridRecyclerView.VerticalSmoothScrolledLayoutManager getLayoutManager() {
        return (GridRecyclerView.VerticalSmoothScrolledLayoutManager) super.getLayoutManager();
    }

    @Override
    public HomeCategoriesAdapter getAdapter() {
        return (HomeCategoriesAdapter) super.getAdapter();
    }

    private void setSelectedPosition() {
        int itemPosition = 0;
        int minimaxCriterion = Integer.MAX_VALUE;

        for (int position = getLayoutManager().findFirstCompletelyVisibleItemPosition(); position <= getLayoutManager().findLastCompletelyVisibleItemPosition(); position++) {
            int differencePositions = Math.abs(position - mSelectedPosition);
            if (differencePositions < minimaxCriterion) {
                minimaxCriterion = differencePositions;
                itemPosition = position;
            }
        }

        if (minimaxCriterion != 0) {
            if (itemPosition == getLayoutManager().findFirstCompletelyVisibleItemPosition()) {
                setSelectedView(getLayoutManager().findFirstCompletelyVisibleItemPosition());
            } else if (itemPosition == getLayoutManager().findLastCompletelyVisibleItemPosition()) {
                setSelectedView(getLayoutManager().findLastCompletelyVisibleItemPosition());
            }
        } else {
            setSelectedView(mSelectedPosition);
        }
    }

    private void setSelectedView(int selectedPosition) {
        final HomeCategoriesAdapter.ViewHolder viewHolder = (HomeCategoriesAdapter.ViewHolder) findViewHolderForAdapterPosition(selectedPosition);
        if (viewHolder != null) {
            viewHolder.itemView.requestFocus();
            viewHolder.categoryListView.setSelectedPosition();
        }
    }

    @Deprecated
    private void smoothScrollToPositionByDirection(int direction) {
        if (mSelectedPosition + direction < 0 || mSelectedPosition + direction > getAdapter().getItemCount() - 1) {
            return;
        }
        mSelectedPosition = mSelectedPosition + direction;
        smoothScrollToPosition(mSelectedPosition);
    }

    public void createCoordinator(List<Project> projectList) {
        for (int i = 0; i < projectList.size(); i++){
            mCoordinator.add(0);
        }
    }
    public int getItemPosition (int rowPosition) {
        return mCoordinator.get(rowPosition);
    }

    public void setItemPosition (int rowPosition, int itemPosition){
        mCoordinator.set(rowPosition, itemPosition);
    }

    @Deprecated
    private class VerticalSmoothScrolledLayoutManager extends LinearLayoutManager {

        private static final float MILLISECONDS_PER_INCH = 50f;

        private final Context mContext;
        private final int mParentStartEndOffset;

        VerticalSmoothScrolledLayoutManager(Context context, int parentOffset) {
            super(context, LinearLayoutManager.VERTICAL, false);
            this.mContext = context;
            this.mParentStartEndOffset = parentOffset;
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
                    final int start = layoutManager.getPaddingTop() + mParentStartEndOffset;
                    final int end = layoutManager.getHeight() - layoutManager.getPaddingBottom() - mParentStartEndOffset;
                    return calculateDtToFit(top, bottom, start, end, snapPreference);
                }
            };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

}
