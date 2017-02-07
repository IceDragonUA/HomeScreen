package com.example.icedr.homescreendemo.delegates;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.holders.HomeCategoryAdapter1;


/**
 * Created by IceDr on 07.02.2017.
 */

public class HomeCategory1 extends RecyclerView.Adapter<HomeCategory1.DefaultChannelHolder> {

    private Context mContext;

    public HomeCategory1(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public DefaultChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultChannelHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(DefaultChannelHolder holder, int position) {
        holder.bind(mContext, new HomeCategoryAdapter1());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class DefaultChannelHolder extends RecyclerView.ViewHolder {

        RecyclerView rootView;

        public DefaultChannelHolder(View itemRootView) {
            super(itemRootView);
            rootView = (RecyclerView) itemRootView;
        }

        public void bind(Context context, HomeCategoryAdapter1 adapter) {
            if(rootView.getAdapter() == null) {
                rootView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                rootView.setAdapter(adapter);
            }
        }

    }

}
