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
 */

public class JsApi {
    public static final int REQUEST_CODE = 1111;
    private static JsApi mjs;
    private Activity mActivity;
    private WebView webView;

    public static JsApi getInctance() {
        if (mjs == null) {
            mjs = new JsApi();
        }
        return mjs;
    }

    public void setActivity(Activity activity) {
        if (activity != null) {
            mActivity = activity;
        }
    }

    @JavascriptInterface
    public void huoqu() { // 设置"选择文件"的动作
        Toast.makeText(mActivity, "show", Toast.LENGTH_SHORT).show();
        MainActivity mainActivity = (MainActivity) mActivity;
    }

    @JavascriptInterface
    public void toAnotherActivity() {
        Intent intent = new Intent(mActivity, SecondActivity.class);
        mActivity.startActivityForResult(intent, REQUEST_CODE);
    }

}
