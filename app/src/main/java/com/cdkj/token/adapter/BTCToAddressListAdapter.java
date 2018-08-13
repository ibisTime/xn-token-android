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

    public BTCToAddressListAdapter(@Nullable List<BTCBillModel.VoutBean> data) {
        super(R.layout.item_coin_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BTCBillModel.VoutBean item) {
        if (item == null) return;

        helper.setText(R.id.tv_address, item.getAddr());
    }


}
