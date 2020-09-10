package com.java.zhangjiayou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.network.NoResponseError;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.search.SearchMapManager;
import com.java.zhangjiayou.ui.DetailActivity;
import com.java.zhangjiayou.util.PassageWithNoContent;

import org.ansj.splitWord.analysis.ToAnalysis;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PassageWithNoContent> dataList;
    private Activity activity;

    public SearchResultAdapter(Activity activity) {
        this.activity = activity;
        this.dataList = new CopyOnWriteArrayList<>();
    }

    public void refreshDataList(String query) {
        dataList.clear();

        Set<PassageWithNoContent> wholeResult = SearchMapManager.getMap().get(query);
        if (wholeResult != null) {
            for (PassageWithNoContent p : wholeResult) {
                p.whole = true;
                if (!dataList.contains(p)) dataList.add(p);
            }
        }

        ToAnalysis.parse(query).forEach((v) -> {
            Set<PassageWithNoContent> result = SearchMapManager.getMap().get(v.getName());
            if (result != null)
                for (PassageWithNoContent p : result) {
                    if (!dataList.contains(p)) dataList.add(p);
                }
        });

        Collections.sort(dataList, (o1, o2) -> {
            if (o1.getDate() == null && o2.getDate() == null) return 0;
            else if (o1.getDate() == null) return 1;
            else if (o2.getDate() == null) return -1;
            else if (!o1.whole && o2.whole) return 1;
            else if (o1.whole && !o2.whole) return -1;
            else return o2.getDate().compareTo(o1.getDate());
        });

        notifyDataSetChanged();
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

        return new SearchResultAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SearchResultAdapter.RecyclerViewHolder recyclerViewHolder = (SearchResultAdapter.RecyclerViewHolder) holder;
        holder.setIsRecyclable(false);
        recyclerViewHolder.titleView.setText(dataList.get(position).getTitle());
        recyclerViewHolder.id = dataList.get(position).getId();
        recyclerViewHolder.originView.setVisibility(View.GONE);
        if (dataList.get(position).getDate() != null)
            recyclerViewHolder.contentView.setText(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                            .format(dataList.get(position).getDate()));
        else {
            recyclerViewHolder.contentView.setVisibility(View.GONE);
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView titleView;
        TextView contentView;
        TextView originView;
        CardView cardView;

        RecyclerViewHolder(final View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title_view);
            contentView = itemView.findViewById(R.id.time_view);
            originView = itemView.findViewById(R.id.origin_view);
            cardView = itemView.findViewById(R.id.passage_card_view);
            cardView.setOnClickListener(v -> new Thread(() -> {
                Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                String rawJSON = "";
                try {
                    rawJSON = new PassagePortal().getNewsJSONFromId(id);
                } catch (NoResponseError noResponseError) {
                    //TODO:处理网络无连接
                    noResponseError.printStackTrace();
                }
                String finalRawJSON = rawJSON;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("id", -1);
                        intent.putExtra("rawJSON", finalRawJSON);
                        intent.setClass(activity, DetailActivity.class);
                        activity.startActivity(intent);
                    }
                });
            }).start());
        }
    }
}
