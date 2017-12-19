package com.zonkey.testjsattachandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.zonkey.testjsattachandroid.view.ScrollSwipeRefreshLayout;

/**
 * Created by xu.wang
 * Date on 2017/5/22 13:48
 */
public class WebViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private WebView wv_test;
    private ScrollSwipeRefreshLayout mSwipeRefreshLayout;
    private String url = "http://www.baidu.com";
    private String testUrl = "file:///android_asset/zonkey/testjs.html";
    private final static String TAG = "WebViewActivity";
    private final static String PROJECT_NAME = "zkzs";
    private RelativeLayout rl_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wv_test = (WebView) findViewById(R.id.wv_test);
        mSwipeRefreshLayout = (ScrollSwipeRefreshLayout) findViewById(R.id.scroll_swipe_refresh_layout);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        JsApi.getInctance().initial(this);
        initData();
    }

    private void initData() {
        wv_test.getSettings().setJavaScriptEnabled(true);
        wv_test.loadUrl(testUrl);
        wv_test.addJavascriptInterface(JsApi.getInctance(), PROJECT_NAME);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.RED);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setViewGroup(wv_test);
        mSwipeRefreshLayout.setRefreshing(true);
        wv_test.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e(TAG, "progress = " + newProgress);
                if (newProgress == 100) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showWebView();
                }
            }
        });
        wv_test.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "onReceivedError");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e(TAG, "shouldOverrideUrlLoading");
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case JsApi.REQUEST_CODE:
                wv_test.loadUrl("javascript:showFromNative()");
                break;
        }
    }

    @Override
    public void onRefresh() {
        wv_test.loadUrl(testUrl);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void setLoading(final boolean isRefreshing) {
        if (isRefreshing) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public void showEmptyView() {
        if (wv_test.getVisibility() != View.GONE) {
            wv_test.setVisibility(View.GONE);
        }
        if (rl_nodata.getVisibility() != View.VISIBLE) {
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    public void showWebView() {
        if (wv_test.getVisibility() != View.VISIBLE) {
            wv_test.setVisibility(View.VISIBLE);
        }
        if (rl_nodata.getVisibility() != View.GONE) {
            rl_nodata.setVisibility(View.GONE);
        }
    }
}
