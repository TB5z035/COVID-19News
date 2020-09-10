package com.java.zhangjiayou;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        recyclerView = findViewById(R.id.search_result);


        historyViewAdapter = new SearchResultAdapter(searchIds, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historyViewAdapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            getSupportActionBar().setTitle("\"" + query + "\"" + "的搜索结果");
            historyViewAdapter.refreshDataList(query);

//            doMySearch(query);
        }

    }

//     void doMySearch(String query) {
//         textView.setText(query + "的搜索结果");
//         Toast.makeText(textView.getContext(), query, Toast.LENGTH_SHORT).show();
//
//         searchIds = new HashSet<>();
//
//         ToAnalysis.parse(query).forEach((v) -> {
//             searchIds.add(v.getName());
//         });
//
//         textView.setText(searchIds.toString());
//         historyViewAdapter.refreshDataList();
//         historyViewAdapter.notifyDataSetChanged();
//     }
}