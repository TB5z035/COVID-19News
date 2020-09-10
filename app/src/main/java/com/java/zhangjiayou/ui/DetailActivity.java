package com.java.zhangjiayou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerMain;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerSub;
import com.java.zhangjiayou.ui.explore.WebViewerFragment;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.share.WbShareCallback;

public class DetailActivity extends AppCompatActivity implements BackPressedHandlerMain {
    private BackPressedHandlerSub backPressedHandler;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Detail");

        Intent intent = getIntent();
        String rawJSON = intent.getStringExtra("rawJSON");

        WebViewerFragment webViewerFragment = (WebViewerFragment) getSupportFragmentManager().findFragmentById(R.id.debug_webview);
        webViewerFragment.setJsonString(rawJSON);
        webViewerFragment.updateWebView();

        constraintLayout = findViewById(R.id.detail_constraint_layout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharePortWeibo.getAPI().authorizeCallback(requestCode, resultCode, data);
    }

    @Override
    public void setBackPressedHandler(BackPressedHandlerSub backPressedHandler) {
        this.backPressedHandler = backPressedHandler;
    }

    @Override
    public void superOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (backPressedHandler == null || backPressedHandler.onBackPressed() == false) {
            superOnBackPressed();
        }
    }
}