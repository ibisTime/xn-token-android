package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransferBinding;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.pop.GasTypeChoosePop;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.WalletHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包转账
 * Created by cdkj on 2018/6/8.
 */

public class WalletTransferActivity extends AbsBaseLoadActivity {

    private ActivityTransferBinding mBinding;

    private final int CODEPERSE = 101;

    public static void open(Context context, BalanceListModel.AccountListBean accountListBean) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletTransferActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, accountListBean);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        BalanceListModel.AccountListBean localCoinModel = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (localCoinModel != null) {
            mBinding.tvCurrency.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(localCoinModel.getBalance()), 8) + localCoinModel.getSymbol());
            mBaseBinding.titleView.setMidTitle(localCoinModel.getSymbol());
        }


        mBaseBinding.titleView.setMidTitle(R.string.transfer);
        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 15, 8));
        initClickListener();

        mSubscription.add(
                Observable.just("")
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> WalletHelper.getGasValue())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            mBinding.tvGas.setText(AccountUtil.amountFormatUnitForShowETH(new BigDecimal(s), 8));
                        }, throwable -> {

                        })
        );


        mBinding.btnNext.setOnClickListener(view -> {

            Observable.just("")
                    .subscribeOn(Schedulers.newThread())
                    .map(s -> {
                        WalletDBModel w = WalletHelper.getPrivateKeyAndAddress();
                        LogUtil.E("余额1" + WalletHelper.getBalance(w.getAddress()));
                        LogUtil.E("余额1" + AccountUtil.amountFormatUnitForShowETH(new BigDecimal(WalletHelper.getBalance(w.getAddress())), 8));

                        LogUtil.E("余额2" + WalletHelper.getBalance(mBinding.editToAddress.getText().toString()));
                        LogUtil.E("余额2" + AccountUtil.amountFormatUnitForShowETH(new BigDecimal(WalletHelper.getBalance(mBinding.editToAddress.getText().toString())), 8));
                        return WalletHelper.transfer(w, mBinding.editToAddress.getText().toString(), "0.1");
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getError() != null) {
                            LogUtil.E("has————" + s.getError().getMessage());
                        }
                        LogUtil.E("has2————" + s.getTransactionHash());
                    }, throwable -> {
                        LogUtil.E("has————" + throwable);
                    });
        });
    }

    private void initClickListener() {
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, CODEPERSE);
        });

        mBinding.linLayoutGasChoose.setOnClickListener(view -> {
            new GasTypeChoosePop(this).showPopupWindow();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == CODEPERSE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (WalletUtils.isValidAddress(result)) {
                        mBinding.editToAddress.setText(result);
                    } else {
                        Toast.makeText(WalletTransferActivity.this, "错误的地址", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WalletTransferActivity.this, "解析地址失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
