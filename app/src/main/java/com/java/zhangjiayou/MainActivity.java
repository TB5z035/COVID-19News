package com.java.zhangjiayou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.share.WbShareCallback;

public class MainActivity extends AppCompatActivity {

    private View backup;
    private BackPressedHandler backPressedHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        getActionBar().hide();
        setContentView(R.layout.activity_main);

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

    public void setBackPressedHandler(BackPressedHandler backPressedHandler) {
        this.backPressedHandler = backPressedHandler;
    }

    public void superOnBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (backPressedHandler == null){
            superOnBackPressed();
        } else {
            backPressedHandler.onBackPressed();
        }
    }

    public interface BackPressedHandler {
        void onBackPressed();
    }
}