package com.java.zhangjiayou;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.share.WbShareCallback;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        getActionBar().hide();
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        PassageDatabase.getInstance(this);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // TODO Auto-generated method stub
//        super.onConfigurationChanged(newConfig);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Callback success.");
        if (SharePortWeibo.getAPI() != null)
            SharePortWeibo.getAPI().doResultIntent(data, new WbShareCallback() {
                @Override
                public void onComplete() {
                    Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "分享取消", Toast.LENGTH_SHORT).show();
                }
            });
    }
}