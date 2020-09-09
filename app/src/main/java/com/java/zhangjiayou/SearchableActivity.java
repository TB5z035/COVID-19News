package com.java.zhangjiayou;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.ui.history.HistoryViewAdapter;

import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SearchableActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private SearchResultAdapter historyViewAdapter;
    private Set<String> searchIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        textView = findViewById(R.id.debug_textVIew);
        recyclerView = findViewById(R.id.search_result);



        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            doMySearch(query);
        }
        historyViewAdapter = new SearchResultAdapter(searchIds,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historyViewAdapter);
        historyViewAdapter.refreshDataList();

    }

     void doMySearch(String query) {
        textView.setText(query + "的搜索结果");
        Toast.makeText(textView.getContext(), query, Toast.LENGTH_SHORT).show();
//        Set<String> strings = getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
//                .getStringSet(query, new HashSet<>());
        searchIds = new HashSet<>();

        ToAnalysis.parse(query).forEach((v) -> {
//<<<<<<< Updated upstream
//            Set<String> strings = new HashSet<>(getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
//                    .getStringSet(v.getName(), new HashSet<>()));

            searchIds.add(v.getName());
////=======
//            Set<String> strings = new HashSet<>(getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
//                    .getStringSet(v.getName(), new HashSet<>()));
//            searchIds.addAll(strings);
//>>>>>>> Stashed changes
        });

        textView.setText(searchIds.toString());
    }
}