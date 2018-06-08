package com.cdkj.token.wallet.coin_detail;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRechargeBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.wallet.account.BillActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 地址展示  （收款）
 */
public class WalletAddressShowActivity extends AbsBaseLoadActivity {


    private ActivityRechargeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WalletAddressShowActivity.class));
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
        initQRCodeAndAddress();
        initListener();
    }


    private void initQRCodeAndAddress() {
        WalletDBModel walletDBModel = WalletHelper.getPrivateKeyAndAddress();
        Bitmap mBitmap = CodeUtils.createImage(walletDBModel.getAddress(), 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(walletDBModel.getAddress());

    }

    private void initListener() {

        mBinding.llClose.setOnClickListener(view -> {
            mBinding.llTip.setVisibility(View.GONE);
        });

        mBinding.llCopy.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(mBinding.txtAddress.getText())) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mBinding.txtAddress.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(WalletAddressShowActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
            }

        });

        mBinding.llAddress.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(mBinding.txtAddress.getText())) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mBinding.txtAddress.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(WalletAddressShowActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
            }

        });


    }
}
