package com.cdkj.token.find.product_application.red_package;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivityRedpacketShareBinding;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 二维码展示
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketShareQRActivity extends BaseActivity {

    private ActivityRedpacketShareBinding mBinding;
    private String redPackageCode;

    private PermissionHelper mPermissionHelper;

    public static void open(Context context, String redPackageCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPacketShareQRActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, redPackageCode);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_redpacket_share);
        if (getIntent() != null) {
            redPackageCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }
        mPermissionHelper = new PermissionHelper(this);
        getRedPacketShareUrlRequest();
        initListener();
    }


    private void initListener() {

        mBinding.imgClose.setOnClickListener(view -> {
            finish();
        });

        mBinding.imgSave.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
            saveBitmapToAlbum();
        });

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
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())  //创建
                .map(o -> BitmapUtils.getBitmapByView(mBinding.fralayoutRedpacket))
                .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, "Theai_red_packet"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));
                }, throwable -> {
                    disMissLoadingDialog();
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));
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

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getJsonToString(map));

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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();

        ImgUtils.loadByte(RedPacketShareQRActivity.this, datas, mBinding.imgQRCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
