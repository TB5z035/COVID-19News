package com.java.zhangjiayou.adapter;

import android.app.Activity;
import android.content.Intent;
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
    private Integer index = 1;
    private Set<String> historyIds;
    private List<PassageWithNoContent> dataList;
    private Activity activity;
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;


    public SearchResultAdapter(Activity activity) {
        this.activity = activity;
        this.dataList = new CopyOnWriteArrayList<>();
        this.historyIds = new HashSet<>();
    }

    public void refreshDataList(String query) {
        dataList.clear();

        Set<PassageWithNoContent> wholeResult = SearchMapManager.getMap().get(query);
        if (wholeResult != null) {
            for (PassageWithNoContent p :
                    wholeResult) {
                p.whole = true;
            }
            for (PassageWithNoContent p : wholeResult) {
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
        setLoadState(LOADING_COMPLETE);
    }

//     普通布局
//    private final int TYPE_ITEM = 1;
//     脚布局
//    private final int TYPE_FOOTER = 2;

    @Override
    public int getItemViewType(int position) {
//         最后一个item设置为FooterView
//        if (position + 1 == getItemCount()) {
//        if (position % 2 == 0) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//          }
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
//        if (holder instanceof HistoryViewAdapter.RecyclerViewHolder) {
        final SearchResultAdapter.RecyclerViewHolder recyclerViewHolder = (SearchResultAdapter.RecyclerViewHolder) holder;
        holder.setIsRecyclable(false);
        recyclerViewHolder.titleView.setText(dataList.get(position).getTitle());
        recyclerViewHolder.id = dataList.get(position).getId();
        if (dataList.get(position).getDate() != null)
            recyclerViewHolder.contentView.setText(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                            .format(dataList.get(position).getDate()));
        else {
            recyclerViewHolder.contentView.setVisibility(View.GONE);
        }
//        } else if (holder instanceof HistoryViewAdapter.FootViewHolder) {
//            HistoryViewAdapter.FootViewHolder footViewHolder = (HistoryViewAdapter.FootViewHolder) holder;
//            switch (loadState) {
//                case LOADING: // 正在加载
//                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
//                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
//                    footViewHolder.llEnd.setVisibility(View.GONE);
//                    break;
//
//                case LOADING_COMPLETE: // 加载完成
//                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
//                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
//                    footViewHolder.llEnd.setVisibility(View.GONE);
//                    break;
//
//                case LOADING_END: // 加载到底
//                    footViewHolder.pbLoading.setVisibility(View.GONE);
//                    footViewHolder.tvLoading.setVisibility(View.GONE);
//                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
//                    break;
//
//                default:
//                    break;
//            }
//        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView titleView;
        TextView contentView;
        CardView cardView;

        RecyclerViewHolder(final View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.title_view);
            contentView = (TextView) itemView.findViewById(R.id.time_view);
            cardView = itemView.findViewById(R.id.passage_card_view);
            cardView.setOnClickListener(v -> {
                //TODO:put selected passage into database
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                }).start();
            });
        }
    }

    //TODO:Enable scroll to load more
//    private class FootViewHolder extends RecyclerView.ViewHolder {
//
//        ProgressBar pbLoading;
//        TextView tvLoading;
//        LinearLayout llEnd;
//
//        FootViewHolder(View itemView) {
//            super(itemView);
//            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
//            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
//            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
//        }
//    }
//
    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}