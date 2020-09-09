package com.java.zhangjiayou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerMain;
import com.java.zhangjiayou.ui.explore.BackPressedHandlerSub;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiFunction;

public class MainActivity extends AppCompatActivity implements  BackPressedHandlerMain{
    private HashMap<String, HashSet<String>> searchMap;
    private View backup;
    private BackPressedHandlerSub backPressedHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        getActionBar().hide();
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("Timer", "run: ");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Start loading!", Toast.LENGTH_SHORT).show();
                    }
                });
                new PassagePortal().getAllPassageIdTitle(new BiFunction<String, String, Boolean>() {
                    
                    @Override
                    public Boolean apply(String key, String val) {
//                        Set<String> stringSet = new HashSet<>(getApplication().getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
//                                .getStringSet(key, new HashSet<>()));
//                        stringSet.add(val);

//                        HashSet<String> now = SearchMapManager.getMap().getOrDefault(key, new HashSet<>());
                        if (SearchMapManager.getMap().containsKey(key)) {
                            HashSet<String> now = SearchMapManager.getMap().get(key);
                            now.add(val);
                            SearchMapManager.getMap().put(key, now);
                        } else {
                            HashSet<String> now = new HashSet<>();
                            now.add(val);
                            SearchMapManager.getMap().put(key, now);
                        }

//                        getApplication().getSharedPreferences(String.valueOf(R.string.search_seg_id_map_key), Context.MODE_PRIVATE)
//                                .edit().putStringSet(key, stringSet).commit();
                        return null;
                    }
                });
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Finish loading!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 0, 30000);

        backup = findViewById(R.id.nav_host_fragment);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_frame, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
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