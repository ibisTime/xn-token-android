package com.cdkj.tha.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.cdkj.token.R;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by LeiQ on 2017/1/10.
 */

public class WxUtil {

    private static IWXAPI api;

    public static final String APPID = "wx368e6044b7436dff";

    public static IWXAPI registToWx(Context context) {
        api = WXAPIFactory.createWXAPI(context, APPID, false);
        api.registerApp(APPID);
        return api;
    }

    /**
     * 检测是否有微信与是否支持微信支付
     *
     * @return
     */
    public static boolean check(Context context) {

        api = registToWx(context);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        boolean isPaySupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, R.string.wx_install_, Toast.LENGTH_SHORT).show();
            return false;
        }

        return isPaySupported;
    }

    /**
     * 分享到朋友圈
     *
     * @param
     */
    public static void shareToPYQ(Context context, String url, String title, String description) {
        System.out.println("shareURL=" + url);
        api = registToWx(context);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, R.string.wx_install_, Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 60, 60, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享微信聊天界面
     *
     * @param
     */
    public static void shareToWX(Context context, String url, String title, String description) {

        api = registToWx(context);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, R.string.wx_install_, Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 60, 60, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 分享微信聊天界面
     *
     * @param
     */
    public static void shareBitmapToWX(Context context, byte[] bitmap) {

        api = registToWx(context);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, R.string.wx_install_, Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject imageObject = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();

        msg.mediaObject = imageObject;

        Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 60, 60, true);
        msg.thumbData = Bitmap2Bytes(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");

        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }

    /**
     * 分享微信聊天界面
     *
     * @param
     */
    public static void shareBitmapToWXPYQ(Context context, byte[] bitmap) {

        api = registToWx(context);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, R.string.wx_install_, Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject imageObject = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();

        msg.mediaObject = imageObject;

        Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 60, 60, true);
        msg.thumbData = Bitmap2Bytes(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");

        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}