package com.test.testjsattachandroid.ui.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.test.testjsattachandroid.R;
import com.test.testjsattachandroid.api.js.TestJsApi;

/**
 * Created by xu.wang
 * Date on  2018/1/2 09:32:13.
 *
 * @Desc 原生WebView
 */

public class WebViewActivity extends BaseWebViewActivity {
    private final static String TAG = "WebViewActivity";
    private WebView mWebView;

    @Override
    public ViewGroup createWeView() {
        mWebView = new WebView(this);
        initData();
        return mWebView;
    }

    private void initData() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(jsApi, PROJECT_NAME);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mSmartRefreshLayout.finishRefresh(false);
                    showWebView();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                if (WebViewActivity.this.isFinishing()) {
                    return super.onJsAlert(view, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
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
                if (WebViewActivity.this.isFinishing()) {
                    return super.onJsConfirm(view, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                if (showPromptDialog(message, defaultValue, result)) {
                    return true;
                } else {
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
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

    private boolean showPromptDialog(String message, String defaultValue, final JsPromptResult result) {
        final View v = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.dialog_prompt, null);
        ((TextView) v.findViewById(R.id.tv_prompt_content)).setText(message);
        ((EditText) v.findViewById(R.id.et_prompt)).setText(defaultValue);
        AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
        b.setTitle("提示");
        b.setView(v);
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = ((EditText) v.findViewById(R.id.et_prompt)).getText().toString();
                result.confirm(value);
            }
        });
        b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });
        if (WebViewActivity.this.isFinishing()) {
            return false;
        }
        b.create().show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TestJsApi.REQUEST_CODE:
                mWebView.loadUrl("javascript:showFromNative()");
                mWebView.loadUrl("javascript: alertJs()");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String script = "getState(" + 1 + ")";
                    mWebView.evaluateJavascript(script, new ValueCallback<String>() {
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
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        mWebView.loadUrl(url);
    }

}
