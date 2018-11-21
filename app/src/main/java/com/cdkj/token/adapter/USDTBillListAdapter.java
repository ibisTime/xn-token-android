package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.LocalUSDTCoinBill;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class USDTBillListAdapter extends BaseQuickAdapter<LocalUSDTCoinBill, BaseViewHolder> {

    private BigDecimal btcUnit;
    private String myAddress;

    public USDTBillListAdapter(@Nullable List<LocalUSDTCoinBill> data, String address) {
        super(R.layout.item_bill_2, data);
        btcUnit = LocalCoinDBUtils.getLocalCoinUnit(WalletHelper.COIN_USDT);
        myAddress = address;
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalUSDTCoinBill item) {
        if (item == null) return;

        helper.setText(R.id.tv_time, DateUtil.formatIntegerData(item.getBlockTime(), DATE_MMddHHmm));
        helper.setImageResource(R.id.iv_type, getIconByAddress(item.getSendingAddress()));

        String amount = AmountUtil.transformFormatToString(item.getAmount(), btcUnit, AmountUtil.ALLSCALE) + " " + WalletHelper.COIN_USDT;

        helper.setText(R.id.tv_amount, getMoneyStateByAddress(item.getSendingAddress()) + amount);

        if (!myAddress.equals(item.getSendingAddress())) {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.get_money));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.in_money));

        } else if (AmountUtil.bigDecimalFormat(item.getAmount(), btcUnit).compareTo(BigDecimal.ZERO) == 0) {                  //执行合约

            helper.setText(R.id.tv_remark, R.string.do_contract);

            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
            helper.setText(R.id.tv_amount, AmountUtil.transformFormatToString(item.getAmount(), btcUnit, AmountUtil.ALLSCALE) + " " + WalletHelper.COIN_USDT);

        } else {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.transfer));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
        }

        helper.setText(R.id.tv_address, item.getTxid());
    }


    public int getIconByAddress(String sendingAddress) {
        if (!myAddress.equals(sendingAddress)) {
            return R.drawable.private_coin_in;
        }
        return R.drawable.private_coin_out;
    }

    public String getMoneyStateByAddress(String sendingAddress) {
        if (!myAddress.equals(sendingAddress)) {
            return "+";
        }
        return "-";

    }
}
