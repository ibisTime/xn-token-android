package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityWebviewBinding;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 介绍类webview
 */
public class WebViewActivity extends AbsActivity {

    private ActivityWebviewBinding mBinding;

//    private WebView webView;

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void openkey(Activity activity, String title, String code) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewActivity.class);
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

        Intent intent = new Intent(activity, WebViewActivity.class);
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

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_webview, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);
        setTopLineState(true);

        initLayout();

        initData();
    }

    private void initLayout() {
        //输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        webView = new WebView(this);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);//js
        mBinding.webview.getSettings().setDefaultTextEncodingName("UTF-8");
        mBinding.webview.getSettings().setSupportZoom(true);   //// 支持缩放
        mBinding.webview.getSettings().setBuiltInZoomControls(true);//// 支持缩放
        mBinding.webview.getSettings().setDomStorageEnabled(true);//开启DOM
        mBinding.webview.getSettings().setLoadWithOverviewMode(false);//// 缩放至屏幕的大小
        mBinding.webview.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
        mBinding.webview.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        mBinding.webview.setWebChromeClient(new MyWebViewClient1());
        mBinding.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBinding.webview.getSettings().setMixedContentMode( mBinding.webview.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//

        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
            }
        });
        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        mBinding.linLayoutWebView.addView(webView, 1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.webview.loadUrl("about:blank");
    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

        setTopTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {

            if (TextUtils.isEmpty(getIntent().getStringExtra("content"))) {
                getKeyUrl(getIntent().getStringExtra("code"));
            } else {
                showContent(getIntent().getStringExtra("content"));
            }
        } else {

            LogUtil.E("打开url" + url);
            mBinding.webview.loadUrl(url);
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

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.objectToJsonString(map));

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
        mBinding.webview.loadData(content, "text/html;charset=UTF-8", "UTF-8");
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
        if (mBinding.webview != null && mBinding.webview.canGoBack()) {
            mBinding.webview.goBack();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        if (mBinding.webview != null) {
            mBinding.webview.clearHistory();
            ((ViewGroup) mBinding.webview.getParent()).removeView(mBinding.webview);
            mBinding.webview.loadUrl("about:blank");
            mBinding.webview.stopLoading();
            mBinding.webview.setWebChromeClient(null);
            mBinding.webview.setWebViewClient(null);
            mBinding.webview.destroy();
        }
        super.onDestroy();
    }


}
