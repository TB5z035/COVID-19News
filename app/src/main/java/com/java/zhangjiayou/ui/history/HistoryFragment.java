package com.java.zhangjiayou.ui.history;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.java.zhangjiayou.MainActivity;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.ui.adapter.HistoryViewAdapter;
import com.java.zhangjiayou.util.NetworkChecker;

import java.util.HashSet;
import java.util.Set;

public class HistoryFragment extends Fragment {
    private SearchView searchView;
    private Set<String> historyIds;
    private RecyclerView recyclerView;
    private HistoryViewAdapter historyViewAdapter;

    @Override
    public void onResume() {
        super.onResume();
        historyViewAdapter.getData();
        historyViewAdapter.notifyDataSetChanged();
        ((MainActivity) requireActivity()).getSupportActionBar().setTitle("History & Search");
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setIconified(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        ((MainActivity) requireActivity()).getSupportActionBar().setTitle("History & Search");

        historyIds = requireActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());

        recyclerView = root.findViewById(R.id.history_view);

        historyViewAdapter = new HistoryViewAdapter(historyIds, requireActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(historyViewAdapter);
        historyViewAdapter.getData();

        searchView = root.findViewById(R.id.search_box);

        CardView searchBoxCard = root.findViewById(R.id.search_card_view);
        searchBoxCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkChecker.isNetworkConnected(requireActivity())) {
                    searchView.setIconified(true);
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not available in offline mode.", Snackbar.LENGTH_SHORT).show();
                } else if (!((MainActivity) HistoryFragment.this.requireActivity()).searchEnable) {
                    searchView.setIconified(true);
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not ready. Please wait for a while.", Snackbar.LENGTH_SHORT).show();
                } else
                    searchView.setIconified(false);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((MainActivity) HistoryFragment.this.requireActivity()).searchEnable) {
                    searchView.setIconified(true);
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not ready. Please wait for a while.", Snackbar.LENGTH_SHORT).show();
                } else if (!NetworkChecker.isNetworkConnected(requireActivity())) {
                    searchView.setIconified(true);
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not available in offline mode.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        return root;
    }
}