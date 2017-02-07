package com.example.icedr.homescreendemo.delegates;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.holders.HomeCategoryAdapter2;


/**
 * Created by IceDr on 07.02.2017.
 */

public class HomeCategory2 extends RecyclerView.Adapter<HomeCategory2.TeaserBottomHolder> {

    private Context mContext;

    public HomeCategory2(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TeaserBottomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeaserBottomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(TeaserBottomHolder holder, int position) {
        holder.bind(mContext, new HomeCategoryAdapter2());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class TeaserBottomHolder extends RecyclerView.ViewHolder {

        RecyclerView rootView;

        public TeaserBottomHolder(View itemRootView) {
            super(itemRootView);
            rootView = (RecyclerView) itemRootView;
        }

        public void bind(Context context, HomeCategoryAdapter2 adapter) {
            if(rootView.getAdapter() == null) {
                rootView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                rootView.setAdapter(adapter);
            }
        }

    }

}
