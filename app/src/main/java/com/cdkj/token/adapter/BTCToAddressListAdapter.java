package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.BTCBillModel;
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

public class BTCToAddressListAdapter extends BaseQuickAdapter<BTCBillModel.VoutBean, BaseViewHolder> {
    private String address;

    public BTCToAddressListAdapter(@Nullable List<BTCBillModel.VoutBean> data, String address) {
        super(R.layout.item_coin_address, data);
        this.address = address;
    }

    @Override
    protected void convert(BaseViewHolder helper, BTCBillModel.VoutBean item) {
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
