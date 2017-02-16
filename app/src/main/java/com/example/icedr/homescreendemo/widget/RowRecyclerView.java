package com.example.icedr.homescreendemo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.widget.categories.HomeCategoryAdapter;
import com.example.icedr.homescreendemo.widget.constants.Motion;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IceDr on 14.02.2017.
 */

public class RowRecyclerView extends RecyclerView {

    private static final String TAG = RowRecyclerView.class.getCanonicalName();

    private static final int DELAY_ACTION_TIME = 20;

    private final int mParentStartEndOffset;

    private int mSelectedPosition = 0;

    private OnItemPressedListener mOnItemPressedListener;

    private boolean isScaled = false;
    private Queue<View> mScaledViews = new LinkedList<>();

    private OnScrollListener onScrollListener = new OnScrollListener() {
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
            } else scaleToSmallSize();
        }
    };

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            for (View scaledView : mScaledViews) {
                scaledView.setSelected(hasFocus);
            }
        }
    };

    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                        setSelectedPosition();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                        setSelectedPosition();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (mOnItemPressedListener != null)
                        mOnItemPressedListener.onItemPressed(mSelectedPosition);
                    return true;
                }
                return false;
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (mSelectedPosition + Motion.LEFT >= 0) {
                        if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) scaleToSmallSize();
                        scrollByDirection(Motion.LEFT);
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (mSelectedPosition + Motion.RIGHT < getAdapter().getItemCount()) {
                        if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE) scaleToSmallSize();
                        scrollByDirection(Motion.RIGHT);
                    }
                    return true;
                }
            }
            return false;
        }
    };

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
        setAdapter(new HomeCategoryAdapter(context, this));
        setOnKeyListener(onKeyListener);
        addOnScrollListener(onScrollListener);
        setOnFocusChangeListener(onFocusChangeListener);
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

    public void setOnItemPressedListener(OnItemPressedListener mOnItemPressedListener) {
        this.mOnItemPressedListener = mOnItemPressedListener;
    }

    public int getSelectedPosition() {
        if (getAdapter() != null) {
            return mSelectedPosition;
        } else {
            return 0;
        }
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

    public View getSelectedView() {
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

    public void scrollByDirection(int direction) {
        if (mSelectedPosition + direction < 0 || mSelectedPosition + direction > getAdapter().getItemCount() - 1) {
            return;
        }
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
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        set.start();
    }

    private void scaleToSmallSize() {
        while (mScaledViews.peek() != null) {
            scaleToBigSize(mScaledViews.poll(), false);
        }
    }

    private class HorizontalSmoothScrolledLayoutManager extends LinearLayoutManager {

        private static final float MILLISECONDS_PER_INCH = 20f;

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
                    final int start = layoutManager.getPaddingLeft() + mParentStartEndOffset;
                    final int end = layoutManager.getWidth() - layoutManager.getPaddingRight() - mParentStartEndOffset;
                    return calculateDtToFit(left, right, start, end, snapPreference);
                }
            };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

    public interface OnItemPressedListener {

        void onItemPressed(int position);

    }

}
