package com.java.zhangjiayou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.ui.explore.WebViewerFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String rawJSON = intent.getStringExtra("rawJSON");
        int id = intent.getIntExtra("id", -1);

        WebViewerFragment webViewerFragment = (WebViewerFragment) getSupportFragmentManager().findFragmentById(R.id.debug_webview);
        webViewerFragment.setJsonString(rawJSON);
        webViewerFragment.updateWebView();



//        WebViewerFragment webViewerFragment = WebViewerFragment.newInstance(-1);
////        webViewerFragment.setJsonString(rawJSON);
////        webViewerFragment.updateWebView();
//        getSupportFragmentManager().beginTransaction().add(R.id.detail_frame, webViewerFragment).commit();

    }
}