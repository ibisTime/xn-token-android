package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.RedPacketDetailListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityRedpacketDetailsBinding;
import com.cdkj.token.model.RedPacketDetails;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 红包详情
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketDetailsActivity extends AbsLoadActivity {

    private ActivityRedpacketDetailsBinding mBinding;

    private String redPacketCode;

    public static void open(Context context, String redpacketCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPacketDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, redpacketCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_redpacket_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        UIStatusBarHelper.setStatusBarDarkMode(this);
        setStatusBarColor(ContextCompat.getColor(this, R.color.red));
        redPacketCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        getRedPacketDetails();

        mBinding.imgFinish.setOnClickListener(view -> finish());

        mBinding.imgMore.setOnClickListener(view -> RedPacketShareQRActivity.open(this, redPacketCode, false));

    }


    /**
     *
     */
    public void getRedPacketDetails() {

        showLoadingDialog();
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("code", redPacketCode);

        Call<BaseResponseModel<RedPacketDetails>> call = RetrofitUtils.createApi(MyApi.class).getRedPacketHistoryDetail("623006", StringUtils.objectToJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<RedPacketDetails>(this) {
            @Override
            protected void onSuccess(RedPacketDetails data, String SucMessage) {

                setShowData(data);

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    private void setShowData(RedPacketDetails data) {

        if (data == null) return;

        if (data.getReceivedNum() < data.getSendNum()) {
            mBinding.imgMore.setVisibility(View.VISIBLE);
        } else {
            mBinding.imgMore.setVisibility(View.GONE);
        }

        mBinding.tvUserName.setText(data.getSendUserNickname());

        ImgUtils.loadLogo(this, data.getSendUserPhoto(), mBinding.imgLogo);

        mBinding.tvSendNum.setText(data.getReceivedNum() + "/" + data.getSendNum());

        mBinding.tvTotlaCount.setText(data.getReceivedCount() + "/" + data.getTotalCount());


        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recycler.setAdapter(new RedPacketDetailListAdapter(data.getReceiverList()));


        mBinding.recycler.setNestedScrollingEnabled(false);
    }


}
