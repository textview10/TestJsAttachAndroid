package com.test.testjsattachandroid.view;

import android.content.Context;
import android.view.MotionEvent;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by xu.wang
 * Date on  2018/2/22 16:39:26.
 *
 * @Desc
 */

public class DispatchQqWebView extends WebView {
    public DispatchQqWebView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
