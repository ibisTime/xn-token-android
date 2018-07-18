package com.cdkj.token.wallet.coin_detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRechargeBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 地址展示  （收款）
 */
public class WalletAddressShowActivity extends AbsLoadActivity {


    private ActivityRechargeBinding mBinding;

    private String coinType;

    public static void open(Context context, String coinType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletAddressShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, coinType);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_recharge, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.get_money);

        if (getIntent() != null) {
            coinType = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        initQRCodeAndAddress();
        initListener();
    }


    private void initQRCodeAndAddress() {
        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());
        String address = WalletHelper.getAddressByCoinType(walletDBModel, coinType);
        Bitmap mBitmap = CodeUtils.createImage(address, 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(address);
    }

    private void initListener() {

        mBinding.llClose.setOnClickListener(view -> {
            mBinding.llTip.setVisibility(View.GONE);
        });

        mBinding.llCopy.setOnClickListener(view -> {

            copyAddress();

        });

        mBinding.llAddress.setOnClickListener(view -> {
            copyAddress();

        });


    }

    private void copyAddress() {
        if (!TextUtils.isEmpty(mBinding.txtAddress.getText())) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, mBinding.txtAddress.getText());
            cmb.setPrimaryClip(clipData);
            ToastUtil.show(WalletAddressShowActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        }
    }
}
