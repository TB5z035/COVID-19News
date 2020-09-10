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
import com.java.zhangjiayou.adapter.HistoryViewAdapter;

import java.util.HashSet;
import java.util.Set;

public class HistoryFragment extends Fragment {
    private SearchView searchView;
    private Set<String> historyIds;
    private RecyclerView recyclerView;
    private HistoryViewAdapter historyViewAdapter;
    private boolean searchEnable = false;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setSearch() {
        searchEnable = true;
        searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("History & Search");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("History & Search");
        historyIds = getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        System.out.println("I'm here#1" + historyIds);
        recyclerView = root.findViewById(R.id.history_view);
        historyViewAdapter = new HistoryViewAdapter(historyIds, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(historyViewAdapter);
        historyViewAdapter.refreshDataList();


//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
//            @Override
//            public void onLoadMore() {
////                historyViewAdapter.setLoadState(historyViewAdapter.LOADING);
//                historyViewAdapter.refreshDataList();
//            }
//        });


        searchView = root.findViewById(R.id.search_box);

        CardView searchBoxCard = root.findViewById(R.id.search_card_view);
        searchBoxCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((MainActivity) HistoryFragment.this.getActivity()).searchEnable)
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not ready. Please wait for some time.", Snackbar.LENGTH_SHORT).show();
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((MainActivity) HistoryFragment.this.getActivity()).searchEnable) {
                    searchView.setIconified(true);
                    Snackbar.make(HistoryFragment.this.recyclerView, "Search is not ready. Please wait for some time.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

//        searchView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (!((MainActivity) HistoryFragment.this.getActivity()).searchEnable)
//                    Snackbar.make(v, "Search is not ready. Please wait for some time.", Snackbar.LENGTH_SHORT);
////                else
////                    super.onClick(v);
//            }
//        });
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//        });


//        searchView.setVisibility(((MainActivity) getActivity()).searchEnable ? View.VISIBLE : View.GONE);
//        searchBoxCard.setVisibility(((MainActivity) getActivity()).searchEnable ? View.VISIBLE : View.GONE);
//        CardView noSearchCard = root.findViewById(R.id.search_not_available_card_view);
//        noSearchCard.setVisibility(((MainActivity) getActivity()).searchEnable ? View.GONE : View.VISIBLE);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent params = new Intent(getActivity(), SearchableActivity.class);
//                params.setAction(Intent.ACTION_SEARCH);
//                params.putExtra(SearchManager.QUERY, query);
//                startActivity(params);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });


        return root;
    }


}