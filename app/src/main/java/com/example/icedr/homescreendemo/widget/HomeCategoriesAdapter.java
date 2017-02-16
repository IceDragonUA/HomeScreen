package com.example.icedr.homescreendemo.widget;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.ViewHolder> {

    private static final String TAG = HomeCategoriesAdapter.class.getCanonicalName();

    private List<Project> projectList;
    private RecyclerView browseListView;

    public HomeCategoriesAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.browseListView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_module, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position), position);
    }

    public void moveSelector(int selectedItemPosition, int direction) {
        try {
            selectedItemPosition = (selectedItemPosition + direction < 0 ? 0 : selectedItemPosition + direction > getItemCount() - 1 ? getItemCount() - 1 : selectedItemPosition + direction);
            browseListView.smoothScrollToPosition(selectedItemPosition);
            browseListView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.requestFocus();
        } catch (Exception e) {
            Log.e(TAG, "scrollToPositionWithOffset: Motion - skipped", e);
        }
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
        RowRecyclerView categoryListView;

        ViewHolder(View itemRootView) {
            super(itemRootView);
            ButterKnife.bind(this, itemRootView);
        }

        void bind(final Project project, final int position) {
            title.setText(project.getProjectName());
            DataDao.getInstance().getAssetsByProjectId(project.getProjectId(), AssetType.IMAGE, new IDataLoadingResult<List<Asset>>() {
                @Override
                public void onResult(List<Asset> result) {
                    categoryListView.getAdapter().swapData(result, position);
                }

                @Override
                public void onFailure(Throwable ex) {
                    Log.e(TAG, "onFailure: AssetList not loaded", ex);
                }
            });
        }
    }
}
