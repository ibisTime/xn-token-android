package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteFriendBinding;
import com.cdkj.token.find.product_application.red_package.SendRedPackageActivity;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteFriendActivity extends AbsStatusBarTranslucentActivity {

    private ActivityInviteFriendBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite_friend, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.drawable.invite_bg_1);

        setClickListener();

    }

    private void setClickListener() {

        //海报邀请
        mBinding.tvQrImg.setOnClickListener(view -> {
            InviteFriendQrImgShowActivity.open(this);
        });

        //发红包邀请好友
        mBinding.tvRedPacket.setOnClickListener(view -> {
            SendRedPackageActivity.open(this);
        });

    }


}
