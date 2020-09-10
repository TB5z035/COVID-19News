package com.java.zhangjiayou.ui.explore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.ui.explore.htmlgenerator.HtmlGenerator;
import com.java.zhangjiayou.ui.explore.utils.AssetsIO;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewerFragment extends Fragment {
    String TAG = "WebViewFrag";
    private final static String ID_KEY = "id_key";
    private int id = -1;
    private WebView webView;
    private AppCompatActivity parentActivity;
    private String title;
    private String jsonString = "";
    String currentWebContent = "[正文]";
    private static int nowId;

    private FloatingActionButton floatingActionButton;

    public WebViewerFragment() {
        // Required empty public constructor
    }

    public static WebViewerFragment newInstance(int id) {
        WebViewerFragment fragment = new WebViewerFragment();
        Bundle args = new Bundle();
        args.putInt(ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID_KEY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化浏览器
        View v = inflater.inflate(R.layout.fragment_viewer, container, false);
        initFab(v);
        initWebView(v);
        updateWebView();
        return v;
    }

    // attach时获取父activity, 方便改标题
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
        ((BackPressedHandlerMain)parentActivity).setBackPressedHandler(() -> {
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }else  {
                return false;
            }
        });
    }

    // 更新父activity
    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity = null;
    }

    // 初始化浏览器
    private void initWebView(View v){
        // 获取webview
        webView = v.findViewById(R.id.web_view);
        // 加载设置
        WebSettings settings = webView.getSettings();
        // 启用js
        settings.setJavaScriptEnabled(true);
        // 支持缓存
        settings.setDomStorageEnabled(true);

        // 防止打开默认app(比如默认浏览器), 遇到特殊url才打开相应app(如thunder)
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( URLUtil.isNetworkUrl(url) || Uri.parse(url).getScheme().equals("file")) {
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 储存标题
                title = view.getTitle();
                Log.d(TAG, "onPageFinished: "+title);
                if (nowId == id) {
                    Log.d(TAG, "onPageFinished: set title");
                    ActionBar actionBar = parentActivity.getSupportActionBar();
                    if (actionBar != null)
                        parentActivity.getSupportActionBar().setTitle(title);
                }
            }
        });
    }

    // 设置json
    public void setJsonString(final String json){
        jsonString = json;
    }

    // 初始化网页内容
    public void updateWebView(){
        webView.clearCache(true);
        webView.clearView();
        webView.reload();

        if (id == 0){ // 中国折线图
            String html = AssetsIO.getFromAssets(parentActivity, "template/history_china.html");
            loadData(html);
        } else if (id == 1){ // 世界折线图
            String html = AssetsIO.getFromAssets(parentActivity, "template/history_world.html");
            loadData(html);
        } else if (id == 2){ // 世界热力图
            String html = AssetsIO.getFromAssets(parentActivity, "template/worldmap.html");
            loadData(html);
        } else if (id == 3){ // 知识图谱
            String html = AssetsIO.getFromAssets(parentActivity, "template/kg.html");
            loadData(html);
        } else if (id == 4) { // 新闻聚类
            String html = AssetsIO.getFromAssets(parentActivity, "template/cluster.html");
            String json = AssetsIO.getFromAssets(parentActivity, "json/cluster.min.json");
            html = HtmlGenerator.generateWithJson(json, html);
            loadData(html);
        } else if (id == 5) { // 知疫学者
            String html = AssetsIO.getFromAssets(parentActivity, "template/scholar-profile/scholar-profile.html");
            webView.loadDataWithBaseURL("file:///android_asset/template/scholar-profile/scholar-profile.html",
                    html,"text/html", "utf-8", null);
        } else if (id == -1) {
            String template = AssetsIO.getFromAssets(parentActivity, "template/news-details/index.html");
            String html = HtmlGenerator.generateWithJson(jsonString, template);
            webView.loadDataWithBaseURL("file:///android_asset/template/news-details/index.html",
                    html,"text/html", "utf-8", null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nowId == id) {
            nowId = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            nowId = id;
            Log.d(TAG, "onPageFinished: set title 2");
            parentActivity.getSupportActionBar().setTitle(title);
        }
    }

    // 把字符串解读为html
    public void loadData(final String html) {
        webView.loadDataWithBaseURL("file:///android_asset/template/worldmap-example.html",
                html,"text/html", "utf-8", null);
    }

    // 从给定url中加载网页, 可以是本地网页, 也可以是互联网上的网页
    public void loadUrl(final String url){
        webView.loadUrl(url);
    }

    private  void initFab(final View v) {

        // 获取fab(分享按钮)
        floatingActionButton = v.findViewById(R.id.fab);
        // 不是网页详情页就不显示, 因为没有jsonString
        if (id != -1) {
            floatingActionButton.hide();
            return;
        }

        floatingActionButton.setOnClickListener(view -> {
            String content = "内容";
            try {
                JSONObject obj = new JSONObject(jsonString);
                content = obj.getString("content");
            } catch (Exception e){
                Log.e(TAG, "initFab: Corrupted json");
            }
            String text = "标题: " + title + ". 摘要: " + content.substring(0, Math.min(100, content.length()));
            new SharePortWeibo().setText(text).share();
        });
    }
}