package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.adapter.BTCFromAddressListAdapter;
import com.cdkj.token.adapter.BTCToAddressListAdapter;
import com.cdkj.token.databinding.ActivityBtcTransctionDetailsBinding;
import com.cdkj.token.databinding.ActivityTransctionDetailsBinding;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.token.utils.AccountUtil.ETHSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;

/**
 * 交易详情
 * Created by cdkj on 2018/6/12.
 */

public class BTCTransactionDetailsActivity extends AbsLoadActivity {

    private ActivityBtcTransctionDetailsBinding mBinding;

    /**
     * @param context
     * @param btcBillModel localCoinBill
     */
    public static void open(Context context, BTCBillModel btcBillModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BTCTransactionDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, btcBillModel);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_btc_transction_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(getString(R.string.transaction_details));

        BTCBillModel btcBill = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        setShowData(btcBill);

        setShowFromAddress(btcBill);

        setShowToAddress(btcBill);

        mBinding.tvViewMore.setOnClickListener(view -> {

            if (btcBill != null) {
                WebViewActivity.openURL(this, getString(R.string.transaction_details), WalletHelper.getBrowserUrlByCoinType(WalletHelper.COIN_BTC) + btcBill.getTxHash());
            }
        });
    }

    /**
     * 显示转出地址
     */
    private void setShowToAddress(BTCBillModel btcBillModel) {

        if (btcBillModel == null) return;
        mBinding.recyclerViewToAddress.setLayoutManager(new LinearLayoutManager(BTCTransactionDetailsActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mBinding.recyclerViewToAddress.setAdapter(new BTCToAddressListAdapter(btcBillModel.getVout(), btcBillModel.getAddress()));

    }

    /**
     * 显示收款地址
     */
    private void setShowFromAddress(BTCBillModel btcBillModel) {

        if (btcBillModel == null) return;
        mBinding.recyclerViewFromAddress.setLayoutManager(new LinearLayoutManager(BTCTransactionDetailsActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recyclerViewFromAddress.setAdapter(new BTCFromAddressListAdapter(btcBillModel.getVin(), btcBillModel.getAddress()));
    }

    /**
     * 设置页面显示数据
     *
     * @param btcBillModel
     */
    private void setShowData(BTCBillModel btcBillModel) {

        if (btcBillModel == null) return;

        if (LocalCoinDBUtils.isInState(btcBillModel.getDirection())) {
            mBinding.tvStateString.setText(R.string.withdraw);
        } else {
            mBinding.tvStateString.setText(R.string.transfer);
        }

        mBinding.tvMoney.setText(getMoneyStateByState(btcBillModel.getDirection()) + AccountUtil.amountFormatUnitForShow(btcBillModel.getValue(), WalletHelper.COIN_BTC, ETHSCALE) + " " + WalletHelper.COIN_BTC);

        mBinding.tvBlockHeight.setText(btcBillModel.getHeight()+"");
        mBinding.tvDate.setText(DateUtil.formatStringData(btcBillModel.getTransDatetime(), DEFAULT_DATE_FMT));
        mBinding.tvGas.setText(AccountUtil.amountFormatUnitForShow(btcBillModel.getTxFee(), WalletHelper.COIN_BTC, ETHSCALE));
        mBinding.tvTransctionCode.setText(btcBillModel.getTxHash());


    }
}
