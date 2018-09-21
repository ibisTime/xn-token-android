package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityInviteBinding;
import com.cdkj.token.find.product_application.red_package.SendRedPacketActivity;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.RecommendAppModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.common.ThaAppConstant.getCoinGameUrl;
import static com.cdkj.token.find.FindFragment.RED_PACKET;

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
        mBinding.tvInviteNumber.setText("0");
        mBinding.tvInviteAmount.setText(Html.fromHtml(getString(R.string.invite_integral_1, "0")));

        mGetUserInfoPresenter = new UserInfoPresenter(this, this);
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.drawable.invite_bg);

        setClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGetUserInfoPresenter != null) {
            mGetUserInfoPresenter.getUserInfoRequest();
        }
    }

    private void setClickListener() {

        mBinding.imgMore.setOnClickListener(view -> {
            InvieMenuSelectActivity.open(this);
        });

        //
        mBinding.imgFinish.setOnClickListener(view -> finish());

        // 去提币
        mBinding.llGoLottery.setOnClickListener(view -> {
            getCoinUrl();
        });

        //海报邀请
        mBinding.btnInvitePoster.setOnClickListener(view -> {
            InviteQrActivity.open(this);
        });

        //发红包邀请好友
        mBinding.btnInviteRed.setOnClickListener(view -> {
            getApplistAndOpenRedPacket();
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

    /**
     *
     */
    public void getCoinUrl() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                WebViewActivity.openURL(InviteActivity.this, getStrRes(R.string.lottery), data.getCvalue() + getCoinGameUrl());
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }


    /**
     * 获取应用列表打开红包
     */
    public void getApplistAndOpenRedPacket() {

        Map<String, String> map = new HashMap<>();

        map.put("language", SPUtilHelper.getLanguage());
        map.put("location", "0");
        map.put("status", "1");

        showLoadingDialog();

        Call<BaseResponseListModel<RecommendAppModel>> call = RetrofitUtils.createApi(MyApi.class).getAppList("625412", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseListCallBack<RecommendAppModel>(this) {
            @Override
            protected void onSuccess(List<RecommendAppModel> data, String SucMessage) {

                for (RecommendAppModel datum : data) {
                    if (datum == null) continue;
                    if (TextUtils.equals(datum.getAction(), RED_PACKET)) {
                        SendRedPacketActivity.open(InviteActivity.this, datum.getCode());
                        break;
                    }
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }
}
