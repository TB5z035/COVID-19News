package com.java.zhangjiayou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.search.SearchMapManager;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerMain;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerSub;
import com.java.zhangjiayou.util.PassageWithNoContent;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements  BackPressedHandlerMain{
    private HashMap<String, HashSet<String>> searchMap;
    private View backup;
    private BackPressedHandlerSub backPressedHandler;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharePortWeibo.initSDK(this);

        //TODO:Set the title bar

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Start loading!", Toast.LENGTH_SHORT).show();
                    }
                });
                new PassagePortal().getAllPassageIdTitle((key, val) -> {
                    if (SearchMapManager.getMap().containsKey(key)) {
                        HashSet<PassageWithNoContent> now = SearchMapManager.getMap().get(key);
                        now.add(val);
                        SearchMapManager.getMap().put(key, now);
                    } else {
                        HashSet<PassageWithNoContent> now = new HashSet<>();
                        now.add(val);
                        SearchMapManager.getMap().put(key, now);
                    }
                    return null;
                });
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Finish loading!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 0, 300000);

        //TODO:test availability when updating search map

        backup = findViewById(R.id.nav_host_fragment);
        navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_frame, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //初始化数据库
        PassageDatabase.getInstance(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Callback success.");
        if (SharePortWeibo.getAPI() != null)
            SharePortWeibo.getAPI().doResultIntent(data, new WbShareCallback() {
                @Override
                public void onComplete() {
                    Snackbar.make(backup, "分享成功", Snackbar.LENGTH_SHORT).show();
                }
                @Override
                public void onError(UiError uiError) {
                    Snackbar.make(backup, "分享失败", Snackbar.LENGTH_SHORT).show();

                }
                @Override
                public void onCancel() {
                    Snackbar.make(backup, "分享取消", Snackbar.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void setBackPressedHandler(BackPressedHandlerSub backPressedHandler) {
        this.backPressedHandler = backPressedHandler;
    }

    @Override
    public void superOnBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (backPressedHandler == null || backPressedHandler.onBackPressed() == false) {
            superOnBackPressed();
        }
    }
}