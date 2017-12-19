package com.zonkey.testjsattachandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by xu.wang
 * Date on 2017/5/22 13:48
 */
public class MainActivity extends AppCompatActivity {
    WebView wv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wv_test = (WebView) findViewById(R.id.wv_test);
        JsApi.getInctance().setActivity(this);
        initData();
    }

    private void initData() {
        wv_test.getSettings().setJavaScriptEnabled(true);
        wv_test.loadUrl("file:///android_asset/zonkey/testjs.html");
        wv_test.addJavascriptInterface(JsApi.getInctance(), "wx");
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
}
