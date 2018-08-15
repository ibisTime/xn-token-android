package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.AmountUtil.ETHSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.getPrivateCoinStataIconByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.isInState;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class CoinBillListAdapter extends BaseQuickAdapter<LocalCoinBill, BaseViewHolder> {

    private String coinSymbol;


    public CoinBillListAdapter(@Nullable List<LocalCoinBill> data, String coinSymbol) {
        super(R.layout.item_bill_2, data);
        this.coinSymbol = coinSymbol;
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalCoinBill item) {
        if (item == null) return;

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getTransDatetime(), DATE_MMddHHmm));
        helper.setImageResource(R.id.iv_type, getPrivateCoinStataIconByState(item.getDirection()));

        helper.setText(R.id.tv_amount, getMoneyStateByState(item.getDirection()) + AmountUtil.amountFormatUnitForShow(item.getValue(), this.coinSymbol, ETHSCALE) + " " + this.coinSymbol);

        if (isInState(item.getDirection())) {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.get_money));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.in_money));
            helper.setText(R.id.tv_address, item.getFrom());
        } else {
            helper.setText(R.id.tv_remark, mContext.getString(R.string.transfer));
            helper.setText(R.id.tv_address, item.getTo());
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
        }

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
