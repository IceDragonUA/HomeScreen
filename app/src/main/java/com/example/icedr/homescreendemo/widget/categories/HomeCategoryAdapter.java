package com.example.icedr.homescreendemo.widget.categories;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.model.Asset;
import com.example.icedr.homescreendemo.widget.HomeCategoriesAdapter;
import com.example.icedr.homescreendemo.widget.constants.Motion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private static final String TAG = HomeCategoryAdapter.class.getCanonicalName();

    private Context context;
    private List<Asset> assetList = new ArrayList<>();
    private RecyclerView browseListView;
    private RecyclerView categoryListView;
    private int browseListPosition = 0;
    private int selectedItemPosition = 0;

    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean focused) {
            notifyDataSetChanged();
        }
    };

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) return false;
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    moveSelector((LinearLayoutManager) categoryListView.getLayoutManager(), Motion.LEFT, false);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    moveSelector((LinearLayoutManager) categoryListView.getLayoutManager(), Motion.RIGHT, false);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    ((HomeCategoriesAdapter) browseListView.getAdapter()).moveSelector((LinearLayoutManager) browseListView.getLayoutManager(), browseListPosition, Motion.UP);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    ((HomeCategoriesAdapter) browseListView.getAdapter()).moveSelector((LinearLayoutManager) browseListView.getLayoutManager(), browseListPosition, Motion.DOWN);
                    return true;
                }
            }
            return false;
        }
    };

    public HomeCategoryAdapter(Context context, RecyclerView browseListView) {
        this.context = context;
        this.browseListView = browseListView;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(onKeyListener);
        recyclerView.setOnFocusChangeListener(onFocusChangeListener);
        this.categoryListView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setSelected(categoryListView.hasFocus() && selectedItemPosition == position);
        holder.bind(context, getItem(position));
        holder.handleScaleChanging(categoryListView.hasFocus() && selectedItemPosition == position);
    }

    private void moveSelector(LinearLayoutManager layoutManager, int direction, boolean cycled) {
        try {
            int oldSelectedItemPosition = selectedItemPosition;
            selectedItemPosition = cycled ?
                    (((selectedItemPosition + direction) >= 0 && (selectedItemPosition + direction) < getItemCount()) ? selectedItemPosition + direction : direction == Motion.LEFT ? getItemCount() - 1 : 0) :
                    (selectedItemPosition + direction < 0 ? 0 : selectedItemPosition + direction > getItemCount() - 1 ? getItemCount() - 1 : selectedItemPosition + direction);
            scrollToPositionWithOffset(layoutManager, selectedItemPosition, oldSelectedItemPosition, direction);
        } catch (Exception e) {
            Log.e(TAG, "scrollToPositionWithOffset: Motion - skipped", e);
        }
    }

    private void scrollToPositionWithOffset(LinearLayoutManager layoutManager, int selectedItemPosition, int oldSelectedItemPosition, int direction) {
        switch (direction) {
            case Motion.LEFT:
                if ((layoutManager.findFirstCompletelyVisibleItemPosition() == selectedItemPosition || layoutManager.findFirstVisibleItemPosition() == selectedItemPosition) &&
                        layoutManager.findFirstVisibleItemPosition() != 0) {
                    motionByDirectionWithOffset(
                            layoutManager,
                            selectedItemPosition,
                            oldSelectedItemPosition,
                            getOffsetDimension());
                } else {
                    motionByDirection(
                            layoutManager,
                            selectedItemPosition,
                            oldSelectedItemPosition);
                    if (layoutManager.findFirstVisibleItemPosition() > selectedItemPosition)
                        categoryListView.scrollBy(-getOffsetDimension(), 0);
                }
                break;
            case Motion.RIGHT:
                if ((layoutManager.findLastCompletelyVisibleItemPosition() == selectedItemPosition || layoutManager.findLastVisibleItemPosition() == selectedItemPosition) &&
                        layoutManager.findLastVisibleItemPosition() != getItemCount() - 1) {
                    motionByDirectionWithOffset(
                            layoutManager,
                            selectedItemPosition,
                            oldSelectedItemPosition,
                            categoryListView.getWidth() - categoryListView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.getWidth() - getOffsetDimension());
                } else {
                    motionByDirection(layoutManager, selectedItemPosition, oldSelectedItemPosition);
                    if (layoutManager.findLastVisibleItemPosition() < selectedItemPosition)
                        categoryListView.scrollBy(getOffsetDimension(), 0);
                }
                break;
        }
    }

    private void motionByDirection(LinearLayoutManager layoutManager, int selectedItemPosition, int oldSelectedItemPosition) {
        this.notifyItemChanged(oldSelectedItemPosition);
        layoutManager.scrollToPosition(selectedItemPosition);
        this.notifyItemChanged(selectedItemPosition);
    }

    private void motionByDirectionWithOffset(LinearLayoutManager layoutManager, int selectedItemPosition, int oldSelectedItemPosition, int offsetDimension) {
        this.notifyItemChanged(oldSelectedItemPosition);
        layoutManager.scrollToPositionWithOffset(selectedItemPosition, offsetDimension);
        this.notifyItemChanged(selectedItemPosition);
    }

    private int getOffsetDimension() {
        return (int) context.getResources().getDimension(R.dimen.offset);
    }

    public void swapData(List<Asset> assetList, int position) {
        this.browseListPosition = position;
        this.assetList.clear();
        this.assetList.addAll(assetList);
        this.notifyDataSetChanged();
    }

    private Asset getItem(int position) {
        return assetList.get(position);
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private AnimatorSet scaleXY;

        private Runnable mScaleToBigSize = new Runnable() {
            @Override
            public void run() {
                if (scaleXY != null) {
                    if (scaleXY.isRunning()) {
                        return;
                    }
                }
                showScalingAnimation();
            }
        };

        private Runnable mScaleToSmallSize = new Runnable() {
            @Override
            public void run() {
                itemRootView.setScaleX(1f);
                itemRootView.setScaleY(1f);
            }
        };

        @BindView(R.id.item)
        ImageView itemCoverView;

        @BindView(R.id.progress)
        ProgressBar itemProgressView;

        private View itemRootView;

        ViewHolder(View itemRootView) {
            super(itemRootView);
            this.itemRootView = itemRootView;
            ButterKnife.bind(this, itemRootView);
        }

        void bind(Context context, Asset asset) {
            Glide.with(context)
                    .load(asset.getAssetUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .override((int) context.getResources().getDimension(R.dimen.defaultWidth), (int) context.getResources().getDimension(R.dimen.defaultHeight))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            itemProgressView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            itemProgressView.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(itemCoverView);
        }

        void handleScaleChanging(boolean selected) {
            if (selected) {
                itemRootView.removeCallbacks(mScaleToBigSize);
                itemRootView.post(mScaleToBigSize);
            } else {
                itemRootView.removeCallbacks(mScaleToBigSize);
                if (scaleXY != null) {
                    scaleXY.cancel();
                    scaleXY = null;
                }
                itemRootView.post(mScaleToSmallSize);
            }
        }

        private void showScalingAnimation() {
            if (!itemRootView.isSelected()) {
                Log.d(TAG, "showScalingAnimation: " + "Not selected. Skip scaling");
                return;
            }

            doAnimation(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d(TAG, "onAnimationStart - Scaling");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG, "onAnimationEnd - Scaling");
                    scaleXY = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }

        private void doAnimation(final Animator.AnimatorListener animatorListener) {
            scaleXY = new AnimatorSet();
            scaleXY.playTogether(
                    ObjectAnimator.ofFloat(itemRootView, ViewGroup.SCALE_X, 1f, 1.2f),
                    ObjectAnimator.ofFloat(itemRootView, ViewGroup.SCALE_Y, 1f, 1.2f)
            );
            scaleXY.setDuration(169);
            scaleXY.setInterpolator(new LinearInterpolator());
            scaleXY.addListener(animatorListener);
            scaleXY.start();
        }
    }
}
