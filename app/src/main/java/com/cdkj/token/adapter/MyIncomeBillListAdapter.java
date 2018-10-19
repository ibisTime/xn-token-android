package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.find.product_application.management_money.MyIncomeBillListActivity;
import com.cdkj.token.model.InvestBillModel;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class MyIncomeBillListAdapter extends BaseQuickAdapter<InvestBillModel, BaseViewHolder> {


    MyIncomeBillListActivity mActivity;
    List<InvestBillModel> mData;

    public MyIncomeBillListAdapter(@Nullable List<InvestBillModel> data, MyIncomeBillListActivity activity) {
        super(R.layout.item_my_income_bill, data);
        mData = data;
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, InvestBillModel item) {

        if (helper.getLayoutPosition() == 0){
            helper.setGone(R.id.tv_date, true);
        } else {

            Date lastDate = new Date(mData.get(helper.getLayoutPosition()-1).getCreateDatetime());
            Date nowDate = new Date(mData.get(helper.getLayoutPosition()).getCreateDatetime());

            if (DateUtil.isSameDate(nowDate,lastDate)){
                helper.setGone(R.id.tv_date, false);
            } else {
                helper.setGone(R.id.tv_date, true);
            }
        }

        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getCreateDatetime(), DateUtil.DATE_TEMPLATE1));
        helper.setText(R.id.tv_incomeTotal, "+" + AmountUtil.transformFormatToString(new BigDecimal(item.getTransAmountString()), item.getCurrency(), 8)+" "+item.getCurrency());

        ImageView ivIcon = helper.getView(R.id.iv_icon);

        if (item.getBizType().equals("invite_income_pop_in")){
            ImgUtils.loadActImgId(mActivity, R.drawable.income_in, ivIcon);
            helper.setText(R.id.tv_mobile, item.getRefUserMobile());
        }

        if (item.getBizType().equals("jf_lottery_in")){
            ImgUtils.loadActImgId(mActivity, R.drawable.lottery_in, ivIcon);
            helper.setText(R.id.tv_mobile, item.getBizNote());
        }

    }
}
