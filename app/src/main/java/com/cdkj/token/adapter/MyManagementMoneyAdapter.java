package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 我的 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class MyManagementMoneyAdapter extends BaseQuickAdapter<MyManamentMoneyProduct, BaseViewHolder> {


    public MyManagementMoneyAdapter(@Nullable List<MyManamentMoneyProduct> data) {
        super(R.layout.item_my_management_money, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyManamentMoneyProduct item) {
        if (item == null || item.getProductInfo() == null) return;

        helper.setText(R.id.tv_name, item.getProductInfo().getName());
        helper.setText(R.id.tv_state, getStateString(item.getStatus()));
        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getCreateDatetime(), DEFAULT_DATE_FMT));
        helper.setText(R.id.tv_end_date, DateUtil.formatStringData(item.getProductInfo().getArriveDatetime(), DEFAULT_DATE_FMT));
        helper.setText(R.id.tv_buy_amount, AmountUtil.transformFormatToString(item.getInvestAmount(), item.getProductInfo().getSymbol(), AmountUtil.ALLSCALE) + item.getProductInfo().getSymbol());
        helper.setText(R.id.tv_income, AmountUtil.transformFormatToString(item.getExpectIncome(), item.getProductInfo().getSymbol(), AmountUtil.ALLSCALE) + item.getProductInfo().getSymbol());

    }


    /**
     * 获取状态描述
     *
     * @param state
     * @return
     */
    public String getStateString(String state) {

        if (TextUtils.isEmpty(state)) {
            return "";
        }

        switch (state) {
            case "0":
                return mContext.getString(R.string.product_buy_state_0);
            case "1":
                return mContext.getString(R.string.product_buy_state_1);
            case "2":
                return mContext.getString(R.string.product_buy_state_2);
            case "3":
                return mContext.getString(R.string.product_buy_state_3);
        }

        return "";

    }


}
