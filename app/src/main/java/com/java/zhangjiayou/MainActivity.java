package com.java.zhangjiayou;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.search.SearchMapManager;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerMain;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerSub;
import com.java.zhangjiayou.util.NetworkChecker;
import com.java.zhangjiayou.util.PassageWithNoContent;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements  BackPressedHandlerMain{
    private BackPressedHandlerSub backPressedHandler;
    public boolean searchEnable = false;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharePortWeibo.initSDK(this);

        navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_frame, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (!NetworkChecker.isNetworkConnected(MainActivity.this)) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
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
                } catch (Exception ignored) {
                }
                searchEnable = true;
            }
        }, 0, 300000);

        //初始化数据库
        PassageDatabase.getInstance(this);
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