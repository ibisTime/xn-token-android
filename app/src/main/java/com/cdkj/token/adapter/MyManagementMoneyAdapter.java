package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;

/**
 * 我的 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class MyManagementMoneyAdapter extends BaseQuickAdapter<MyManamentMoneyProduct, BaseViewHolder> {


    public MyManagementMoneyAdapter(@Nullable List<MyManamentMoneyProduct> data) {
//        super(R.layout.item_my_management_money, data);
        super(R.layout.item_my_management_money_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyManamentMoneyProduct item) {
        if (item == null || item.getProductInfo() == null) return;

        helper.setText(R.id.tv_name, item.getProductInfo().getName());
//        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getCreateDatetime(), DEFAULT_DATE_FMT));
        helper.setText(R.id.tv_end_date, DateUtil.formatStringData(item.getProductInfo().getArriveDatetime(), DATE_YMD) + mContext.getString(R.string.product_end));
        helper.setText(R.id.tv_buy_amount, AmountUtil.transformFormatToString(item.getInvestAmount(), item.getProductInfo().getSymbol(), AmountUtil.ALLSCALE) + item.getProductInfo().getSymbol());
        helper.setText(R.id.tv_income, AmountUtil.transformFormatToString(item.getExpectIncome(), item.getProductInfo().getSymbol(), AmountUtil.SCALE_4) + item.getProductInfo().getSymbol());
        helper.setText(R.id.tv_income_rate, StringUtils.showformatPercentage(item.getProductInfo().getExpectYield()));

        getStateString(helper, item.getStatus());
    }


    /**
     * 获取状态描述
     *
     * @param state
     * @return
     */
    public void getStateString(BaseViewHolder helper, String state) {

        if (TextUtils.isEmpty(state)) {
            return;
        }

        switch (state) {
            case "0":
                helper.setBackgroundRes(R.id.tv_status, R.drawable.bg_item_invest_status);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.blue_0064ff));
                helper.setText(R.id.tv_status, mContext.getString(R.string.product_buy_state_0));
                break;

            case "1":
                helper.setBackgroundRes(R.id.tv_status, R.drawable.bg_item_invest_status);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.blue_0064ff));
                helper.setText(R.id.tv_status, mContext.getString(R.string.product_buy_state_1));
                break;

            case "2":
                helper.setBackgroundRes(R.id.tv_status, R.drawable.bg_item_invest_status_gray);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.gray_999999));
                helper.setText(R.id.tv_status, mContext.getString(R.string.product_buy_state_2));
                break;

            case "3":
                helper.setBackgroundRes(R.id.tv_status, R.drawable.bg_item_invest_status_gray);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.gray_999999));
                helper.setText(R.id.tv_status, mContext.getString(R.string.product_buy_state_3));
                break;

        }

    }


}
