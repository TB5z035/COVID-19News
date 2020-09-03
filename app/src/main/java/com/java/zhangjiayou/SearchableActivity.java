package com.java.zhangjiayou;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SearchableActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_searchable);
        textView = findViewById(R.id.debug_textVIew);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            doMySearch(query);
        }

    }

    void doMySearch(String query) {
        textView.setText(query+"的搜索结果");
        Toast.makeText(textView.getContext(),query,Toast.LENGTH_SHORT).show();
    }
}