package com.cdkj.token.wallet.account_wallet;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.GlideApp;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityAddressQrimgShowBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

//TODO 地址显示优化WalletAddressShowActivity界面重复
public class RechargeAddressQRActivity extends AbsLoadActivity {

    private ActivityAddressQrimgShowBinding mBinding;

    private PermissionHelper mPermissionHelper;

    /**
     * @param context
     * @param coinType 币种类型
     */
    public static void open(Context context, String coinType) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, RechargeAddressQRActivity.class)
                .putExtra(CdRouteHelper.DATASIGN, coinType));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_address_qrimg_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_title_charge));

        mPermissionHelper = new PermissionHelper(this);

        if (getIntent() != null) {
            getAddressByType(getIntent().getStringExtra(CdRouteHelper.DATASIGN));
        }
        initListener();
    }


    private void initQRCodeAndAddress(CoinModel.AccountListBean model) {
        if (model == null) return;

        String coinLogoUrl = SPUtilHelper.getQiniuUrl() + LocalCoinDBUtils.getCoinWatermarkWithCurrency(model.getCurrency(), 0);

        GlideApp.with(this).asBitmap().load(coinLogoUrl)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap mBitmap = CodeUtils.createImage(model.getCoinAddress(), 400, 400, resource);
                        mBinding.imgQRCode.setImageBitmap(mBitmap);
                        mBinding.txtAddress.setText(model.getCoinAddress());
                    }

                });


    }

    private void initListener() {

        mBinding.btnCopy.setOnClickListener(view -> {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(mBinding.txtAddress.getText().toString()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            UITipDialog.showInfoNoIcon(RechargeAddressQRActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        });

        mBinding.btnSavePhoto.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
        });
    }


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
    public void saveBitmapToAlbum() {

        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> BitmapUtils.getBitmapByView(mBinding.scrollView))
                .observeOn(Schedulers.newThread())
                .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));
                }, throwable -> {
                    LogUtil.E("a" + throwable);
                    UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));
                    disMissLoading();
                }, () -> disMissLoading()));
    }


    /**
     * 获取币种地址
     *
     * @param
     */
    private void getAddressByType(String type) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || TextUtils.isEmpty(type))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", type);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                if (data.getAccountList() == null || data.getAccountList().size() == 0) {
                    return;
                }
                initQRCodeAndAddress(data.getAccountList().get(0));
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
