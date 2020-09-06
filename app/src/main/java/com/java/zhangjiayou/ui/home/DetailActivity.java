package com.java.zhangjiayou.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.java.zhangjiayou.ui.home.htmlgenerator.HtmlGenerator;
import com.java.zhangjiayou.ui.home.utils.AssetsIO;
import com.java.zhangjiayou.ui.home.utils.COVIDData;
import com.java.zhangjiayou.ui.home.utils.FileIO;
import com.java.zhangjiayou.ui.home.utils.WXRegister;
import com.java.zhangjiayou.ui.home.utils.WXShare;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.java.zhangjiayou.R;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    // 微信分享需要APP ID
    private static final String WX_APP_ID= "wx88888888";
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI iwxapi;
    // webView是浏览器
    private WebView webView;
    // statusView是状态栏
    private TextView statusView;
    // currentWebContent携带当前网页的内容
    private String currentWebContent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            // init statusView
            initStatusView();

            // prepare webView
            initWebView();

            // register to WX
//            iwxapi = WXRegister.regToWx(this);

            // prepare fab
            initFab(iwxapi);

            /* debug */
            try {
                debugStart();
            } catch (Exception e){
                e.printStackTrace();
            }

            /* end debug */

        }
    }

    // 使用一个网页进行debug
    private void debugStart() throws JSONException {
        /*
         * 测试loadUrl
         * */
//        String url = "file:///android_asset/web/index.html";
//        String url = "file:///android_asset/sigma.js-1.2.1/examples/events.html";
//        String url = "file:///android_asset/template/worldmap-example.html";
//        String url = "https://www.baidu.com/";
//        loadUrl(url);

        /*
         * 测试无格式新闻页
         * */
//        String template = AssetsIO.getFromAssets(this, "template/plaintext.html");
//        String unencodedHtml = HtmlGenerator.generatePlainText("Title", "Text", "没有摘要", "没有来源", "今天", template);
//        loadData(unencodedHtml);

        /*
         * 测试疫情统计
         * */
        String template = AssetsIO.getFromAssets(this, "template/worldmap.html");
//        String template = AssetsIO.getFromAssets(this, "template/historyplot_with_all.html");
//        String template = AssetsIO.getFromAssets(this, "template/historyplot_with_china.html");
//        String template = AssetsIO.getFromAssets(this, "template/historyplot_with_countries.html");
        String json = AssetsIO.getFromAssets(this, "json/epidemic.json");
        String unencodedHtml = HtmlGenerator.generateWithJson(json, template);
        loadData(unencodedHtml);


    }

    // 初始化状态栏
    private void initStatusView(){
        statusView = (TextView) findViewById(R.id.status);
        statusView.bringToFront();
        showOnStatus("");
    }

    // 展示信息, 主要是错误信息
    public void showOnStatus(final String info){
        statusView.setText(info);
        statusView.postDelayed(new Runnable() {
            @Override
            public void run() {
                statusView.setText("");
            }
        }, 5000);
    }

    // 初始化浏览器
    private void initWebView(){
        // 获取webview
        webView = (WebView) findViewById(R.id.web_view);
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
                if( URLUtil.isNetworkUrl(url) ) {
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 更新action bar的标题
//                DetailActivity.this.getSupportActionBar().setTitle(view.getTitle());
                // 获取html的内容
                view.evaluateJavascript(
                        //"(function() { return (document.getElementsByClassName('details')[0].innerHTML); })();",
                        //"(function() { return (document.body.innerHTML); })();",
                        "(function() { return (document.getElementsByName('description')[0].content); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(final String abstra) {
                                currentWebContent = abstra.substring(1, abstra.length()-1); // 丢弃自带的引号
                                currentWebContent = Html.fromHtml(currentWebContent).toString();
                                currentWebContent = currentWebContent == null? "" : currentWebContent;
                                System.out.println(currentWebContent);
                            }
                        });

            }
        });
    }

    // 初始化分享按钮
    private  void initFab(final IWXAPI api){
        // 获取fab(分享按钮)
        FloatingActionButton fab = findViewById(R.id.fab);
        // 修改点击行为为分享给微信好友
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String abstra = currentWebContent.length() > 100 ? currentWebContent.substring(0, 100): currentWebContent;
                String text = WXShare.generateShareText(webView.getUrl(), webView.getTitle(), abstra);
//                WXShare.share(api, text); ///// TODO
            }
        });
    }

    // 修改返回按键行为, 优先返回到上一页, 然后才是退出
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else  {
            super.onBackPressed();
        }
    }

    // 把字符串解读为html
    public void loadData(final String unencodedHtml){
//        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
//        webView.loadData(encodedHtml, "text/html", "base64");
        webView.loadDataWithBaseURL("file:///android_asset/template/worldmap-example.html", unencodedHtml,"text/html", "utf-8", null);
    }

    // 从给定url中加载网页, 可以是本地网页, 也可以是互联网上的网页
    public void loadUrl(final String url){
        webView.loadUrl(url);
    }
}

