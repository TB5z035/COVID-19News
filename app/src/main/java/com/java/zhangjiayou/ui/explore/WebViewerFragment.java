package com.java.zhangjiayou.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.ui.explore.htmlgenerator.HtmlGenerator;
import com.java.zhangjiayou.ui.explore.utils.AssetsIO;

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
    private static String oldTitle;
    private String title;
    private String jsonString = "";
    private static int nowId;

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
        initWebView(v);
        updateWebView();
        initFab(v, null);
        return v;
    }

    // attach时获取父activity, 方便改标题
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
        ((BackPressedHandlerMain)parentActivity).setBackPressedHandler(new BackPressedHandlerSub() {
            @Override
            public boolean onBackPressed() {
                if (webView.canGoBack()){
                    webView.goBack();
                    return true;
                }else  {
                    return false;
                }
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
        webView = (WebView) v.findViewById(R.id.web_view);
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
                // 储存标题
                title = view.getTitle();
                if (nowId == id && isVisible()){
                    parentActivity.getSupportActionBar().setTitle(title);
                }

                // 异步获取新闻正文
                // TODO: 得到正文后, 把正文放在成员变量中
                getContextDelayed(view);
            }

            // 获取html的内容
            public void getContextDelayed(WebView view){
//                view.evaluateJavascript(
//                        //"(function() { return (document.getElementsByClassName('details')[0].innerHTML); })();",
//                        //"(function() { return (document.body.innerHTML); })();",
//                        "(function() { return (document.getElementsByName('description')[0].content); })();",
//                        new ValueCallback<String>() {
//                            @Override
//                            public void onReceiveValue(final String abstra) {
//                                currentWebContent = abstra.substring(1, abstra.length()-1); // 丢弃自带的引号
//                                currentWebContent = Html.fromHtml(currentWebContent).toString();
//                                currentWebContent = currentWebContent == null? "" : currentWebContent;
//                                System.out.println(currentWebContent);
//                            }
//                        });
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
            loadUrl("https://www.baidu.com");
        } else if (id == 5) { // 知疫学者
            loadUrl("https://www.bing.com");
        } else if (id == -1) {
            String template = AssetsIO.getFromAssets(parentActivity, "template/plaintext.html");
            String html = HtmlGenerator.generateWithJson(jsonString, template);
            loadData(html);
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

    private  void initFab(final View v, final String api){

        // 获取fab(分享按钮)
        FloatingActionButton fab = v.findViewById(R.id.fab);
        // 不是网页详情页就不显示, 因为没有jsonString
        if (id != -1){
            fab.hide();
            return;
        }
        // 修改点击行为为分享给微信好友
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: FAB not initialized!");
            }
        });
    }
}