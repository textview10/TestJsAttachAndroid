package com.zonkey.testjsattachandroid;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by xu.wang
 * Date on 2017/5/22 13:48
 *
 * @Desc @javascriptInterface好像是在子线程触发的
 */

public class JsApi {
    public static final int REQUEST_CODE = 1111;
    private static JsApi mjs;
    private WebViewActivity mActivity;
    private WebView webView;
    private static final String TAG = "JsApi";

    public static JsApi getInctance() {
        if (mjs == null) {
            mjs = new JsApi();
        }
        return mjs;
    }


    public void initial(WebViewActivity activity) {
        if (activity != null) {
            mActivity = activity;
        }
    }

    //设置刷新状态
    @JavascriptInterface
    public void setLoading(final boolean isLoading) {
        Log.e(TAG, "isLoading = " + isLoading);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.setLoading(isLoading);
            }
        });
    }

    //设置空界面
    @JavascriptInterface
    public void showEmptyView() {
        Log.e(TAG, "showEmptyView ");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.showEmptyView();
            }
        });
    }

    //显示WebView
    @JavascriptInterface
    public void showWebView() {
        Log.e(TAG, "showWebView ");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.showWebView();
            }
        });
    }

    @JavascriptInterface
    public void startActivity(int type) {
        Log.e(TAG, "type = " + type);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, SecondActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

}
