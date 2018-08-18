package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityMyManageMoneyDetailsBinding;
import com.cdkj.token.model.MyManamentMoneyProduct;

/**
 * 我的理财详情
 * Created by cdkj on 2018/8/9.
 */

public class MyManagementMoneyDetailsActivity extends AbsLoadActivity {

    private ActivityMyManageMoneyDetailsBinding mBinding;

    private MyManamentMoneyProduct moneyProduct;

    public static void open(Context context, MyManamentMoneyProduct product) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyManagementMoneyDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, product);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_manage_money_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarBlue();
        setTitleBgBlue();

        if (getIntent() != null) {
            moneyProduct = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        }

        setShowData(moneyProduct);


    }

    /**
     * @param moneyProduct
     */
    private void setShowData(MyManamentMoneyProduct moneyProduct) {

        if (moneyProduct == null) {
            return;
        }

        mBinding.tvCode.setText(moneyProduct.getCode());
        mBinding.tvTransctionTime.setText(DateUtil.formatStringData(moneyProduct.getCreateDatetime(), DateUtil.DEFAULT_DATE_FMT));
        mBinding.tvProductLimit.setText("xx");
        mBinding.tvIncomeRate.setText("xx");

        mBinding.tvBuyAmount.setText("xx");
        mBinding.tvIncome.setText("xx");
        mBinding.tvStartTime.setText("xx");
        mBinding.tvEndTime.setText("xx");

    }


}
