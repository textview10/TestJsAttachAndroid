package com.test.testjsattachandroid.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.test.testjsattachandroid.api.js.BaseJsApi;
import com.test.testjsattachandroid.R;
import com.test.testjsattachandroid.api.js.TestJsApi;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.wang
 * Date on  2018/1/2 09:27:01.
 *
 * @Desc
 */

public abstract class BaseWebViewActivity extends AppCompatActivity implements OnRefreshListener {
    private static final String TAG = "BaseWebViewActivity";
    public static final String TAG_TYPE = "WEB_CONTENT_TYPE";
    public static final String PROJECT_NAME = "wise_class";    //项目名称,用于Js与原生通讯
    //WebView 类型 ---------------
    public static final String TYPE_WEB_VIEW = "type_web_view";
    public static final int NATIVE_WEBVIEW = 1; //原生WebView
    public static final int QQ_X5_WEBVIEW = 2;  //QQ_X5 WebView;

    @BindView(R.id.tv_webview_back)
    TextView tv_back;
    @BindView(R.id.tv_webview_title)
    public TextView tv_title;
    @BindView(R.id.srl_webview)
    public SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.rl_webview_nodata)
    public RelativeLayout rl_nodata;
    @BindView(R.id.tv_webview_type)
    public TextView tv_type;
    @BindView(R.id.btn_wv_sendmsg)
    AppCompatButton btn_sendMsg;
    @BindView(R.id.et_wv_getmsg)
    AppCompatEditText et_getmsg;
    public String url;
    public BaseJsApi jsApi;
    public int type = 0;    //0是原生WebView ,1,是QQ x5浏览器
    private ViewGroup viewGroup;
    private int type1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initialWebDetail();
        initialData();
//        if (BuildConfig.DEBUG) {
        initialTestData();
//        }
    }

    private void initialTestData() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String script = "javascript:back()";
                if (viewGroup instanceof com.tencent.smtt.sdk.WebView) {
                    ((com.tencent.smtt.sdk.WebView) viewGroup).evaluateJavascript(script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            isBack(s);
                        }
                    });
                } else if (viewGroup instanceof WebView) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((WebView) viewGroup).evaluateJavascript(script, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.e(TAG, "onReceiveValueSuccess = " + s);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getBaseContext(), "WebView类型错误...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewGroup == null) {
                    Toast.makeText(getBaseContext(), "WebView加载失败...", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = et_getmsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(getBaseContext(), "发送内容不能为空...", Toast.LENGTH_SHORT).show();
                    return;
                }
//                msg = "111";
                String script = "javascript:receiveMsgFromNative(\'" + msg + "\')";
                if (viewGroup instanceof com.tencent.smtt.sdk.WebView) {
                    Toast.makeText(getBaseContext(), "QQWebView发送消息 = " + msg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "QQWebView发送消息 script = " + script);
                    ((com.tencent.smtt.sdk.WebView) viewGroup).evaluateJavascript(script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.e(TAG, "onReceiveValueSuccess = " + s);
                        }
                    });
                } else if (viewGroup instanceof WebView) {
                    Toast.makeText(getBaseContext(), "NativeWebView发送消息 = " + msg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "NativeWebView发送消息 script = " + script);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((WebView) viewGroup).evaluateJavascript(script, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.e(TAG, "onReceiveValueSuccess = " + s);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getBaseContext(), "WebView类型错误...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isBack(String s) {
        if (TextUtils.equals(s, "true")) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("提示");
            b.setMessage("是否退出?");
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            if (isFinishing()) {
                return;
            }
            b.create().show();
        }
    }

    private void initialWebDetail() {
        type1 = getIntent().getIntExtra(TAG_TYPE, -1);
        switch (type1) {
            case 1:
                if (TextUtils.isEmpty(url) || jsApi == null) {
                    Log.e(TAG, "Exception = do not have this type ...");
                    return;
                }
                break;
            case 2:
                url = "http://192.168.13.213:8080/wiseclass_evalute?type=teacher&subject=1";
                jsApi = new TestJsApi();
                jsApi.initial(this);
                break;
            default:
                url = "file:///android_asset/test/testjs.html";
                jsApi = new TestJsApi();
                jsApi.initial(this);
                break;
        }
    }

    private void initialData() {
        mSmartRefreshLayout.setDragRate(0.6f);//显示下拉高度/手指真实下拉高度=阻尼效果
        mSmartRefreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
        mSmartRefreshLayout.setHeaderMaxDragRate(3);//最大显示下拉高度/Header标准高度
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setEnableLoadmore(false);
        viewGroup = createWeView();
        mSmartRefreshLayout.addView(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
            mSmartRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSmartRefreshLayout.finishRefresh(true);
                }
            });
        } else {
            mSmartRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSmartRefreshLayout.finishRefresh(false);
                }
            });
        }
    }

    public void showEmptyView() {
        Log.e(TAG, "showEmptyView");
        View child = mSmartRefreshLayout.getChildAt(0);
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
        View child = mSmartRefreshLayout.getChildAt(0);
        Log.e(TAG, "child == null ? " + (child == null));
        if (child != null) {
            if (child.getVisibility() != View.VISIBLE) {
                Log.e(TAG, "showWebView");
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

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
