package com.java.zhangjiayou.ui.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.SearchableActivity;

import java.util.HashSet;
import java.util.Set;

public class ExploreFragment extends Fragment {
    private SearchView searchView;
    private Set<String> historyIds;
    private RecyclerView recyclerView;
    private HistoryViewAdapter loadMoreAdapter;
    private ExploreViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        dashboardViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        historyIds = getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        System.out.println(historyIds);
        recyclerView = root.findViewById(R.id.history_view);

        loadMoreAdapter = new HistoryViewAdapter(historyIds, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreAdapter);
        loadMoreAdapter.refreshDataList();

        searchView = root.findViewById(R.id.search_box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent params = new Intent(getActivity(), SearchableActivity.class);
                params.setAction(Intent.ACTION_SEARCH);
                params.putExtra("query", query);
                startActivity(params);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return root;
    }


}