package com.test.testjsattachandroid.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.test.testjsattachandroid.R;
import com.test.testjsattachandroid.api.js.TestJsApi;
import com.test.testjsattachandroid.view.DispatchQqWebView;

/**
 * Created by xu.wang
 * Date on  2018/1/2 09:47:30.
 *
 * @Desc
 */

public class QQWebViewActivity extends BaseWebViewActivity {
    private static final String TAG = "QQWebViewActivity";
    private DispatchQqWebView mQQWebView;

    @Override
    public ViewGroup createWeView() {
        mQQWebView = new DispatchQqWebView(this);
        initData();
        return mQQWebView;
    }

    private void initData() {
        mQQWebView.getSettings().setJavaScriptEnabled(true);
        mQQWebView.loadUrl(url);
        mQQWebView.addJavascriptInterface(jsApi, PROJECT_NAME);
        mQQWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, consoleMessage.message() + "");
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                if (newProgress == 100) {
                    mSmartRefreshLayout.finishRefresh(true);
                    showWebView();
                }
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(QQWebViewActivity.this);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                if (QQWebViewActivity.this.isFinishing()) {
                    return super.onJsAlert(webView, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(QQWebViewActivity.this);
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
                if (QQWebViewActivity.this.isFinishing()) {
                    return super.onJsConfirm(view, url, message, result);
                }
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final com.tencent.smtt.export.external.interfaces.JsPromptResult result) {
                final View v = LayoutInflater.from(QQWebViewActivity.this).inflate(R.layout.dialog_prompt, null);
                ((TextView) v.findViewById(R.id.tv_prompt_content)).setText(message);
                ((EditText) v.findViewById(R.id.et_prompt)).setText(defaultValue);
                AlertDialog.Builder b = new AlertDialog.Builder(QQWebViewActivity.this);
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
                if (QQWebViewActivity.this.isFinishing()) {
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
            case TestJsApi.REQUEST_CODE:
                mQQWebView.loadUrl("javascript:showFromNative()");
                mQQWebView.loadUrl("javascript: alertJs()");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String script = "getState(" + 1 + ")";
                    mQQWebView.evaluateJavascript(script, new ValueCallback<String>() {
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
        mQQWebView.loadUrl(url);
    }


}
