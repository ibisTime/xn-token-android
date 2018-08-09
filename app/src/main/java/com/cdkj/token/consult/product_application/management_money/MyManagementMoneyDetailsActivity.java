package com.cdkj.token.consult.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityMyManageMoneyDetailsBinding;

/**
 * 我的理财详情
 * Created by cdkj on 2018/8/9.
 */

public class MyManagementMoneyDetailsActivity extends AbsLoadActivity {

    private ActivityMyManageMoneyDetailsBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyManagementMoneyDetailsActivity.class);
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
    }


}
