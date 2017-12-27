package com.zonkey.testjsattachandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;
import com.zonkey.testjsattachandroid.view.ScrollSwipeRefreshLayout;

/**
 * Created by xu.wang
 * Date on  2017/12/27 15:49:58.
 *
 * @Desc
 */

public class QQx5Activity extends BaseWebViewActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "QQx5Activity";
    private String testUrl = "file:///android_asset/zonkey/testjs.html";
    private final static String PROJECT_NAME = "zkzs";
    private TextView tv_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_x5);
        initialView();
        initData();
    }

    private void initialView() {
        mQQWebView = (WebView) findViewById(R.id.wv_qq_x5);
        tv_title = (TextView) findViewById(R.id.tv_qqx5_title);
        mSwipeRefreshLayout = (ScrollSwipeRefreshLayout) findViewById(R.id.sfl_qq_x5);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_qqx5_nodata);
        tv_version = (TextView) findViewById(R.id.tv_qqx5_version);
    }

    private void initData() {
        tv_version.setText("QQx5内核");
        JsApi.getInstance().initial(this);
        mQQWebView.getSettings().setJavaScriptEnabled(true);
        mQQWebView.loadUrl(testUrl);
        mQQWebView.addJavascriptInterface(JsApi.getInstance(), PROJECT_NAME);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.RED);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setViewGroup(mQQWebView);
        mSwipeRefreshLayout.setRefreshing(true);
        mQQWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                Log.e(TAG, "progress = " + newProgress);
                if (newProgress == 100) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showWebView();
                }
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(QQx5Activity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                if (QQx5Activity.this.isFinishing()) {
                    return super.onJsAlert(webView, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(QQx5Activity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                if (QQx5Activity.this.isFinishing()) {
                    return super.onJsConfirm(view, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final com.tencent.smtt.export.external.interfaces.JsPromptResult result) {
                final View v = LayoutInflater.from(QQx5Activity.this).inflate(R.layout.prompt_dialog, null);
                ((TextView) v.findViewById(R.id.prompt_message_text)).setText(message);
                ((EditText) v.findViewById(R.id.prompt_input_field)).setText(defaultValue);
                AlertDialog.Builder b = new AlertDialog.Builder(QQx5Activity.this);
                b.setTitle("提示");
                b.setView(v);
                b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = ((EditText) v.findViewById(R.id.prompt_input_field)).getText().toString();
                        result.confirm(value);
                    }
                });
                b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                if (QQx5Activity.this.isFinishing()) {
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }
                b.create().show();
                return true;
            }

        });
        mQQWebView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.e(TAG, "onReceivedError");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                Log.e(TAG, "shouldOverrideUrlLoading");
                return super.shouldOverrideUrlLoading(webView, s);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case JsApi.REQUEST_CODE:
                mQQWebView.loadUrl("javascript:showFromNative()");
                mQQWebView.loadUrl("javascript: alertJs()");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String script = "getState(" + 1 + ")";
                    mQQWebView.evaluateJavascript(script, new com.tencent.smtt.sdk.ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("WebViewActivity", "" + value);
                        }
                    });
                } else {
//                    wv_test.loadUrl("javascript:getState()");
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        mQQWebView.loadUrl(testUrl);
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
