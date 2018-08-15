package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.token.R;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class BTCFromAddressListAdapter extends BaseQuickAdapter<BTCBillModel.VinBean, BaseViewHolder> {

    private String address;

    /**
     * @param data
     * @param address 用户进行请求的地址
     */
    public BTCFromAddressListAdapter(@Nullable List<BTCBillModel.VinBean> data, String address) {
        super(R.layout.item_coin_address, data);
        this.address = address;
    }

    @Override
    protected void convert(BaseViewHolder helper, BTCBillModel.VinBean item) {
        if (item == null) return;

        helper.setText(R.id.tv_address, item.getAddr());

        helper.setText(R.id.tv_amount, item.getValue() + WalletHelper.COIN_BTC);

        if (TextUtils.equals(item.getAddr(), address)) {
            helper.setTextColor(R.id.tv_address, ContextCompat.getColor(mContext, R.color.text_blue));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.text_blue));
        } else {
            helper.setTextColor(R.id.tv_address, ContextCompat.getColor(mContext, R.color.text_black_cd));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.text_black_cd));
        }
    }


}
