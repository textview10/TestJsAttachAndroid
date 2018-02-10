package com.test.testjsattachandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.test.testjsattachandroid.ui.activity.BaseWebViewActivity;
import com.test.testjsattachandroid.ui.activity.QQWebViewActivity;
import com.test.testjsattachandroid.ui.activity.WebViewActivity;

/**
 * Created by xu.wang
 * Date on  2017/12/27 16:46:42.
 *
 * @Desc
 */

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_webview;
    private AppCompatButton btn_qqx5_webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialView();
    }

    private void initialView() {
        btn_webview = (AppCompatButton) findViewById(R.id.btn_webview);
        btn_qqx5_webview = (AppCompatButton) findViewById(R.id.btn_qqx5_webview);

        btn_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(BaseWebViewActivity.TAG_TYPE,2);
                startActivity(intent);
            }
        });

        btn_qqx5_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QQWebViewActivity.class);
                intent.putExtra(BaseWebViewActivity.TAG_TYPE,2);
                startActivity(intent);
            }
        });
    }
}
