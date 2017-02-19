package com.example.icedr.homescreendemo.widget.category;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.widget.constants.Motion;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerView extends RecyclerView {

    private static final String TAG = GridRecyclerView.class.getCanonicalName();

    private static final int MILLISECONDS_PER_EVENT = 150;

    private static final int VELOCITY_START_POINT = 300;
    private static final float VELOCITY_MAX_SPEED = 0.85f;

    private int mSelectedPosition = 0;

    private boolean mMotionMode;
    private boolean mMotionDirection;

    private int mDigitalFilter = 0;

    private long mCurrentEventTime;
    private long mLastEventTime;

    private boolean mBlocker;

    private ArrayList<Integer> mCoordinator = new ArrayList<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mSelectedPosition + Motion.UP >= 0) {
                    mCurrentEventTime = System.currentTimeMillis();
                    if (mCurrentEventTime - mLastEventTime > MILLISECONDS_PER_EVENT) {
                        mLastEventTime = mCurrentEventTime;
                        if (event.getRepeatCount() == 0 || (getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0 &&
                                getLayoutManager().findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount() - 1)) {
                            smoothScrollToPositionByDirection(Motion.UP);
                        } else {
                            mDigitalFilter = getDigitalFilter(mDigitalFilter, Motion.UP);
                            fling(0, Motion.UP * mDigitalFilter);
                        }
                        mBlocker = true;
                    } else mBlocker = false;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mSelectedPosition + Motion.DOWN < getAdapter().getItemCount()) {
                    mCurrentEventTime = System.currentTimeMillis();
                    if (mCurrentEventTime - mLastEventTime > MILLISECONDS_PER_EVENT) {
                        mLastEventTime = mCurrentEventTime;
                        if (event.getRepeatCount() == 0 || (getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0 &&
                                getLayoutManager().findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount() - 1)) {
                            smoothScrollToPositionByDirection(Motion.DOWN);
                            if (mSelectedPosition == getAdapter().getItemCount() - 1) setSelectedView(mSelectedPosition);
                        } else {
                            mDigitalFilter = getDigitalFilter(mDigitalFilter, Motion.DOWN);
                            fling(0, Motion.DOWN * mDigitalFilter);
                        }
                        mBlocker = true;
                    } else mBlocker = false;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                mDigitalFilter = 0;
                if (mBlocker) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) setSelectedView(mSelectedPosition);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                mDigitalFilter = 0;
                if (mBlocker) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) setSelectedView(mSelectedPosition);
                }
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (mDigitalFilter == 0 ||
                        getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0 ||
                        getLayoutManager().findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount() - 1)) {
                    postOnAnimationDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSelectedPosition = mMotionDirection ?
                                    (mMotionMode ? getLayoutManager().findFirstVisibleItemPosition() : getLayoutManager().findFirstCompletelyVisibleItemPosition()) :
                                    (mMotionMode ? getLayoutManager().findLastVisibleItemPosition() : getLayoutManager().findLastCompletelyVisibleItemPosition());

                            if (mMotionMode) {
                                mMotionMode = false;
                                smoothScrollToPosition(mSelectedPosition);
                            } else {
                                setSelectedView(mSelectedPosition);
                            }
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

    private void setSelectedView(int selectedPosition) {
        final HomeCategoriesAdapter.ViewHolder viewHolder = (HomeCategoriesAdapter.ViewHolder) findViewHolderForAdapterPosition(selectedPosition);
        if (viewHolder != null) {
            viewHolder.itemView.requestFocus();
            viewHolder.categoryListView.setSelectedPosition();
        }
    }

    private int getDigitalFilter(int pastDigitalFilter, int direction) {
        mMotionMode = true;
        mMotionDirection = direction == Motion.LEFT;
        return (int) Math.floor(VELOCITY_START_POINT + VELOCITY_MAX_SPEED * pastDigitalFilter);
    }

    private void smoothScrollToPositionByDirection(int direction) {
        if (mSelectedPosition + direction < 0 || mSelectedPosition + direction > getAdapter().getItemCount() - 1) {
            return;
        }
        mMotionMode = false;
        mMotionDirection = direction == Motion.LEFT;
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

    private class VerticalSmoothScrolledLayoutManager extends LinearLayoutManager {

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
