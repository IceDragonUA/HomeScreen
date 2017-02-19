package com.example.icedr.homescreendemo.widget.categories;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.widget.constants.Motion;
import com.example.icedr.homescreendemo.widget.interfaces.OnItemPressedListener;

import java.util.LinkedList;
import java.util.Queue;

public class RowRecyclerView extends RecyclerView {

    private static final String TAG = RowRecyclerView.class.getCanonicalName();

    private static final int MILLISECONDS_PER_EVENT = 250;

    private static final int VELOCITY_START_POINT = 300;
    private static final float VELOCITY_MAX_SPEED = 0.85f;

    private final int mParentStartEndOffset;

    private int mSelectedPosition = 0;

    private boolean mMotionMode;
    private boolean mMotionDirection;

    private int mDigitalFilter = 0;

    private long mCurrentEventTime;
    private long mLastEventTime;

    private boolean mBlocker;

    private OnItemPressedListener mOnItemPressedListener;

    private boolean isScaled = false;
    private Queue<View> mScaledViews = new LinkedList<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mSelectedPosition + Motion.LEFT >= 0) {
                    mCurrentEventTime = System.currentTimeMillis();
                    if (mCurrentEventTime - mLastEventTime > MILLISECONDS_PER_EVENT) {
                        mLastEventTime = mCurrentEventTime;
                        if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) scaleToSmallSize();
                        if (event.getRepeatCount() == 0 || (getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0 &&
                                getLayoutManager().findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount() - 1)) {
                            smoothScrollToPositionByDirection(Motion.LEFT);
                        } else {
                            mDigitalFilter = getDigitalFilter(mDigitalFilter, Motion.LEFT);
                            fling(Motion.LEFT * mDigitalFilter, 0);
                        }
                        mBlocker = true;
                    } else mBlocker = false;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mSelectedPosition + Motion.RIGHT < getAdapter().getItemCount()) {
                    mCurrentEventTime = System.currentTimeMillis();
                    if (mCurrentEventTime - mLastEventTime > MILLISECONDS_PER_EVENT) {
                        mLastEventTime = mCurrentEventTime;
                        if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) scaleToSmallSize();
                        if (event.getRepeatCount() == 0 || (getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0 &&
                                getLayoutManager().findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount() - 1)) {
                            smoothScrollToPositionByDirection(Motion.RIGHT);
                            if (mSelectedPosition == getAdapter().getItemCount() - 1) setSelectedView(mSelectedPosition);
                        } else {
                            mDigitalFilter = getDigitalFilter(mDigitalFilter, Motion.RIGHT);
                            fling(Motion.RIGHT * mDigitalFilter, 0);
                        }
                        mBlocker = true;
                    } else mBlocker = false;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (getAdapter().getBrowseListPosition() + Motion.UP >= 0) {
                    scaleToSmallSize();
                }
                getAdapter().getBrowseListView().setItemPosition(getAdapter().getBrowseListPosition(), mSelectedPosition);
                getAdapter().getBrowseListView().onKeyDown(keyCode, event);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (getAdapter().getBrowseListPosition() + Motion.DOWN < getAdapter().getBrowseListView().getAdapter().getItemCount()) {
                    scaleToSmallSize();
                }
                getAdapter().getBrowseListView().setItemPosition(getAdapter().getBrowseListPosition(), mSelectedPosition);
                getAdapter().getBrowseListView().onKeyDown(keyCode, event);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mDigitalFilter = 0;
                if (mBlocker) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                        setSelectedView(mSelectedPosition);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mDigitalFilter = 0;
                if (mBlocker) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                        setSelectedView(mSelectedPosition);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (mOnItemPressedListener != null)
                    mOnItemPressedListener.onItemPressed(mSelectedPosition, getAdapter().getBrowseListPosition());
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                getAdapter().getBrowseListView().onKeyUp(keyCode, event);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                getAdapter().getBrowseListView().onKeyUp(keyCode, event);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public RowRecyclerView(Context context) {
        this(context, null);
    }

    public RowRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mParentStartEndOffset = (int) context.getResources().getDimension(R.dimen.child_offset);
        setLayoutManager(new HorizontalSmoothScrolledLayoutManager(context, mParentStartEndOffset));
        addItemDecoration(new ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                super.getItemOffsets(outRect, view, parent, state);

                boolean isFirstChild = parent.getChildAdapterPosition(view) == 0;
                boolean isLastChild = parent.getChildAdapterPosition(view) == state.getItemCount() - 1;

                if (!(isFirstChild || isLastChild)) {
                    return;
                }

                if (isFirstChild) {
                    outRect.left = mParentStartEndOffset;
                } else {
                    outRect.right = mParentStartEndOffset;
                }
            }
        });
        setHasFixedSize(true);
        setAnimation(null);
        setNestedScrollingEnabled(false);
        setChildrenDrawingOrderEnabled(true);
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
                } else scaleToSmallSize();
            }
        });
    }

    @Override
    public HorizontalSmoothScrolledLayoutManager getLayoutManager() {
        return (HorizontalSmoothScrolledLayoutManager) super.getLayoutManager();
    }

    @Override
    public HomeCategoryAdapter getAdapter() {
        return (HomeCategoryAdapter) super.getAdapter();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View view = getSelectedView();
        if (null == view) {
            return super.getChildDrawingOrder(childCount, i);
        }
        int position = indexOfChild(view);

        if (position < 0) {
            return super.getChildDrawingOrder(childCount, i);
        }
        if (i == childCount - 1) {
            return position;
        }
        if (i == position) {
            return childCount - 1;
        }
        return super.getChildDrawingOrder(childCount, i);
    }

    public void setOnItemPressedListener(OnItemPressedListener onItemPressedListener) {
        this.mOnItemPressedListener = onItemPressedListener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    private int getSelectedPosition() {
        if (getAdapter() != null) {
            return mSelectedPosition;
        } else {
            return 0;
        }
    }

    public void setSelectedPosition() {
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

    private View getSelectedView() {
        ViewHolder viewHolder = findViewHolderForAdapterPosition(getSelectedPosition());
        if (viewHolder != null) {
            return viewHolder.itemView;
        } else {
            return null;
        }
    }

    private void setSelectedView(int selectedPosition) {
        View itemView = getLayoutManager().findViewByPosition(selectedPosition);
        if (itemView != null) {
            scaleToBigSize(itemView, true);
            mScaledViews.add(itemView);
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

    private void scaleToBigSize(final View view, final boolean scaleView) {
        if (isScaled == scaleView) return;
        isScaled = scaleView;
        view.setSelected(scaleView);
        view.requestLayout();

        final AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, ViewGroup.SCALE_X, scaleView ? 1f : 1.05f, scaleView ? 1.05f : 1f),
                ObjectAnimator.ofFloat(view, ViewGroup.SCALE_Y, scaleView ? 1f : 1.05f, scaleView ? 1.05f : 1f)
        );
        set.setDuration(120);
        set.setInterpolator(new LinearInterpolator());
        set.start();
    }

    private void scaleToSmallSize() {
        while (mScaledViews.peek() != null) {
            scaleToBigSize(mScaledViews.poll(), false);
        }
    }

    private class HorizontalSmoothScrolledLayoutManager extends LinearLayoutManager {

        private final Context mContext;
        private final int mParentStartEndOffset;

        HorizontalSmoothScrolledLayoutManager(Context context, int parentOffset) {
            super(context, LinearLayoutManager.HORIZONTAL, false);
            this.mContext = context;
            this.mParentStartEndOffset = parentOffset;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state,
                                           int position) {

            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

                @Override
                public int calculateDxToMakeVisible(View view, int snapPreference) {
                    final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
                        return 0;
                    }
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                    final int left = layoutManager.getDecoratedLeft(view) - params.leftMargin;
                    final int right = layoutManager.getDecoratedRight(view) + params.rightMargin;
                    final int start = layoutManager.getPaddingLeft() + mParentStartEndOffset;
                    final int end = layoutManager.getWidth() - layoutManager.getPaddingRight() - mParentStartEndOffset;
                    return calculateDtToFit(left, right, start, end, snapPreference);
                }
            };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

}
