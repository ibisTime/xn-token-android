package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.AccountUtil.ETHSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.getPrivateCoinStataIconByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.isInState;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class BTCBillListAdapter extends BaseQuickAdapter<BTCBillModel, BaseViewHolder> {

    public BTCBillListAdapter(@Nullable List<BTCBillModel> data) {
        super(R.layout.item_bill_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BTCBillModel item) {
        if (item == null) return;

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getTransDatetime(), DATE_MMddHHmm));
        helper.setImageResource(R.id.iv_type, getPrivateCoinStataIconByState(item.getDirection()));

        helper.setText(R.id.tv_amount, getMoneyStateByState(item.getDirection()) + AccountUtil.amountFormatUnitForShow(item.getValue(), WalletHelper.COIN_BTC, ETHSCALE) + " " + WalletHelper.COIN_BTC);

        if (isInState(item.getDirection())) {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.get_money));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.in_money));

        } else {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.transfer));

            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
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
