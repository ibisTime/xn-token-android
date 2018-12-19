package com.cdkj.token.wallet.export;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityPrivateKeyShowBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 私钥展示
 * Created by cdkj on 2018/6/7.
 */

public class CoinPrivateKeyShowActivity extends AbsLoadActivity {

    private ActivityPrivateKeyShowBinding mBinding;

    /**
     * @param context
     * @param coinType
     */
    public static void open(Context context, String coinType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinPrivateKeyShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, coinType);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_private_key_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        String type = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        String name = "";

        mBaseBinding.titleView.setMidTitle(name + getString(R.string.private_key));

        mBinding.tvCoinName.setText(getString(R.string.coin_key_name, name));

        mSubscription.add(
                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .map(s -> WalletHelper.getUserWalletInfoByUserId(WalletHelper.WALLET_USER))
                        .filter(walletDBModel1 -> walletDBModel1 != null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(walletDBModel1 -> {
                            mBinding.tvAddress.setText(WalletHelper.getAddressByCoinType(walletDBModel1, type));
                            mBinding.tvPrivateKey.setText(WalletHelper.getPrivateKeyByCoinType(walletDBModel1, type));
                        })
        );

        mBinding.tvAddress.setOnClickListener(view -> {
            setCopyText(mBinding.tvAddress.getText(), getStrRes(R.string.wallet_charge_address_copy_success));
        });
        mBinding.tvPrivateKey.setOnClickListener(view -> {
            setCopyText(mBinding.tvPrivateKey.getText(), getString(R.string.private_key_copy_success));
        });

    }

    /**
     * @param copy
     * @param str  提示语句
     */
    private void setCopyText(CharSequence copy, String str) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(copy); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        ToastUtil.show(CoinPrivateKeyShowActivity.this, str);
    }
}
