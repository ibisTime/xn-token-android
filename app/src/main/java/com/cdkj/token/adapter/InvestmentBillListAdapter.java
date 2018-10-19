package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.InvestBillModel;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class InvestmentBillListAdapter extends BaseQuickAdapter<InvestBillModel, BaseViewHolder> {


    public InvestmentBillListAdapter(@Nullable List<InvestBillModel> data) {
        super(R.layout.item_investment_bill, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvestBillModel item) {

        helper.setText(R.id.tv_name, item.getProductName());
        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getCreateDatetime(), DateUtil.DATE_MMddHHmm));

        BigDecimal transAmount = new BigDecimal(item.getTransAmountString());
        int i = transAmount.compareTo(BigDecimal.ZERO);
        if (i == 1) {
            helper.setText(R.id.tv_transAmount, "+" + AmountUtil.transformFormatToString(transAmount, item.getCurrency(), 8)+" "+item.getCurrency());
            helper.setTextColor(R.id.tv_transAmount, ContextCompat.getColor(mContext, R.color.oragne_ff8000));
        } else {
            helper.setText(R.id.tv_transAmount, AmountUtil.transformFormatToString(transAmount, item.getCurrency(), 8)+" "+item.getCurrency());
            helper.setTextColor(R.id.tv_transAmount, ContextCompat.getColor(mContext, R.color.green_46aaaf));
        }

        BigDecimal postAmount = new BigDecimal(item.getPostAmountString());
        helper.setText(R.id.tv_postAmount, mContext.getString(R.string.balance)+AmountUtil.transformFormatToString(postAmount, item.getCurrency(), 8)+" "+item.getCurrency());

    }
}
