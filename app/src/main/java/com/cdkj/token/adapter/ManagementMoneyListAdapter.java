package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
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

        if (item == null) return;


        BigDecimal unit = LocalCoinDBUtils.getLocalCoinUnit(item.getSymbol());

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_income, StringUtils.showformatPercentage(item.getExpectYield()));
        helper.setText(R.id.tv_time, mContext.getString(R.string.product_days, item.getLimitDays() + ""));

        helper.setText(R.id.tv_min_money, Html.fromHtml(mContext.getString(R.string.product_buy_minamount,
                AmountUtil.amountFormatUnitForShow(item.getMinAmount(), unit, AmountUtil.ALLSCALE) + item.getSymbol())));

        helper.setText(R.id.tv_state, getStateString(item, unit));

        ProgressBar progressBar = helper.getView(R.id.progressbar);
        BigDecimal f = BigDecimalUtils.div(item.getSaleAmount(), item.getAmount(), 2);
        progressBar.setProgress((int) (f.floatValue() * 100));

        helper.setText(R.id.tv_buy_totla_ratio, StringUtils.showformatPercentage(f.floatValue()));

        helper.setVisible(R.id.view_state_end, isEnd(item.getStatus())); //已结束显示灰色

    }

    /*（0草稿，1待审核，2审核通过，3审核不通过，4即将开始，5募集期，6停止交易，7产品封闭期，8还款成功，9募集失败)*/

    /**
     * 是否结束
     *
     * @return
     */
    public boolean isEnd(String state) {
        return TextUtils.equals(state, "8") || TextUtils.equals(state, "9");
    }

    /**
     * 根据状态显示
     *
     * @param data
     */
    public CharSequence getStateString(ManagementMoney data, BigDecimal unit) {

        if (TextUtils.isEmpty(data.getStatus())) {
            return "";
        }

        switch (data.getStatus()) {
            case "4":
                return mContext.getString(R.string.management_money_state_4);
            case "5":
                return Html.fromHtml(mContext.getString(R.string.product_buy_end, AmountUtil.amountFormatUnitForShow(data.getAvilAmount(), unit, AmountUtil.ALLSCALE) + data.getSymbol()));
            case "6":

                if (BigDecimalUtils.compareEqualsZERO(data.getAvilAmount())) {
                    return mContext.getString(R.string.management_money_state_6);
                } else {
                    return mContext.getString(R.string.management_money_state_10);
                }

            case "7":
                return mContext.getString(R.string.management_money_state_7);
            case "8":
                return mContext.getString(R.string.management_money_state_8);
            case "9":
                return mContext.getString(R.string.management_money_state_9);
        }

        return "";

    }


}
