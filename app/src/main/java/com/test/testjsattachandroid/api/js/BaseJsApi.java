package com.test.testjsattachandroid.api.js;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.test.testjsattachandroid.SecondActivity;
import com.test.testjsattachandroid.ui.activity.BaseWebViewActivity;

/**
 * Created by xu.wang
 * Date on  2017/12/27 13:51:07.
 *
 * @Desc
 */

public class BaseJsApi {
    public static final int REQUEST_CODE = 1111;
    public BaseWebViewActivity mActivity;
    public String TAG = "TestJsApi";

    public void initial(BaseWebViewActivity activity) {
        if (activity != null) {
            mActivity = activity;
        }
    }

    //设置刷新状态
    @JavascriptInterface
    public void setLoading(final boolean isLoading) {
        Log.e(TAG, "isLoading = " + isLoading);
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivity.setLoading(isLoading);
                }
            });
        } else {
            mActivity.setLoading(isLoading);
        }

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
        Log.e(TAG, "showWebView");
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

    @JavascriptInterface
    public void setTitle(final String title) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.setWebViewTitle(title);
            }
        });
    }

}
