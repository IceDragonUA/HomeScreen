package com.example.icedr.homescreendemo.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.dao.DataDao;
import com.example.icedr.homescreendemo.model.Asset;
import com.example.icedr.homescreendemo.model.AssetType;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.network.IDataLoadingResult;
import com.example.icedr.homescreendemo.widget.constants.Motion;
import com.example.icedr.homescreendemo.widget.categories.HomeCategoryAdapter;
import com.example.icedr.homescreendemo.widget.managers.SmoothScrolledLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.ViewHolder> {

    private static final String TAG = HomeCategoriesAdapter.class.getCanonicalName();

    private Context context;
    private List<Project> projectList;
    private RecyclerView browseListView;

    public HomeCategoriesAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.browseListView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_module, parent, false));
        viewHolder.categoryListView.setHasFixedSize(true);
        viewHolder.categoryListView.setLayoutManager(new SmoothScrolledLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewHolder.categoryListView.setAdapter(new HomeCategoryAdapter(parent.getContext(), browseListView));
        viewHolder.categoryListView.setAnimation(null);
        viewHolder.categoryListView.setNestedScrollingEnabled(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position), position);
    }

    public void moveSelector(LinearLayoutManager layoutManager, int selectedItemPosition, int direction) {
        try {
            selectedItemPosition = (selectedItemPosition + direction < 0 ? 0 : selectedItemPosition + direction > getItemCount() - 1 ? getItemCount() - 1 : selectedItemPosition + direction);
            scrollToPositionWithOffset(layoutManager, selectedItemPosition, direction);
        } catch (Exception e) {
            Log.e(TAG, "scrollToPositionWithOffset: Motion - skipped", e);
        }
    }

    private void scrollToPositionWithOffset(LinearLayoutManager layoutManager, int selectedItemPosition, int direction) {
        switch (direction) {
            case Motion.UP:
                if ((layoutManager.findFirstCompletelyVisibleItemPosition() == selectedItemPosition || layoutManager.findFirstVisibleItemPosition() == selectedItemPosition) &&
                        layoutManager.findFirstVisibleItemPosition() != 0) {
                    motionByDirectionWithOffset(
                            layoutManager,
                            selectedItemPosition,
                            getOffsetDimension());
                } else {
                    motionByDirection(
                            layoutManager,
                            selectedItemPosition
                    );
                    if (layoutManager.findFirstVisibleItemPosition() > selectedItemPosition)
                        browseListView.scrollBy(-getOffsetDimension(), 0);
                }
                break;
            case Motion.DOWN:
                if ((layoutManager.findLastCompletelyVisibleItemPosition() == selectedItemPosition || layoutManager.findLastVisibleItemPosition() == selectedItemPosition) &&
                        layoutManager.findLastVisibleItemPosition() != getItemCount() - 1) {
                    motionByDirectionWithOffset(
                            layoutManager,
                            selectedItemPosition,
                            browseListView.getHeight() - browseListView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.getHeight() - getOffsetDimension());
                } else {
                    motionByDirection(layoutManager, selectedItemPosition);
                    if (layoutManager.findLastVisibleItemPosition() < selectedItemPosition)
                        browseListView.scrollBy(getOffsetDimension(), 0);
                }
                break;
        }
    }

    private void motionByDirection(LinearLayoutManager layoutManager, int selectedItemPosition) {
        browseListView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.requestFocus();
        layoutManager.scrollToPosition(selectedItemPosition);
    }

    private void motionByDirectionWithOffset(LinearLayoutManager layoutManager, int selectedItemPosition, int offsetDimension) {
        browseListView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.requestFocus();
        layoutManager.scrollToPositionWithOffset(selectedItemPosition, offsetDimension);
    }

    private int getOffsetDimension() {
        return (int) context.getResources().getDimension(R.dimen.offset);
    }

    private Project getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.list_item)
        RecyclerView categoryListView;

        ViewHolder(View itemRootView) {
            super(itemRootView);
            ButterKnife.bind(this, itemRootView);
        }

        void bind(final Project project, final int position) {
            title.setText(project.getProjectName());
            DataDao.getInstance().getAssetsByProjectId(project.getProjectId(), AssetType.IMAGE, new IDataLoadingResult<List<Asset>>() {
                @Override
                public void onResult(List<Asset> result) {
                    ((HomeCategoryAdapter) categoryListView.getAdapter()).swapData(result, position);
                }

                @Override
                public void onFailure(Throwable ex) {
                    Log.e(TAG, "onFailure: AssetList not loaded", ex);
                }
            });
        }
    }
}