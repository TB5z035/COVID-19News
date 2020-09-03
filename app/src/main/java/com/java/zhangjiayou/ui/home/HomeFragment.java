package com.java.zhangjiayou.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.network.NoResponseError;
import com.java.zhangjiayou.network.Passage;
import com.java.zhangjiayou.network.PassagePortal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private Integer index = 1;

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreAdapter loadMoreAdapter;

    private List<Passage> dataList = new ArrayList<>();

    private void getData(final boolean mode) {
        if (mode) {
            index = 1;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    onUpdateList(new PassagePortal().getNewsFromType("news", index, 20), mode);
                } catch (NoResponseError noResponseError) {
                    noResponseError.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void onUpdateList(List<Passage> list, boolean mode) {
        try {
            if (mode) dataList.clear();
            dataList.addAll(list);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                }
            });
            index++;
        } catch (NullPointerException e) {
//            Toast.makeText(null, "You clicked too fast!", Toast.LENGTH_SHORT).show();
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(new SimpleDateFormat("mm:ss").format(new Date()));
//            }
//        });

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
        loadMoreAdapter = new LoadMoreAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreAdapter);
        getData(true);

        //Pull-to-load-more-listener
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);

                if (dataList.size() < 100) {
                    getData(false);
                } else {
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                }
            }
        });

        return root;
    }


}