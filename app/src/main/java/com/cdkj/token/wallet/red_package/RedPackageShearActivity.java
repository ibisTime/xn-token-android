package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
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
public class RedPackageShearActivity extends AbsBaseLoadActivity {
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
        if (getIntent() != null) {
            currentCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
            if (TextUtils.isEmpty(currentCode)) {
                finish();
            }
        } else {
            finish();
        }
        initOnclick();
        initDates();

    }

    private void initDates() {
        showLoadingDialog();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("code", currentCode);
        Call<BaseResponseModel<RedPackageDetialsBean>> redPackageDetails = RetrofitUtils.createApi(MyApi.class).getRedPackageDetails("623006", StringUtils.getJsonToString(map));
        redPackageDetails.enqueue(new BaseResponseModelCallBack<RedPackageDetialsBean>(this) {
            @Override
            protected void onSuccess(RedPackageDetialsBean data, String SucMessage) {

                mBinding.tvUserName.setText(data.getSendUserNickname());
                mBinding.tvMasg.setText(data.getGreeting());
                ImgUtils.loadLogo(RedPackageShearActivity.this, data.getSendUserPhoto(), mBinding.imgLogo);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initOnclick() {
        mBinding.llOpen.setOnClickListener(view -> {
            QRRedPackageImgActivity.open(this,currentCode);
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
