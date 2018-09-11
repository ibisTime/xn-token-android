package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityRedPackageShearBinding;
import com.cdkj.token.model.RedPackageDetialsBean;
import com.cdkj.token.model.RedPackageEventBusBean;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 红包分享
 */
public class RedPackageShearActivity extends AbsLoadActivity {
    ActivityRedPackageShearBinding mBinding;
    private String currentCode;

    public static void open(Context context, String currentCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPackageShearActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, currentCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_red_package_shear, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.red_packet_status_bar));

        if (getIntent() != null) {
            currentCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        }
        initOnclick();
        initDates();

    }

    private void initDates() {
        if (TextUtils.isEmpty(currentCode)) return;
        showLoadingDialog();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("code", currentCode);
        Call<BaseResponseModel<RedPackageDetialsBean>> redPackageDetails = RetrofitUtils.createApi(MyApi.class).getRedPackageDetails("623006", StringUtils.getJsonToString(map));
        redPackageDetails.enqueue(new BaseResponseModelCallBack<RedPackageDetialsBean>(this) {
            @Override
            protected void onSuccess(RedPackageDetialsBean data, String SucMessage) {

                mBinding.tvRedPacketTip.setVisibility(View.VISIBLE);
                mBinding.llOpen.setVisibility(View.VISIBLE);

                mBinding.tvUserName.setText(data.getSendUserNickname());
                mBinding.tvMasg.setText(data.getGreeting());

                SPUtilHelper.saveUserPhoto(data.getSendUserPhoto());

                ImgUtils.loadLogo(RedPackageShearActivity.this, data.getSendUserPhoto(), mBinding.imgLogo);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    private void initOnclick() {
        mBinding.llOpen.setOnClickListener(view -> {
            QRRedPackageImgActivity.open(this, currentCode);
            //隐藏出去背景图片相关元素
            mBinding.imgLogo.setVisibility(View.INVISIBLE);
            mBinding.tvUserName.setVisibility(View.INVISIBLE);
            mBinding.tvRedPacketTip.setVisibility(View.INVISIBLE);
            mBinding.tvMasg.setVisibility(View.INVISIBLE);
            mBinding.llOpen.setVisibility(View.INVISIBLE);
        });
        mBinding.rllTop.ivBack.setOnClickListener(view -> {
            finish();
        });
        mBinding.rllTop.tvTopRightTitle.setVisibility(View.GONE);
        mBinding.rllTop.tvTopTitle.setText(R.string.my_red_package);
    }

    @Subscribe()
    public void onMessageEvent(RedPackageEventBusBean event) {
        finish();
    }
}
