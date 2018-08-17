package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ProgressBar;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class ManagementMoneyListAdapter extends BaseQuickAdapter<ManagementMoney, BaseViewHolder> {


    public ManagementMoneyListAdapter(@Nullable List<ManagementMoney> data) {
        super(R.layout.item_manager_money, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ManagementMoney item) {

        BigDecimal unit = LocalCoinDBUtils.getLocalCoinUnit(item.getSymbol());

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_income, StringUtils.showformatPercentage(item.getExpectYield()));
        helper.setText(R.id.tv_time, mContext.getString(R.string.product_days, item.getLimitDays() + ""));


        helper.setText(R.id.tv_min_money, Html.fromHtml(mContext.getString(R.string.product_buy_minamount,
                AmountUtil.amountFormatUnitForShow(item.getMinAmount(), unit, AmountUtil.ALLSCALE) + item.getSymbol())));

        helper.setText(R.id.tv_state, AmountUtil.amountFormatUnitForShow(item.getAvilAmount(), unit, AmountUtil.ALLSCALE));

        ProgressBar progressBar = helper.getView(R.id.progressbar);
        BigDecimal f = BigDecimalUtils.div(item.getAmount(), item.getSaleAmount(), 2);
        progressBar.setProgress(f.intValue());

        helper.setText(R.id.tv_buy_totla_ratio, StringUtils.showformatPercentage(f.floatValue()));

    }


}
