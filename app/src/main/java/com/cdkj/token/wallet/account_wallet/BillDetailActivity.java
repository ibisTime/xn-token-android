package com.cdkj.token.wallet.account_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.databinding.ActivityBillDetailBinding;
import com.cdkj.token.model.BillModel;

import java.math.BigDecimal;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**账单详情
 * Created by lei on 2017/10/26.
 */

public class BillDetailActivity extends AbsLoadActivity {

    private BillModel.ListBean bean;
    private ActivityBillDetailBinding mBinding;

    public static void open(Context context, BillModel.ListBean bean) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillDetailActivity.class)
                .putExtra(CdRouteHelper.DATASIGN, bean));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bill_detail, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_title_bill_detail));

        setStatusBarBlue();
        setTitleBgBlue();

        if (getIntent() != null) {
            bean = (BillModel.ListBean) getIntent().getSerializableExtra(CdRouteHelper.DATASIGN);
            if (bean != null) {
                setView();
            }

        }


    }

    private void setView() {
        mBinding.tvInfo.setText(bean.getBizNote());
        BigDecimal tas = new BigDecimal(bean.getTransAmountString());

        int i = tas.compareTo(BigDecimal.ZERO);
        if (i == 1) {
            mBinding.tvAmount.setText("+" + AmountUtil.amountFormatUnit(tas, bean.getCurrency(), 8) + " " + bean.getCurrency());
        } else {
            mBinding.tvAmount.setText(AmountUtil.amountFormatUnit(tas, bean.getCurrency(), 8) + " " + bean.getCurrency());
        }

        mBinding.tvBefore.setText(AmountUtil.amountFormatUnit(new BigDecimal(bean.getPreAmountString()), bean.getCurrency(), 8));
        mBinding.tvAfter.setText(AmountUtil.amountFormatUnit(new BigDecimal(bean.getPostAmountString()), bean.getCurrency(), 8));
        mBinding.tvDate.setText(DateUtil.formatStringData(bean.getCreateDatetime(), DEFAULT_DATE_FMT));
        mBinding.tvType.setText(AmountUtil.formatBizType(bean.getBizType()));
        mBinding.tvStatus.setText(AmountUtil.formatBillStatus(bean.getStatus()));
    }


}
