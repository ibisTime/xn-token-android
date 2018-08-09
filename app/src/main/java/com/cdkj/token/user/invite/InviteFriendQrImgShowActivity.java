package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteFriendQrcodeBinding;

/**
 * 邀请好友二维码
 * Created by cdkj on 2018/8/9.
 */

public class InviteFriendQrImgShowActivity extends BaseActivity {

    private ActivityInviteFriendQrcodeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteFriendQrImgShowActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_qrcode);
        UIStatusBarHelper.setStatusBarLightMode(this);
        UIStatusBarHelper.translucent(this);

        mBinding.imgBack.setOnClickListener(view -> finish());

    }


}
