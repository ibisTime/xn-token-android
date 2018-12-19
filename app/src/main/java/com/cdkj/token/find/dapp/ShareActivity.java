package com.cdkj.token.find.dapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.tha.wxapi.WeiboShareActivity;
import com.cdkj.tha.wxapi.WxUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityShareBinding;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;


/**
 * 分享界面
 * Created by cdkj on 2017/8/1.
 */

public class ShareActivity extends AbsActivity implements WbShareCallback {

    private ActivityShareBinding mBinding;

    private String mShareUrl;//需要分享的URL
    private String mPhotoUrl;//需要分享的URL

    private String mTitle;//title
    private String mContent;//content

    private UITipDialog tipDialog;

    private WbShareHandler wbShareHandler;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String shareUrl, String title, String content) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_share, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mShareUrl = getIntent().getStringExtra("shareUrl");
            mTitle = getIntent().getStringExtra("title");
            mContent = getIntent().getStringExtra("content");
            mPhotoUrl = getIntent().getStringExtra("photoUrl");
        }


        initListener();
    }


    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.txtCancel.setOnClickListener(v -> {
            finish();
        });

        //微信分享
        mBinding.linLayoutWxShare.setOnClickListener(view -> {
            Log.e("linLayoutWxShare","linLayoutWxShare");
            WxUtil.shareToWX(ShareActivity.this, mShareUrl,
                    mTitle, mContent);
        });
        //微信朋友圈
        mBinding.linLayoutPyqShare.setOnClickListener(view -> {
            Log.e("linLayoutPyqShare","linLayoutPyqShare");
            WxUtil.shareToPYQ(ShareActivity.this, mShareUrl,
                    mTitle, mContent);
        });

        mBinding.linLayoutWeibo.setOnClickListener(view -> {
            shareToWeiBo(mShareUrl);
        });

    }

    private void shareToWeiBo(String url) {

        WbSdk.install(this, new AuthInfo(this, WeiboShareActivity.APPKEY, WeiboShareActivity.APPURL, null));


        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
        wbShareHandler.doResultIntent(getIntent(), this);

        WeiboMultiMessage message = new WeiboMultiMessage();

        TextObject object = new TextObject();
        object.text = url;
        message.textObject = object;

        wbShareHandler.shareMessage(message, false);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wbShareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {
        UITipDialog.showSuccess(this, getString(R.string.share_succ));
        finish();
    }

    @Override
    public void onWbShareCancel() {
        UITipDialog.showSuccess(this, getString(R.string.share_cancel));
        finish();
    }

    @Override
    public void onWbShareFail() {
        UITipDialog.showSuccess(this, getString(R.string.share_fail));
        finish();
    }
}
