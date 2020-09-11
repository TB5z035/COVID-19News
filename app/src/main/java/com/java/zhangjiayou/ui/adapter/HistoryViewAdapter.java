package com.java.zhangjiayou.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.ui.DetailActivity;
import com.java.zhangjiayou.util.Passage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HistoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer index = 1;
    private Set<String> historyIds;
    private List<Passage> dataList;
    private Activity activity;

    public HistoryViewAdapter(Set<String> ids, Activity activity) {
        this.historyIds = ids;
        this.activity = activity;
        dataList = new ArrayList<>();
        activity.getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        getData();
                    }
                });
    }

    public void getData() {
        dataList.clear();
        historyIds = activity
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        new Thread(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
            PassageDatabase db = PassageDatabase.getInstance(activity);
            dataList = db.getPassageDao().getPassagesFromIds(new ArrayList<>(historyIds));
            Collections.sort(dataList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            activity.runOnUiThread(this::notifyDataSetChanged);
        }).start();

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recyclerview, parent, false);

        return new HistoryViewAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HistoryViewAdapter.RecyclerViewHolder recyclerViewHolder = (HistoryViewAdapter.RecyclerViewHolder) holder;
        holder.setIsRecyclable(false);
        recyclerViewHolder.titleView.setText(dataList.get(position).getTitle());

        recyclerViewHolder.rawJSON = dataList.get(position).rawJSON;
        recyclerViewHolder.originView.setVisibility(View.GONE);

        recyclerViewHolder.contentView.setText(
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                        .format(dataList.get(position).getDate()));
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView contentView;
        TextView originView;
        CardView cardView;
        String rawJSON;

        RecyclerViewHolder(final View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title_view);
            contentView = itemView.findViewById(R.id.time_view);
            cardView = itemView.findViewById(R.id.passage_card_view);
            originView = itemView.findViewById(R.id.origin_view);
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("id", -1);
                intent.putExtra("rawJSON", rawJSON);
                intent.setClass(activity, DetailActivity.class);
                HistoryViewAdapter.this.activity.startActivity(intent);
            });
        }
    }
}
