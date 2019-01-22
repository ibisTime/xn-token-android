package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogInfoSureBinding;

/**
 * 提示信息确认
 * Created by cdkj on 2018/7/20.
 */

public class InfoSureDialog extends Dialog {

    private Activity mActivity;

    private DialogInfoSureBinding mBinding;
    private Boolean isShowWeb;


    public InfoSureDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_info_sure, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
        int screenHeight = DisplayHelper.getScreenHeight(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), (int) (screenHeight * 0.6));
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        initListener();
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

    public InfoSureDialog setInfoTitle(String title) {
        if (mBinding == null) return this;
        mBinding.tvTitle.setText(title);
        return this;
    }

    public InfoSureDialog setInfoContent(String content) {
        if (mBinding == null) return this;
        if (!isShowWeb) {
            mBinding.tvContent.setText(content);
        } else {
            mBinding.webView.loadData(content, "text/html;charset=UTF-8", "UTF-8");
        }
        return this;
    }

    /**
     * 显示  web文本 还是 普通的文本  默认是普通文本
     *
     * @param isShowWeb
     * @return
     */
    public InfoSureDialog setShowWeb(Boolean isShowWeb) {
        this.isShowWeb = isShowWeb;
        if (isShowWeb) {
            mBinding.tvContent.setVisibility(View.GONE);
            mBinding.webView.setVisibility(View.VISIBLE);
            initWebView();
        }
        return this;
    }


    private void initListener() {
        mBinding.btnSure.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void initWebView() {
        mBinding.webView.getSettings().setJavaScriptEnabled(true);//js
        mBinding.webView.getSettings().setDefaultTextEncodingName("UTF-8");
//       mBinding.webview.getSettings().setSupportZoom(true);   //// 支持缩放
//       mBinding.webview.getSettings().setBuiltInZoomControls(true);//// 支持缩放
//       mBinding.webview.getSettings().setDomStorageEnabled(true);//开启DOM
//       mBinding.webview.getSettings().setLoadWithOverviewMode(false);//// 缩放至屏幕的大小
//       mBinding.webview.getSettings().setUseWideViewPort(true);//将图片调整到适合mBinding.webview的大小
//       webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        mBinding.webView.setWebChromeClient(new MyWebViewClient1());

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();  // 接受所有网站的证书

                // 处理Google play因WebView SSL Error Handler alerts被拒的问题
//                mHandler= handler;
//                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
//                builder.setMessage(com.cdkj.baselibrary.R.string.ssl_error);
//                builder.setPositiveButton(com.cdkj.baselibrary.R.string.go_on, (dialog, which) -> mHandler.proceed());
//                builder.setNegativeButton(com.cdkj.baselibrary.R.string.cancel, (dialog, which) -> mHandler.cancel());
//                builder.setOnKeyListener((dialog, keyCode, event) -> {
//                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                        mHandler.cancel();
//                        dialog.dismiss();
//                        return true;
//                    }
//                    return false;
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });
    }

    private class MyWebViewClient1 extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
}
