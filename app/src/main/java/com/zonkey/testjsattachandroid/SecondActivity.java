package com.zonkey.testjsattachandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

/**
 * Created by xu.wang
 * Date on  2017/12/15 09:11:38.
 *
 * @Desc
 */

public class SecondActivity extends AppCompatActivity {

    private AppCompatButton btn_second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initialView();
    }

    private void initialView() {
        btn_second = (AppCompatButton) findViewById(R.id.btn_second);
        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(JsApi.REQUEST_CODE);
                finish();
            }
        });
    }
}
