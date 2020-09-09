package com.java.zhangjiayou.ui.home;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreAdapter loadMoreAdapter;
    private String type;
    private Integer size = 20;
    private Set<String> historyIds;
    private final static String ID_TYPE = "id_type";
    private final static String ID_HISTORY = "id_history";


//    public HomeFragment(String type, Set<String> historyIds) {
//        this.historyIds = historyIds;
//        if (type.equals("News") || type.equals("Paper")) this.type = type.toLowerCase();
//        else throw new UnsupportedPassageType();
//    }

    public HomeFragment() {
        // Intentionally left blank
    }

    public static HomeFragment newInstance(String type, Set<String> historyIds) {
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        ArrayList<String> param = new ArrayList<>(historyIds);
        args.putString(ID_TYPE, type);
        args.putStringArrayList(ID_HISTORY, param);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(ID_TYPE);
        //TODO

        ArrayList<String> arrayList = requireArguments().getStringArrayList(ID_HISTORY);

        if (arrayList == null) historyIds = new HashSet<>();
        else
            historyIds = new HashSet<>(arrayList);
    }

    void onDataGot() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
            }
        });
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
                loadMoreAdapter.getData(true, type, size);
            }
        });


        recyclerView = root.findViewById(R.id.recycler_view);
        loadMoreAdapter = new LoadMoreAdapter(historyIds, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreAdapter);
        loadMoreAdapter.getData(true, type, size);

        //Pull-to-load-more-listener
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                loadMoreAdapter.getData(false, type, size);
            }
        });

        return root;
    }
}