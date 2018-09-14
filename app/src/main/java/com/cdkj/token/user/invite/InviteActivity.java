package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteBinding;
import com.cdkj.token.find.product_application.red_package.SendRedPackageActivity;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteActivity extends AbsStatusBarTranslucentActivity implements UserInfoInterface {

    private ActivityInviteBinding mBinding;
    private UserInfoPresenter mGetUserInfoPresenter;//获取用户信息

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.fraLayoutTitle.setVisibility(View.GONE);

        mGetUserInfoPresenter = new UserInfoPresenter(this, this);
        mGetUserInfoPresenter.getUserInfoRequest();
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.mipmap.invite_bg);

        setClickListener();

    }

    private void setClickListener() {

        mBinding.imgMore.setOnClickListener(view -> {
            InvieMenuSelectActivity.open(this);
        });

        //
        mBinding.imgFinish.setOnClickListener(view -> finish());

        // 去提币
        mBinding.llGoLottery.setOnClickListener(view -> {

        });

        //海报邀请
        mBinding.btnInvitePoster.setOnClickListener(view -> {
            InviteQrActivity.open(this);
        });

        //发红包邀请好友
        mBinding.btnInviteRed.setOnClickListener(view -> {
            SendRedPackageActivity.open(this);
        });

    }


    @Override
    public void onStartGetUserInfo() {
        showLoadingDialog();
    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        disMissLoadingDialog();
        if (userInfo == null) return;

        mBinding.tvInviteNumber.setText(userInfo.getJfInviteNumber() + "");
        mBinding.tvInviteAmount.setText(Html.fromHtml(getString(R.string.invite_integral_1, userInfo.getJfAmount() + "")));


    }
}
