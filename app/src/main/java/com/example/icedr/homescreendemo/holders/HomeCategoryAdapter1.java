package com.example.icedr.homescreendemo.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icedr.homescreendemo.R;


/**
 * Created by IceDr on 07.02.2017.
 */

public class HomeCategoryAdapter1 extends RecyclerView.Adapter<HomeCategoryAdapter1.MegaHolder> {

    @Override
    public MegaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MegaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_1, parent, false));
    }

    @Override
    public void onBindViewHolder(MegaHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MegaHolder extends RecyclerView.ViewHolder {

        public MegaHolder(View itemRootView) {
            super(itemRootView);
        }
    }

}
