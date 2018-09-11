package com.cdkj.token.user.question_feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.QuestionHistoryListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.AbsRefreshClipListActivity;
import com.cdkj.token.model.QuestionFeedbackModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 问题反馈列表
 * Created by cdkj on 2018/8/4.
 */

public class QuestionFeedbackHistoryListActivity extends AbsRefreshClipListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QuestionFeedbackHistoryListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        QuestionHistoryListAdapter questionHistoryListAdapter = new QuestionHistoryListAdapter(listData);
        questionHistoryListAdapter.setOnItemClickListener((adapter, view, position) -> {
            QuestionFeedbackDetailsActivity.open(QuestionFeedbackHistoryListActivity.this, questionHistoryListAdapter.getItem(position).getCode());
        });
        return questionHistoryListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (isShowDialog) showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<QuestionFeedbackModel>>> call = RetrofitUtils.createApi(MyApi.class).getQuestionFeedbackList("805107", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<QuestionFeedbackModel>>(QuestionFeedbackHistoryListActivity.this) {
            @Override
            protected void onSuccess(ResponseInListModel<QuestionFeedbackModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_question_history), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitleBgBlue();
        setStatusBarBlue();
        mBaseBinding.titleView.setMidTitle(R.string.question_history);
        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);
    }


}
