package com.cdkj.token.find.product_application.question;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.QuestionlistAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityQuestionAnswerDetailsBinding;
import com.cdkj.token.databinding.ActivityQuestionDetailsBinding;
import com.cdkj.token.model.AppQuestionModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 应用问题列表
 * Created by cdkj on 2018/9/14.
 */

public class ApplicationQuestionAnswerActivity extends AbsLoadActivity {

    private ActivityQuestionAnswerDetailsBinding mBinding;


    public static void open(Context context, AppQuestionModel.HelpListBean helpListBean, String title) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ApplicationQuestionAnswerActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, helpListBean);
        intent.putExtra(CdRouteHelper.DATASIGN2, title);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_question_answer_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getIntent().getStringExtra(CdRouteHelper.DATASIGN2));

        AppQuestionModel.HelpListBean helpListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (helpListBean == null) return;
        mBinding.tvQuestion.setText(helpListBean.getQuestion());

        mBinding.webviewAnswer.loadData(helpListBean.getAnswer(), "text/html;charset=UTF-8", "UTF-8");


    }

}
