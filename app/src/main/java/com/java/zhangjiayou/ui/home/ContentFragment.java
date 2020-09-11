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
import com.java.zhangjiayou.ui.adapter.TypeAdapter;
import com.java.zhangjiayou.ui.adapter.NormalTypeAdapter;
import com.java.zhangjiayou.ui.adapter.MoreTypeAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContentFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TypeAdapter loadMoreAdapter;
    private String type;
    private Integer size = 20;
    private Set<String> historyIds;
    private final static String ID_TYPE = "id_type";
    private final static String ID_HISTORY = "id_history";

    public ContentFragment() {
        // Intentionally left blank
    }

    public static ContentFragment newInstance(String type, Set<String> historyIds) {
        ContentFragment fragment = new ContentFragment();

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
        type = requireArguments().getString(ID_TYPE);

        ArrayList<String> arrayList = requireArguments().getStringArrayList(ID_HISTORY);

        if (arrayList == null) historyIds = new HashSet<>();
        else historyIds = new HashSet<>(arrayList);
    }

    public void onDataGot(boolean mode) {
        getActivity().runOnUiThread(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadMoreAdapter.setLoadState(mode ? loadMoreAdapter.LOADING_COMPLETE : loadMoreAdapter.LOADING_END);
        });
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = root.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setRefreshing(true);
        //Pull-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(getContext(), R.string.toast_test, Toast.LENGTH_SHORT).show();
            loadMoreAdapter.getData(true, type, size);
        });

        recyclerView = root.findViewById(R.id.recycler_view);
        if (type.equals("News") || type.equals("Paper"))
            loadMoreAdapter = new NormalTypeAdapter(historyIds, this);
        else loadMoreAdapter = new MoreTypeAdapter(historyIds, this);
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