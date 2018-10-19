package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.IncomeRankTopModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 收益排行Top100
 * Created by cdkj on 2018/5/25.
 */

public class IncomeRankAdapter extends BaseQuickAdapter<IncomeRankTopModel.Top100Bean, BaseViewHolder> {


    public IncomeRankAdapter(@Nullable List<IncomeRankTopModel.Top100Bean> data) {
        super(R.layout.item_my_income, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IncomeRankTopModel.Top100Bean item) {
        if (item == null) return;


        helper.setText(R.id.tv_no, "NO."+item.getRank());
        helper.setText(R.id.tv_mobile, item.getMobile());
        helper.setText(R.id.tv_incomeTotal, AmountUtil.transformFormatToString2(item.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4)+" BTC");

    }

}
