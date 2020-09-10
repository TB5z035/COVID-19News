package com.java.zhangjiayou.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.adapter.SearchResultAdapter;

public class SearchableActivity extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;

    private SearchResultAdapter searchResultAdapter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        recyclerView = findViewById(R.id.search_result);

        searchResultAdapter = new SearchResultAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchResultAdapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            searchResultAdapter.refreshDataList(query);
        }

        getSupportActionBar().setTitle("“" + query + "”" + "的搜索结果");
    }
}