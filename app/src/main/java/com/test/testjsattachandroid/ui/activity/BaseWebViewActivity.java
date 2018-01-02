package com.test.testjsattachandroid.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.testjsattachandroid.api.js.BaseJsApi;
import com.test.testjsattachandroid.R;
import com.test.testjsattachandroid.api.js.TestJsApi;
import com.test.testjsattachandroid.view.ScrollSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.wang
 * Date on  2018/1/2 09:27:01.
 *
 * @Desc
 */

public abstract class BaseWebViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BaseWebViewActivity";
    public static final String TAG_TYPE = "WEB_CONTENT_TYPE";
    public static final String PROJECT_NAME = "wise_class";    //项目名称,用于Js与原生通讯
    //WebView 类型 ---------------
    public static final String TYPE_WEB_VIEW = "type_web_view";
    public static final int NATIVE_WEBVIEW = 1; //原生WebView
    public static final int QQ_X5_WEBVIEW = 2;  //QQ_X5 WebView;

    @BindView(R.id.tv_webview_title)
    public TextView tv_title;
    @BindView(R.id.ssrl_webview)
    public ScrollSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rl_webview_nodata)
    public RelativeLayout rl_nodata;
    @BindView(R.id.tv_webview_type)
    public TextView tv_type;
    public String url;
    public BaseJsApi jsApi;
    public int type = 0;    //0是原生WebView ,1,是QQ x5浏览器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initialWebDetail();
        initialData();
    }

    private void initialWebDetail() {
        int type = getIntent().getIntExtra(TAG_TYPE, -1);
        switch (type) {
            case 1:
                if (TextUtils.isEmpty(url) || jsApi == null) {
                    Log.e(TAG, "Exception = do not have this type ...");
                    return;
                }
                break;
            default:
                url = "file:///android_asset/test/testjs.html";
                jsApi = new TestJsApi();
                jsApi.initial(this);
                break;
        }
    }

    private void initialData() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.RED, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        ViewGroup viewGroup = createWeView();
        mSwipeRefreshLayout.addView(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefreshLayout.setViewGroup(viewGroup);
        if (viewGroup instanceof com.tencent.smtt.sdk.WebView) {  //是QQ浏览器
            tv_type.setText("QQ x5");
            type = 1;
        } else if (viewGroup instanceof WebView) {    //是native浏览器
            tv_type.setText("WebView");
            type = 0;
        }
    }


    public abstract ViewGroup createWeView();

    public void setLoading(final boolean isRefreshing) {
        Log.e(TAG, "show loading() ");
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
        Log.e(TAG, "showEmptyView");
        View child = mSwipeRefreshLayout.getChildAt(0);
        if (child != null) {
            if (child.getVisibility() != View.GONE) {
                child.setVisibility(View.GONE);
            }
        }
        if (rl_nodata.getVisibility() != View.VISIBLE) {
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    public void showWebView() {
        Log.e(TAG, "showWebView");
        View child = mSwipeRefreshLayout.getChildAt(0);
        if (child != null) {
            if (child.getVisibility() != View.VISIBLE) {
                child.setVisibility(View.VISIBLE);
            }
        }
        if (rl_nodata.getVisibility() != View.GONE) {
            rl_nodata.setVisibility(View.GONE);
        }
    }

    public void setWebViewTitle(String msg) {
        tv_title.setText(msg);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TestJsApi.REQUEST_CODE:
//                mQQWebView.loadUrl("javascript:showFromNative()");
//                mQQWebView.loadUrl("javascript: alertJs()");
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    String script = "getState(" + 1 + ")";
//                    mQQWebView.evaluateJavascript(script, new com.tencent.smtt.sdk.ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.e("WebViewActivity", "" + value);
//                        }
//                    });
//                } else {
////                    wv_test.loadUrl("javascript:getState()");
//                }
//                break;
//        }
    }
}
