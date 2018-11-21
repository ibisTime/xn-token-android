package com.cdkj.token.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityHelpCenterBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * webView图片背景
 *
 */
public class WebViewImgBgActivity extends AbsStatusBarTranslucentActivity {

    private ActivityHelpCenterBinding mBinding;

    private WebView webView;

    private SslErrorHandler mHandler;

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void openkey(Activity activity, String title, String code) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewImgBgActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }

    /**
     * 加载activity,加载富文本
     *
     * @param activity 上下文
     */
    public static void openContent(Context activity, String title, String content) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewImgBgActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void openURL(Activity activity, String title, String url) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewImgBgActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_help_center, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setPageBgImage(R.drawable.my_bg);
        initLayout();
        initData();
    }

    private void initLayout() {
        //输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);//js
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
//       webView.getSettings().setSupportZoom(true);   //// 支持缩放
//       webView.getSettings().setBuiltInZoomControls(true);//// 支持缩放
//       webView.getSettings().setDomStorageEnabled(true);//开启DOM
//       webView.getSettings().setLoadWithOverviewMode(false);//// 缩放至屏幕的大小
//       webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
//       webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        webView.setWebChromeClient(new MyWebViewClient1());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();  // 接受所有网站的证书

                // 处理Google play因WebView SSL Error Handler alerts被拒的问题
                mHandler= handler;
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewImgBgActivity.this);
                builder.setMessage(R.string.ssl_error);
                builder.setPositiveButton(R.string.go_on, (dialog, which) -> mHandler.proceed());
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> mHandler.cancel());
                builder.setOnKeyListener((dialog, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        mHandler.cancel();
                        dialog.dismiss();
                        return true;
                    }
                    return false;
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mBinding.linLayoutWebView.addView(webView );

    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

        setMidTitle(getIntent().getStringExtra("title"));

        if (TextUtils.isEmpty(getIntent().getStringExtra("url"))) {

            if (TextUtils.isEmpty(getIntent().getStringExtra("content"))) {
                getKeyUrl(getIntent().getStringExtra("code"));
            } else {
                showContent(getIntent().getStringExtra("content"));
            }
        } else {
            webView.loadUrl(getIntent().getStringExtra("url"));
        }

    }


    public void getKeyUrl(String key) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("ckey", key);
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                showContent(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }

    private void showContent(String content) {
        webView.loadData(content, "text/html;charset=UTF-8", "UTF-8");
    }


    private class MyWebViewClient1 extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mBinding.pb.setProgress(newProgress);

            if (newProgress > 90) {
                mBinding.pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


}
