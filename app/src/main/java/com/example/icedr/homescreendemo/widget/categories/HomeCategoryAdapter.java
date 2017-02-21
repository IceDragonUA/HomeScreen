package com.example.icedr.homescreendemo.widget.categories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.example.icedr.homescreendemo.widget.category.GridRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private static final String TAG = HomeCategoryAdapter.class.getCanonicalName();

    private Context context;
    private int browseListPosition = 0;
    private GridRecyclerView browseListView;
    private RowRecyclerView categoryListView;
    private List<Asset> assetList = new ArrayList<>();

    public HomeCategoryAdapter(Context context, GridRecyclerView browseListView) {
        this.context = context;
        this.browseListView = browseListView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.categoryListView = (RowRecyclerView) recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, getItem(position));
    }

    GridRecyclerView getBrowseListView() {
        return browseListView;
    }

    int getBrowseListPosition() {
        return browseListPosition;
    }

    public void swapData(List<Asset> assetList, int position) {
        this.browseListPosition = position;
        this.assetList.clear();
        this.assetList.addAll(assetList);
        this.notifyDataSetChanged();
        this.categoryListView.scrollToPosition(browseListView.getItemPosition(position));
    }

    private Asset getItem(int position) {
        return assetList.get(position);
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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
