package com.cdkj.token.user.invite;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivityInviteQrBinding;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteQrActivity extends AbsStatusBarTranslucentActivity {

    private ActivityInviteQrBinding mBinding;

    private PermissionHelper mPermissionHelper;
    private String shareUrl;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteQrActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite_qr, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canEvenFinish() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.fraLayoutTitle.setVisibility(View.GONE);

        mPermissionHelper = new PermissionHelper(this);

        setPageBgImage(R.mipmap.invite_bg);

        setClickListener();

        geShareUrlRequest();

        getinviteRule();
    }

    private void setClickListener() {

        mBinding.tvFinish.setOnClickListener(view -> finish());

        mBinding.btnCopy.setOnClickListener(view -> {
            copyShareURl();
        });

        mBinding.tvFinish.setOnClickListener(view -> finish());

        // 本地保存
        mBinding.llSave.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
        });

        // 微信
        mBinding.llWx.setOnClickListener(view -> {

            showLoadingDialog();
            mBinding.linLayoutCopy.setVisibility(View.GONE);
            mBinding.llRule.setVisibility(View.VISIBLE);
            mSubscription.add(Observable.just("")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread())  //创建
                    .map(s -> screenshot())
                    .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
//                        WxUtil.shareBitmapToWX(InviteQrActivity.this, path);
                        disMissLoadingDialog();

                        // 还原UI
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        mBinding.llRule.setVisibility(View.GONE);
                    }, throwable -> {
                        disMissLoadingDialog();
                        UITipDialog.showFail(this, getString(R.string.info_share_fail));
                        LogUtil.E("微信分享错误" + throwable.toString());
                        // 还原UI
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        mBinding.llRule.setVisibility(View.GONE);
                    }));

        });

        // 朋友圈
        mBinding.llPyq.setOnClickListener(view -> {

            showLoadingDialog();
            mSubscription.add(Observable.just(screenshot())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread())  //创建
                    .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
//                        WxUtil.shareBitmapToWXPYQ(InviteQrActivity.this, path);
                        disMissLoadingDialog();

                        // 还原UI
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        mBinding.llRule.setVisibility(View.GONE);
                    }, throwable -> {
                        disMissLoadingDialog();
                        UITipDialog.showFail(this, getString(R.string.info_share_fail));
                        LogUtil.E("微信朋友圈分享错误" + throwable.toString());

                        // 还原UI
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        mBinding.llRule.setVisibility(View.GONE);
                    }));

        });

        // 微博
        mBinding.llWb.setOnClickListener(view -> {

        });

    }

    private Bitmap screenshot() {
        return BitmapUtils.getBitmapByView(mBinding.scrollview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
     * 保存图片到相册
     */
    private void saveBitmapToAlbum() {
        showLoadingDialog();
        mBinding.linLayoutCopy.setVisibility(View.GONE);
        mBinding.llRule.setVisibility(View.VISIBLE);
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())  //创建
                .map(o -> getBitmapByView(mBinding.scrollview))
                .observeOn(Schedulers.newThread())  //创建
                .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, "Theai_invite"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                    mBinding.llRule.setVisibility(View.GONE);
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));


                }, throwable -> {
                    mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                    mBinding.llRule.setVisibility(View.GONE);
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));
                    LogUtil.E("图片保存失败" + throwable);
                }));
    }


    /**
     * 截取scrollview的生产bitmap
     *
     * @param scrollView
     * @return
     */
    public Bitmap getBitmapByView(ScrollView scrollView) {

        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            if (scrollView.getChildAt(i).getVisibility() != View.VISIBLE) {
                continue;
            }
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        return bitmap;
    }


    public void geShareUrlRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
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

                shareUrl = data.getCvalue() + ThaAppConstant.getInviteFriendUrl(SPUtilHelper.getSecretUserId());

                try {
                    Bitmap bitmap = CodeUtils.createImage(shareUrl, 500, 500, null);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] datas = baos.toByteArray();
                    ImgUtils.loadByte(InviteQrActivity.this, datas, mBinding.ivQr);

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
     *
     */
    public void getinviteRule() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "invite_add");
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

                mBinding.tvInviteRule.setText(getString(R.string.invite_26, data.getCvalue()));
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    private void copyShareURl() {
        if (!TextUtils.isEmpty(shareUrl)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, shareUrl);
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(InviteQrActivity.this, getStrRes(R.string.copy_success));
        }
    }

}
