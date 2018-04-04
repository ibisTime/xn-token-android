package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityNoneBinding;

/**
 * Created by lei on 2018/3/29.
 */

public class NoneActivity extends AbsBaseActivity{
    private ActivityNoneBinding mBinding;

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity, String location) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, NoneActivity.class);
        intent.putExtra("location", location);
        activity.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_none, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getString(R.string.consult_none_title));
        setSubLeftImgState(true);
        setTopLineState(true);

        if (getIntent() == null)
            return;

        if (getIntent().getStringExtra("location").equals("mall")){
            mBinding.ivNone.setImageResource(R.mipmap.none_mall);
        }else {
            mBinding.ivNone.setImageResource(R.mipmap.none_dig);
        }
    }
}
