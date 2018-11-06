package com.cdkj.token.wallet.private_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransctionDetailsBinding;
import com.cdkj.token.model.LocalUSDTCoinBill;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.token.utils.AmountUtil.ETHSCALE;
import static com.cdkj.token.utils.wallet.WalletHelper.COIN_USDT;

/**
 * USDT币交易详情
 * Created by cdkj on 2018/6/12.
 */

public class USDTTransactionDetailsActivity extends AbsLoadActivity {

    private ActivityTransctionDetailsBinding mBinding;

    private String sendAddress;

    /**
     * @param context
     * @param localCoinBill localCoinBill
     */
    public static void open(Context context, LocalUSDTCoinBill localCoinBill, String sendAddress) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, USDTTransactionDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, localCoinBill);
        intent.putExtra(CdRouteHelper.DATASIGN2, sendAddress);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transction_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(getString(R.string.transaction_details));

        LocalUSDTCoinBill localCoinBill = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        sendAddress = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);

        setShowData(localCoinBill);

        mBinding.tvViewMore.setOnClickListener(view -> {

            if (localCoinBill != null) {
                WebViewActivity.openURL(this, getString(R.string.transaction_details), WalletHelper.getBrowserUrlByCoinType(COIN_USDT) + localCoinBill.getTxid());
            }
        });
    }

    /**
     * 设置页面显示数据
     *
     * @param localCoinBill
     */
    private void setShowData(LocalUSDTCoinBill localCoinBill) {

        if (localCoinBill == null) return;

//        mBinding.imgStateIcon.setImageResource(CoinUtil.getStataIconByState(localCoinBill.getDirection()));

        if (!sendAddress.equals(localCoinBill.getSendingAddress())) {
            mBinding.tvStateString.setText(R.string.withdraw);
        } else {
            mBinding.tvStateString.setText(R.string.transfer);
        }

        mBinding.tvMoney.setText(getMoneyStateByAddress(localCoinBill.getSendingAddress()) + AmountUtil.transformFormatToString(localCoinBill.getAmount(), COIN_USDT, ETHSCALE) + " " + COIN_USDT);

        mBinding.tvToAddress.setText(localCoinBill.getReferenceAddress());
        mBinding.tvFromAddress.setText(localCoinBill.getSendingAddress());
        mBinding.tvBlockHeight.setText(localCoinBill.getBlock() + "");
        mBinding.tvDate.setText(DateUtil.formatIntegerData(localCoinBill.getBlockTime(), DEFAULT_DATE_FMT));
        mBinding.tvGas.setText(AmountUtil.transformFormatToString(localCoinBill.getFee(), COIN_USDT, ETHSCALE));
        mBinding.tvTransctionCode.setText(localCoinBill.getTxid());

    }

    public String getMoneyStateByAddress(String sendingAddress) {
        if (!sendAddress.equals(sendingAddress)) {
            return "+";
        }
        return "-";

    }
}
