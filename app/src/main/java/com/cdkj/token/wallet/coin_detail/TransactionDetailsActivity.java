package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransctionDetailsBinding;
import com.cdkj.token.databinding.ActivityTransferBinding;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.CoinUtil;
import com.cdkj.token.utils.WalletHelper;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.token.utils.AccountUtil.ETHSCALE;
import static com.cdkj.token.utils.CoinUtil.getMoneyStateByState;

/**
 * 交易详情
 * Created by cdkj on 2018/6/12.
 */

public class TransactionDetailsActivity extends AbsBaseLoadActivity {

    private ActivityTransctionDetailsBinding mBinding;

    private String coinType;

    /**
     * @param context
     * @param localCoinBill localCoinBill
     * @param coinType      币类型
     */
    public static void open(Context context, LocalCoinBill localCoinBill, String coinType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, localCoinBill);
        intent.putExtra(CdRouteHelper.DATASIGN2, coinType);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transction_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.transaction_details));

        LocalCoinBill localCoinBill = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        coinType = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);

        setShowData(localCoinBill);

        mBinding.tvViewMore.setOnClickListener(view -> {

            if (localCoinBill != null) {
                if (TextUtils.equals(coinType, WalletHelper.COIN_ETH)) {
                    WebViewActivity.openURL(this, getString(R.string.transaction_details), WalletHelper.TO_BROWSER_URL + localCoinBill.getTxHash());
                } else if (TextUtils.equals(coinType, WalletHelper.COIN_WAN)) {
                    WebViewActivity.openURL(this, getString(R.string.transaction_details), WalletHelper.WAN_TO_BROWSER_URL + localCoinBill.getTxHash());
                }
            }
        });
    }

    /**
     * 设置页面显示数据
     *
     * @param localCoinBill
     */
    private void setShowData(LocalCoinBill localCoinBill) {

        if (localCoinBill == null) return;

        mBinding.imgStateIcon.setImageResource(CoinUtil.getStataIconByState(localCoinBill.getDirection()));

        if (CoinUtil.isInState(localCoinBill.getDirection())) {
            mBinding.tvStateString.setText(R.string.withdraw);
        } else {
            mBinding.tvStateString.setText(R.string.transfer);
        }

        mBinding.tvMoney.setText(getMoneyStateByState(localCoinBill.getDirection()) + AccountUtil.amountFormatUnitForShow(localCoinBill.getValue(), ETHSCALE) + " " + coinType);

        mBinding.tvToAddress.setText(localCoinBill.getTo());
        mBinding.tvFromAddress.setText(localCoinBill.getFrom());
        mBinding.tvBlockHeight.setText(localCoinBill.getHeight());
        mBinding.tvDate.setText(DateUtil.formatStringData(localCoinBill.getTransDatetime(), DEFAULT_DATE_FMT));
        mBinding.tvGas.setText(AccountUtil.amountFormatUnitForShow(localCoinBill.getTxFee(), ETHSCALE));
        mBinding.tvTransctionCode.setText(localCoinBill.getTxHash());


    }
}
