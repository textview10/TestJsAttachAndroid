package com.zonkey.testjsattachandroid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;
import com.zonkey.testjsattachandroid.view.ScrollSwipeRefreshLayout;

/**
 * Created by xu.wang
 * Date on  2017/12/27 16:25:41.
 *
 * @Desc
 */

public class BaseWebViewActivity extends AppCompatActivity {
    public WebView mQQWebView;    //QQx5浏览器
    public android.webkit.WebView mWebView; //原生WebKit
    public TextView tv_title;
    public ScrollSwipeRefreshLayout mSwipeRefreshLayout;
    public RelativeLayout rl_nodata;

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
        if (mWebView != null) {
            if (mWebView.getVisibility() != View.GONE) {
                mWebView.setVisibility(View.GONE);
            }
        }
        if (mQQWebView != null) {
            if (mQQWebView.getVisibility() != View.GONE) {
                mQQWebView.setVisibility(View.GONE);
            }
        }
        if (rl_nodata.getVisibility() != View.VISIBLE) {
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    public void showWebView() {
        if (mWebView != null) {
            if (mWebView.getVisibility() != View.VISIBLE) {
                mWebView.setVisibility(View.VISIBLE);
            }
        }
        if (mQQWebView != null) {
            if (mQQWebView.getVisibility() != View.VISIBLE) {
                mQQWebView.setVisibility(View.VISIBLE);
            }
        }
        if (rl_nodata.getVisibility() != View.GONE) {
            rl_nodata.setVisibility(View.GONE);
        }
    }

    public void setWebViewTitle(String msg) {
        tv_title.setText(msg);
    }

    public void showConfirm(final String message, final JsResult result) {

    }
}
