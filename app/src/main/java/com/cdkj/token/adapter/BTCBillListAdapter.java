package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.getPrivateCoinStataIconByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.isInState;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class BTCBillListAdapter extends BaseQuickAdapter<BTCBillModel, BaseViewHolder> {

    private BigDecimal btcUnit;

    public BTCBillListAdapter(@Nullable List<BTCBillModel> data) {
        super(R.layout.item_bill_2, data);
        btcUnit = LocalCoinDBUtils.getLocalCoinUnit(WalletHelper.COIN_BTC);
    }

    @Override
    protected void convert(BaseViewHolder helper, BTCBillModel item) {
        if (item == null) return;

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getTransDatetime(), DATE_MMddHHmm));
        helper.setImageResource(R.id.iv_type, getPrivateCoinStataIconByState(item.getDirection()));

        String amount = AmountUtil.transformFormatToString(item.getValue(), btcUnit, AmountUtil.ALLSCALE) + " " + WalletHelper.COIN_BTC;

        helper.setText(R.id.tv_amount, getMoneyStateByState(item.getDirection()) + amount);

        if (isInState(item.getDirection())) {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.get_money));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.in_money));

            if (item.getHeight() < 0) {
                helper.setText(R.id.tv_amount, amount + mContext.getString(R.string.transaction_in));
            }

        } else if (AmountUtil.bigDecimalFormat(item.getValue(), btcUnit).compareTo(BigDecimal.ZERO) == 0) {                  //执行合约

            helper.setText(R.id.tv_remark, R.string.do_contract);

            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
            helper.setText(R.id.tv_amount, AmountUtil.transformFormatToString(item.getValue(), btcUnit, AmountUtil.ALLSCALE) + " " + WalletHelper.COIN_BTC);

        } else {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.transfer));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
            if (item.getHeight() < 0) {
                helper.setText(R.id.tv_amount, R.string.transaction_out);
            }
        }

        helper.setText(R.id.tv_address, item.getTxHash());
    }

    /**
     * 获取哈希值显示格式
     *
     * @param hash
     * @return
     */
    private String getShowHashText(String hash) {
        if (TextUtils.isEmpty(hash)) {
            return "";
        }

        if (hash.length() > 12) {
            return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 6, hash.length());
        }

        return hash;
    }
}
