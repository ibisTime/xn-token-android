package com.cdkj.token.wallet.private_wallet;

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
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityAddressQrimgShowBinding;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 地址展示  （收款）
 */
public class WalletAddressShowActivity extends AbsLoadActivity {


    private ActivityAddressQrimgShowBinding mBinding;

    private String mAddress;

    public static void open(Context context, String address) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletAddressShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, address);
        context.startActivity(intent);
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
        mBaseBinding.titleView.setMidTitle(R.string.get_money);

        if (getIntent() != null) {
            mAddress = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        initQRCodeAndAddress();
        initListener();
    }


    private void initQRCodeAndAddress() {
        if (TextUtils.isEmpty(mAddress)) return;
        Bitmap mBitmap = CodeUtils.createImage(mAddress, 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(mAddress);
    }

    private void initListener() {
        mBinding.btnCopy.setOnClickListener(view -> {
            copyAddress();
        });

    }

    private void copyAddress() {
        if (!TextUtils.isEmpty(mBinding.txtAddress.getText())) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, mBinding.txtAddress.getText());
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(WalletAddressShowActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        }
    }
}
