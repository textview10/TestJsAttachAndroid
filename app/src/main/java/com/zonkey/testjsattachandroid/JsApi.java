package com.zonkey.testjsattachandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
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

public class JsApi extends BaseJsApi{
    private static JsApi mjs;

    public static BaseJsApi getInstance() {
        if (mjs == null) {
            mjs = new JsApi();
        }
        return mjs;
    }


}
