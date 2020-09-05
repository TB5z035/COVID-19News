package com.java.zhangjiayou.ui.Explore;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class HistoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Set<String> historyIds;

    public HistoryViewAdapter(Set<String> ids) {
        historyIds = ids;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
