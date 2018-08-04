package com.cdkj.token.user.question_feedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.QuestionHistoryListAdapter;
import com.cdkj.token.adapter.QuestionPhotoAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityQuestionFeedbackDetailsBinding;
import com.cdkj.token.model.QuestionFeedbackModel;
import com.cdkj.token.utils.AccountUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;

/**
 * 问题反馈详情
 * Created by cdkj on 2018/8/4.
 */

public class QuestionFeedbackDetailsActivity extends AbsLoadActivity {

    private ActivityQuestionFeedbackDetailsBinding mBinding;

    private String mDetailsCode;//详情编号

    /**
     * @param context
     */
    public static void open(Context context, String detailsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QuestionFeedbackDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, detailsCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_question_feedback_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTitleBgBlue();

        setStatusBarBlue();

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.question_details));

        if (getIntent() != null) {
            mDetailsCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        getDetails();

    }

    private void getDetails() {

        if (TextUtils.isEmpty(mDetailsCode)) return;


        Map<String, String> map = new HashMap<>();

        map.put("code", mDetailsCode);

        showLoadingDialog();

        Call<BaseResponseModel<QuestionFeedbackModel>> call = RetrofitUtils.createApi(MyApi.class).getQuestionFeedbackDetails("805106", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<QuestionFeedbackModel>(QuestionFeedbackDetailsActivity.this) {
            @Override
            protected void onSuccess(QuestionFeedbackModel data, String SucMessage) {
                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 设置显示数据
     *
     * @param data
     */
    private void setShowData(QuestionFeedbackModel data) {
        if (data == null) return;
        mBinding.tvFrom.setText(getFrom(data.getDeviceSystem()));
        mBinding.tvQuestionTro.setText(data.getDescription());
        mBinding.tvShowStep.setText(data.getReappear());
        mBinding.tvRemark.setText(data.getCommitNote());
        mBinding.tvBugState.setText(getLevelString(data.getLevel()));
        mBinding.tvFinalLevel.setText(data.getLevel());
        mBinding.tvMoney.setText(AccountUtil.amountFormatUnitForShow(data.getPayAmount(), AccountUtil.ALLSCALE) + "wan");
        mBinding.tvCommitTime.setText(DateUtil.formatStringData(data.getCommitDatetime(), DATE_YMD));

        mBinding.recycler.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recycler.setAdapter(new QuestionPhotoAdapter(StringUtils.splitBannerList(data.getPic())));
    }

    /**
     * @param deviceSystem
     * @return
     */
    private String getFrom(String deviceSystem) {
        if (TextUtils.isEmpty(deviceSystem)) {
            return "";
        }

        switch (deviceSystem.toLowerCase()) {
            case "ios":
                return getStrRes(R.string.ios_text);
            case "android":
                return getStrRes(R.string.android_txt);
            case "h5":
                return getStrRes(R.string.h5_text);
        }

        return "";
    }


    /**
     * 等级 //1严重 2 一般 3 优化
     *
     * @param level
     * @return
     */
    public String getLevelString(String level) {
        if (TextUtils.isEmpty(level)) {
            return "";
        }

        switch (level) {
            case "1":
                return getString(R.string.bug_level_1);
            case "2":
                return getString(R.string.bug_level_2);
            case "3":
                return getString(R.string.bug_level_3);
        }

        return "";
    }

}
