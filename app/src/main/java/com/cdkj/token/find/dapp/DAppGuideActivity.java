package com.cdkj.token.find.dapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityDappGuideBinding;

/**
 * Created by cdkj on 2018/12/13.
 */

public class DAppGuideActivity extends AbsActivity {

    private ActivityDappGuideBinding mBinding;

    private SslErrorHandler mHandler;

    private int id;
    private String url;
    private String title;
    private String content;

    private String loadUrl;

    public static void open(Context context, String url, int id, String title, String content) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DAppGuideActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, id);
        intent.putExtra(CdRouteHelper.DATASIGN2, url);
        intent.putExtra(CdRouteHelper.DATASIGN3, title);
        intent.putExtra(CdRouteHelper.DATASIGN4, content);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_dapp_guide, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        init();
        initLayout();
        initListener();

        load();
    }

    private void initListener() {
        mBinding.flBack.setOnClickListener(view -> {
            finish();
        });

        mBinding.flRight.setOnClickListener(view -> {
            if (SPUtilHelper.isLogin()){
                ShareActivity.open(this, loadUrl, title, content);
            }

        });
    }

    private void init() {
        id = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);
        url = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);
        title = getIntent().getStringExtra(CdRouteHelper.DATASIGN3);
        content = getIntent().getStringExtra(CdRouteHelper.DATASIGN4);

        loadUrl = url + "/public/strategy.html?strategyID="+id+"&userId="+ SPUtilHelper.getUserId()+"&lang="+SPUtilHelper.getLanguage();
    }

    private void initLayout() {
        //输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mBinding.webview.getSettings().setJavaScriptEnabled(true);//js
        mBinding.webview.getSettings().setDefaultTextEncodingName("UTF-8");
        mBinding.webview.setWebChromeClient(new DAppGuideActivity.MyWebViewClient());

        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();  // 接受所有网站的证书

                // 处理Google play因WebView SSL Error Handler alerts被拒的问题
                mHandler= handler;
                AlertDialog.Builder builder = new AlertDialog.Builder(DAppGuideActivity.this);
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

    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private void load() {

        mBinding.webview.loadUrl(loadUrl);
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
