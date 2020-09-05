package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.network.NoResponseError;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.util.Passage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {
    private Integer index = 1;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreAdapter loadMoreAdapter;
    private String type;
    private Integer size = 20;
    private Set<String> historyIds;

    private List<Passage> dataList = new ArrayList<>();

    public LoadMoreAdapter getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

    public HomeFragment(String type, Set<String> historyIds) {
        this.historyIds = historyIds;
        if (type.equals("news") || type.equals("paper")) this.type = type;
        else throw new UnsupportedPassageType();
    }

    private void getData(final boolean mode) {
        if (mode) {
            index = 1;
        }
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                if (mode) dataList.clear();
                dataList.addAll(new PassagePortal().getNewsFromType(type, index, size));
                index++;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                    }
                });
            } catch (NullPointerException e) {
                System.out.println(e.getStackTrace());
            } catch (NoResponseError noResponseError) {
                noResponseError.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = root.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setRefreshing(true);

        //Pull-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), R.string.toast_test, Toast.LENGTH_SHORT).show();
                getData(true);
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        loadMoreAdapter = new LoadMoreAdapter(dataList, historyIds, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreAdapter);
        getData(true);

        //Pull-to-load-more-listener
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                getData(false);
            }
        });

        return root;
    }
}