package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserJoinBinding;

/**
 * Created by cdkj on 2018/5/26.
 */

public class UserJoinActivity extends AbsBaseActivity {

    private ActivityUserJoinBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserJoinActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_join, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getStrRes(R.string.user_title_join));
        setSubLeftImgState(true);

        mBinding.tvFacebook.setText("@thahoffchaj");
        mBinding.tvTwitter.setText("@thasddoddfvb");
    }
}
