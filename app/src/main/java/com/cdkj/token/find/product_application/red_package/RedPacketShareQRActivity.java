package com.cdkj.token.find.product_application.red_package;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.tha.wxapi.WeiboShareActivity;
import com.cdkj.tha.wxapi.WxUtil;
import com.cdkj.token.R;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivityRedpacketShareBinding;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.tha.wxapi.WeiboShareActivity.SCOPE;

/**
 * 二维码展示
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketShareQRActivity extends BaseActivity {

    private ActivityRedpacketShareBinding mBinding;
    private String redPackageCode;

    private PermissionHelper mPermissionHelper;

    private WbShareHandler wbShareHandler;

    private boolean isOpenHistory;

    public static void open(Context context, String redPackageCode, boolean isOpenHistory) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPacketShareQRActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN2, isOpenHistory);
        intent.putExtra(CdRouteHelper.DATASIGN, redPackageCode);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_redpacket_share);
        if (getIntent() != null) {
            redPackageCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
            isOpenHistory = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN2, true);
        }
        mPermissionHelper = new PermissionHelper(this);
        getRedPacketShareUrlRequest();
        initListener();
    }


    private void initListener() {

        //微信分享
        mBinding.linLayoutWxShare.setOnClickListener(view -> shareToWx());
        //微信朋友圈
        mBinding.linLayoutPyqShare.setOnClickListener(view -> shareToWxPYQ());

        mBinding.linLayoutPyqWeibo.setOnClickListener(view -> shareToWeiBo());

        //关闭界面
        mBinding.imgClose.setOnClickListener(view -> {
            if (isOpenHistory) {
                RedPacketSendHistoryActivity.openMySend(this);
            }
            finish();
        });

        //图片保存
        mBinding.imgSave.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
        });

    }

    private void shareToWeiBo() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())  //创建
                .map(s -> BitmapUtils.getBitmapByView(mBinding.scrollView))
                .observeOn(Schedulers.newThread())  //创建
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    shareToWeiBo(bytes);
                    disMissLoadingDialog();
                }, throwable -> {
                    disMissLoadingDialog();
                    UITipDialog.showFail(this, getString(R.string.info_share_fail));
                    LogUtil.E("微信分享错误" + throwable.toString());
                }));
    }

    private void shareToWeiBo(Bitmap bitmap) {

        WbSdk.install(this, new AuthInfo(this, WeiboShareActivity.APPKEY, WeiboShareActivity.APPURL, SCOPE));

        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        weiboMessage.imageObject = imageObject;

        wbShareHandler.shareMessage(weiboMessage, false);

    }

    /**
     * 分享到微信朋友圈
     */
    private void shareToWxPYQ() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())  //创建
                .map(s -> BitmapUtils.getBitmapByView(mBinding.scrollView))
                .observeOn(Schedulers.newThread())  //创建
                .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    WxUtil.shareBitmapToWXPYQ(this, bytes);
                    disMissLoadingDialog();
                }, throwable -> {
                    disMissLoadingDialog();
                    UITipDialog.showFail(this, getString(R.string.info_share_fail));
                    LogUtil.E("微信分享错误" + throwable.toString());
                }));
    }

    /**
     * 分享到微信
     */
    private void shareToWx() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())  //创建
                .map(s -> BitmapUtils.getBitmapByView(mBinding.fralayoutRedpacket))
                .observeOn(Schedulers.newThread())  //创建
                .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    WxUtil.shareBitmapToWX(this, bytes);
                    disMissLoadingDialog();
                }, throwable -> {
                    disMissLoadingDialog();
                    UITipDialog.showFail(this, getString(R.string.info_share_fail));
                    LogUtil.E("微信分享错误" + throwable.toString());
                }));
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * 保存图片到相册
     */
    private void saveBitmapToAlbum() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())  //创建
                .map(o -> BitmapUtils.getBitmapByView(mBinding.fralayoutRedpacket))
                .observeOn(Schedulers.newThread())  //创建
                .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));

                }, throwable -> {
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));
                    LogUtil.E("图片保存失败" + throwable);
                }));
    }


    /**
     * 申请权限
     */
    private void permissionRequestAndSaveBitmap() {
        mPermissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                saveBitmapToAlbum();
            }

            @Override
            public void doAfterDenied(String... permission) {
                showSureDialog(getString(R.string.no_file_permission), view -> {
                });
            }

        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     *
     */
    public void getRedPacketShareUrlRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
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

                try {
                    showQRImage(data);
                } catch (Exception e) {

                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    /**
     * 显示二维码
     *
     * @param data
     */
    private void showQRImage(IntroductionInfoModel data) {
        Bitmap bitmap = CodeUtils.createImage(data.getCvalue() + ThaAppConstant.getRedPacketShareUrl(redPackageCode, SPUtilHelper.getSecretUserId()), 500, 500, null);
        mBinding.imgQRCode.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (wbShareHandler == null) return;
        wbShareHandler.doResultIntent(intent, new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                UITipDialog.showSuccess(RedPacketShareQRActivity.this, getString(R.string.share_succ));
            }

            @Override
            public void onWbShareCancel() {
                UITipDialog.showSuccess(RedPacketShareQRActivity.this, getString(R.string.share_cancel));
            }

            @Override
            public void onWbShareFail() {
                UITipDialog.showSuccess(RedPacketShareQRActivity.this, getString(R.string.save_fail));
            }
        });
    }


}
