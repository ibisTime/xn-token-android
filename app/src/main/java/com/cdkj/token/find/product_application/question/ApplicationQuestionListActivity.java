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
import com.cdkj.token.databinding.ActivityQuestionDetailsBinding;
import com.cdkj.token.model.AppQuestionModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 应用问题列表
 * Created by cdkj on 2018/9/14.
 */

public class ApplicationQuestionListActivity extends AbsLoadActivity {

    private ActivityQuestionDetailsBinding mBinding;

    private String appCode;
    private String title;

    /**
     *
     * @param context
     * @param appCode 应用编号
     * @param title   界面title
     */
    public static void open(Context context, String appCode,String title) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ApplicationQuestionListActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, appCode);
        intent.putExtra(CdRouteHelper.DATASIGN2, title);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_question_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        appCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        title = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);
        mBaseBinding.titleView.setMidTitle(title);
        getQuestion();
    }

    /**
     * 获取问题列表
     */
    public void getQuestion() {

        Map<String, String> map = new HashMap<>();

        map.put("code", appCode);

        Call<BaseResponseModel<AppQuestionModel>> call = RetrofitUtils.createApi(MyApi.class).getAPPQuestionList("625413", StringUtils.objectToJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<AppQuestionModel>(this) {
            @Override
            protected void onSuccess(AppQuestionModel data, String SucMessage) {
                if (data.getDapp() != null) {
                    mBinding.tvQuestion.setText(data.getDapp().getDescription());
                }


                mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(ApplicationQuestionListActivity.this, LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });


                QuestionlistAdapter questionlistAdapter = new QuestionlistAdapter(data.getHelpList());

                mBinding.recyclerView.setAdapter(questionlistAdapter);

                questionlistAdapter.setOnItemClickListener((adapter, view, position) -> {
                    ApplicationQuestionAnswerActivity.open(ApplicationQuestionListActivity.this,questionlistAdapter.getItem(position),title);
                });


            }

            @Override
            protected void onFinish() {

            }

        });

    }

}
