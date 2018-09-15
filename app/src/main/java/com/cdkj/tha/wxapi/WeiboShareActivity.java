package com.cdkj.tha.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by cdkj on 2018/9/15.
 */

public class WeiboShareActivity extends BaseActivity implements WbShareCallback {


    private SsoHandler mSsoHandler;

    public static final String APPKEY = "947817370";
    public static final String APPURL = "http://theia.wallet";

    public static final String SCOPE = "invitation_write";

    //    public static final String SCOPE ="invitation_write";
    private WbShareHandler wbShareHandler;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WeiboShareActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareToWeiBo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void shareToWeiBo() {

        WbSdk.install(this, new AuthInfo(this, "947817370", "http://theia.wallet", SCOPE));

        mSsoHandler = new SsoHandler(WeiboShareActivity.this);
        mSsoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                if (oauth2AccessToken.isSessionValid()) {
                    UITipDialog.showSuccess(WeiboShareActivity.this, getString(R.string.share_succ), dialogInterface -> finish());
                }
            }

            @Override
            public void cancel() {
                UITipDialog.showInfo(WeiboShareActivity.this, getString(R.string.share_cancel), dialogInterface -> finish());
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                UITipDialog.showFail(WeiboShareActivity.this, getString(R.string.share_fail), dialogInterface -> finish());
            }
        });

        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
        sendMultiMessage(true, true);

    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj("df", "dfe");
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }

        wbShareHandler.shareMessage(weiboMessage, false);

    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.invite_bg);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String title, String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        textObject.title = title;
        return textObject;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wbShareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {

    }

    @Override
    public void onWbShareCancel() {

    }

    @Override
    public void onWbShareFail() {

    }
}
