package com.cdkj.token.user.credit;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreditScoreDetailBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cdkj on 2018/9/17.
 */

public class CreditActivity extends AbsLoadActivity {

    private ActivityCreditScoreDetailBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreditActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_credit_score_detail, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.credit_score));

        setStatusBarBlue();
        setTitleBgBlue();

        mBinding.btnAdd.setOnClickListener(view -> {
            EventBus.getDefault().post(new AllFinishEvent());
            MainActivity.open(this);
            finish();
        });

    }
}
