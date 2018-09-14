package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteRuleBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

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
        mBaseBinding.fraLayoutTitle.setVisibility(View.GONE);
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.mipmap.invite_bg);
        getinviteRule();
        getinviteRule2();

        mBinding.tvFinish.setOnClickListener(view -> finish()   );
    }


    /**
     *
     */
    public void getinviteRule() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "invite_add");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.objectToJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                mBinding.tvInviteRule.setText(getString(R.string.invite_26, data.getCvalue()));
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    public void getinviteRule2() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "register_add");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.objectToJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                mBinding.tvInviteRule2.setText(getString(R.string.invite_32, data.getCvalue()));
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }


}
