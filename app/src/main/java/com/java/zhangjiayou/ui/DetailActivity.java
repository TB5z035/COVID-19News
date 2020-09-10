package com.java.zhangjiayou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerMain;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerSub;
import com.java.zhangjiayou.ui.explore.WebViewerFragment;

public class DetailActivity extends AppCompatActivity implements BackPressedHandlerMain {
    private BackPressedHandlerSub backPressedHandler;

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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharePortWeibo.getAPI().authorizeCallback(requestCode, resultCode, data);
    }

}