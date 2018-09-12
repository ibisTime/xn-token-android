package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteRuleBinding;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteRuleActivity extends AbsStatusBarTranslucentActivity {

    private ActivityInviteRuleBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteRuleActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite_rule, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.mipmap.invite_bg);

    }

}
