package com.example.icedr.homescreendemo.widget.categories;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private RecyclerView browseListView;
    private RecyclerView categoryListView;

    private int browseListPosition = 0;
    private int selectedItemPosition = 0;

    private List<Asset> assetList = new ArrayList<>();

    private Handler mHandler = new Handler();

    private Runnable mActionLeft = new Runnable() {
        @Override public void run() {
            if (selectedItemPosition != 0)
                moveSelector(Motion.LEFT);
        }
    };

    private Runnable mActionRight = new Runnable() {
        @Override public void run() {
            if (selectedItemPosition != getItemCount() - 1)
                moveSelector(Motion.RIGHT);
        }
    };

    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean focused) {
//            notifyDataSetChanged();
        }
    };

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mHandler.removeCallbacks(mActionLeft);
                    mHandler.postDelayed(mActionLeft, 250);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mHandler.removeCallbacks(mActionRight);
                    mHandler.postDelayed(mActionRight, 250);
                    return true;
                }
                return false;
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    // TODO: 13.02.2017 implement scrolling selection logic
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    // TODO: 13.02.2017 implement scrolling selection logic
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    ((HomeCategoriesAdapter) browseListView.getAdapter()).moveSelector(browseListPosition, Motion.UP);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    ((HomeCategoriesAdapter) browseListView.getAdapter()).moveSelector(browseListPosition, Motion.DOWN);
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
//        recyclerView.setOnKeyListener(onKeyListener);
//        recyclerView.setOnFocusChangeListener(onFocusChangeListener);
        this.categoryListView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, getItem(position));
    }

    private void moveSelector(int direction) {
        try {
            int oldSelectedItemPosition = selectedItemPosition;
            selectedItemPosition = selectedItemPosition + direction < 0 ? 0 : selectedItemPosition + direction > getItemCount() - 1 ? getItemCount() - 1 : selectedItemPosition + direction;
            this.notifyItemChanged(oldSelectedItemPosition);
            categoryListView.smoothScrollToPosition(selectedItemPosition);
            this.notifyItemChanged(selectedItemPosition);
        } catch (Exception e) {
            Log.e(TAG, "scrollToPositionWithOffset: Motion - skipped", e);
        }
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        ImageView itemCoverView;

        @BindView(R.id.progress)
        ProgressBar itemProgressView;

        ViewHolder(View itemRootView) {
            super(itemRootView);
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
    }
}
