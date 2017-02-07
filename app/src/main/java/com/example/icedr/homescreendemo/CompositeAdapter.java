package com.example.icedr.homescreendemo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeAdapter extends RecyclerView.Adapter {

    private List<RecyclerView.Adapter> mAdapters;

    private CompositeAdapter(List<RecyclerView.Adapter> adapters) {
        mAdapters = Collections.unmodifiableList(adapters);
        AdaptersObserver adaptersObserver = new AdaptersObserver();
        for (RecyclerView.Adapter adapter : mAdapters) {
            adapter.registerAdapterDataObserver(adaptersObserver);
        }
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < mAdapters.size(); i++) {
            RecyclerView.Adapter adapter = mAdapters.get(i);
            int count = adapter.getItemCount();
            if (position < count) {
                return i;
            }
            position -= count;
        }
        throw new IllegalStateException("Requested position is too far.");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.Adapter adapter = mAdapters.get(viewType);
        return adapter.onCreateViewHolder(parent, 0);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        for (int i = 0; i < mAdapters.size(); i++) {
            RecyclerView.Adapter adapter = mAdapters.get(i);
            int count = adapter.getItemCount();
            if (position < count) {
                adapter.onBindViewHolder(holder, position);
                return;
            }
            position -= count;
        }
        throw new IllegalStateException("Requested position is too far.");
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (RecyclerView.Adapter adapter : mAdapters) {
            count += adapter.getItemCount();
        }
        return count;
    }

    private class AdaptersObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

    }

    public static class Builder {

        private List<RecyclerView.Adapter> mAdapters = new ArrayList<>();

        public Builder addAdapter(RecyclerView.Adapter adapter) {
            checkNotNull(adapter);
            mAdapters.add(adapter);
            return this;
        }

        public CompositeAdapter build() {
            return new CompositeAdapter(mAdapters);
        }

        public static <T> T checkNotNull(T reference) {
            if (reference == null) {
                throw new NullPointerException();
            }
            return reference;
        }

    }

}
