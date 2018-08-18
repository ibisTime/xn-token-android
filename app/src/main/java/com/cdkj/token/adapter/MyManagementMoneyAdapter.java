package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
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
        if (item == null) return;

        helper.setText(R.id.tv_name, "xxx");
        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getCreateDatetime(), DEFAULT_DATE_FMT));
        helper.setText(R.id.tv_end_date, DateUtil.formatStringData(item.getLastInvestDatetime(), DEFAULT_DATE_FMT));
//        helper.setText(R.id.tv_buy_amount, AmountUtil.amountFormatUnitForShow(item.getInvestAmount(),item.getc ));


    }


}
