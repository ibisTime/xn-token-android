package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.BjbMyIncome;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class MyIncomeAdapter extends BaseQuickAdapter<BjbMyIncome.Top5, BaseViewHolder> {


    public MyIncomeAdapter(@Nullable List<BjbMyIncome.Top5> data) {
        super(R.layout.item_my_income, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BjbMyIncome.Top5 item) {
        if (item == null) return;

        helper.setText(R.id.tv_no, "NO."+item.getRank());
        helper.setText(R.id.tv_mobile, item.getMobile());
        helper.setText(R.id.tv_incomeTotal, AmountUtil.transformFormatToString2(item.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4)+" BTC");

    }

}
