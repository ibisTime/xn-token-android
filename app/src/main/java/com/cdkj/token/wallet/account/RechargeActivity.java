package com.cdkj.token.wallet.account;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityAddressQrimgShowBinding;
import com.cdkj.token.databinding.ActivityRechargeBinding;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.wallet.coin_detail.WalletAddressShowActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class RechargeActivity extends AbsLoadActivity {

    private WalletBalanceModel model;

    private ActivityAddressQrimgShowBinding mBinding;

    public static void open(Context context, WalletBalanceModel model) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, RechargeActivity.class)
                .putExtra(CdRouteHelper.DATASIGN, model));
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

        if (getIntent() != null) {
            model = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

            if (!TextUtils.isEmpty(model.getAddress())) {
                initQRCodeAndAddress();
            }

        }

        initView();
        initListener();

    }

    private void initView() {
        if (model == null)
            return;

//        mBinding.tvCoin.setText(getString(R.string.my_coin_address, model.getCoinName()));

//        switch (model.getCoinName()){
//            case "ETH":
//                mBinding.tvCoin.setText(getStrRes(R.string.wallet_charge_address_eth));
//                break;
//
//            case "BTC":
//                mBinding.tvCoin.setText(getStrRes(R.string.wallet_charge_address_btc));
//                break;
//        }
    }

    private void initQRCodeAndAddress() {
        Bitmap mBitmap = CodeUtils.createImage(model.getAddress(), 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(model.getAddress());

    }

    private void initListener() {

        mBinding.btnCopy.setOnClickListener(view -> {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(model.getAddress().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            UITipDialog.showInfoNoIcon(RechargeActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        });
    }
}
