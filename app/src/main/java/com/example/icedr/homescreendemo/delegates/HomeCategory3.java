package com.example.icedr.homescreendemo.delegates;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icedr.homescreendemo.R;
import com.example.icedr.homescreendemo.holders.HomeCategoryAdapter3;


/**
 * Created by IceDr on 07.02.2017.
 */

public class HomeCategory3 extends RecyclerView.Adapter<HomeCategory3.TeaserPromoHolder> {

    private Context mContext;

    public HomeCategory3(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TeaserPromoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeaserPromoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(TeaserPromoHolder holder, int position) {
        holder.bind(mContext, new HomeCategoryAdapter3());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class TeaserPromoHolder extends RecyclerView.ViewHolder {

        RecyclerView rootView;

        public TeaserPromoHolder(View itemRootView) {
            super(itemRootView);
            rootView = (RecyclerView) itemRootView;
        }

        public void bind(Context context, HomeCategoryAdapter3 adapter) {
            if(rootView.getAdapter() == null) {
                rootView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                rootView.setAdapter(adapter);
            }

        }

    }

}
