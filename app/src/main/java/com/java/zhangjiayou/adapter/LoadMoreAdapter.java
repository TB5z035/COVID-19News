package com.java.zhangjiayou.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.network.NoResponseError;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.ui.DetailActivity;
import com.java.zhangjiayou.ui.home.HomeFragment;
import com.java.zhangjiayou.util.Passage;

import org.ansj.splitWord.analysis.ToAnalysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer index = 1;
    private List<Passage> dataList;
    private Set<String> historyIds;
    private HomeFragment fragment;

    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;
    private int loadState = 2;

    public final int LOADING = 1;
    public final int LOADING_COMPLETE = 2;
    public final int LOADING_END = 3;

    public Passage getItem(int position) {
        return dataList.get(position);
    }

    public LoadMoreAdapter(Set<String> map, HomeFragment fragment) {
        this.dataList = new ArrayList<>();
        this.historyIds = map;
        this.fragment = fragment;
    }

     public void getData(final boolean mode, String type, int size) {
        if (mode) {
            index = 1;
        }
        new Thread(() -> {
            try {
                if (mode) dataList.clear();
                dataList.addAll(new PassagePortal().getNewsFromType(type.toLowerCase(), index, size));
                index++;
                fragment.onDataGot();
            } catch (NullPointerException e) {
                System.out.println(e.getStackTrace());
            } catch (NoResponseError noResponseError) {
                noResponseError.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
//        if (position % 2 == 0) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_recyclerview, parent, false);

            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            holder.setIsRecyclable(false);
            recyclerViewHolder.titleView.setText(dataList.get(position).getTitle());

            if (historyIds.contains(dataList.get(position).getId()))
                recyclerViewHolder.titleView.setTextColor(R.color.colorTextAccent);
            recyclerViewHolder.contentView.setText(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                            .format(dataList.get(position).getDate()));
            recyclerViewHolder.originView.setText(dataList.get(position).getProperties().get("source").toString());
            recyclerViewHolder.rawJSON = dataList.get(position).rawJSON;
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView contentView;
        TextView originView;
        CardView cardView;
        String rawJSON;

        RecyclerViewHolder(final View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title_view);
            contentView = (TextView) itemView.findViewById(R.id.time_view);
            originView = (TextView)itemView.findViewById(R.id.origin_view);
            cardView = itemView.findViewById(R.id.passage_card_view);

            //点击时：将文章保存至数据库；分词，然后将关键词-id映射关系保存到本地映射表；延迟通知adapter以留足够的时间，完成点击动画后项目才变灰；启动详情页activity
            cardView.setOnClickListener(v -> {
//                    itemView.getContext().getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
//                            .edit().putString(dataList.get(getLayoutPosition()).getId(), null)
//                            .apply();
                historyIds.add(dataList.get(getLayoutPosition()).getId());

                new Thread(() -> {
                    itemView.getContext()
                            .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                            .edit()
                            .putStringSet(String.valueOf(R.string.history_fileid_set_key), historyIds)
                            .apply();
                    Passage passageInDB = PassageDatabase.getInstance(null).getPassageDao().getPassageFromId(dataList.get(getLayoutPosition()).getId());
                    if (passageInDB == null)
                        PassageDatabase.getInstance(null).getPassageDao().insert(dataList.get(getLayoutPosition()));
                    String title = dataList.get(getLayoutPosition()).getTitle();

                    ToAnalysis.parse(title).forEach((v1) -> {
                        if (v1 != null) {
                            Set<String> strings = itemView.getContext().getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
                                    .getStringSet(v1.getName(), new HashSet<>());

                            strings.add(dataList.get(getLayoutPosition()).getId());

                            itemView.getContext().getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
                                    .edit().putStringSet(v1.getName(), new HashSet<>(strings)).apply();
                            System.out.println(v1.getName());
                        }
                    });

                }).start();
                itemView.postDelayed(() -> notifyDataSetChanged(), 200);

                Intent intent = new Intent();
                intent.putExtra("id", -1);
                intent.putExtra("rawJSON", rawJSON);
                intent.setClass(fragment.getContext(), DetailActivity.class);
                LoadMoreAdapter.this.fragment.startActivity(intent);
            });
        }
    }


    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

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