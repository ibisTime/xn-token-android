package com.cdkj.token.user.question_feedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.QuestionHistoryListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityQuestionFeedbackHistoryBinding;
import com.cdkj.token.model.QuestionFeedbackModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 问题反馈列表
 * Created by cdkj on 2018/8/4.
 */

public class QuestionFeedbackHistoryListActivity extends AbsLoadActivity {

    private ActivityQuestionFeedbackHistoryBinding mbinding;

    private RefreshHelper mRefreshHelper;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QuestionFeedbackHistoryListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_question_feedback_history, null, false);
        return mbinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTitleBgBlue();
        setStatusBarBlue();
        mBaseBinding.titleView.setMidTitle(R.string.question_history);

        iniRFreshHelper();

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void iniRFreshHelper() {

        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mbinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mbinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                QuestionHistoryListAdapter questionHistoryListAdapter = new QuestionHistoryListAdapter(listData);
                questionHistoryListAdapter.setOnItemClickListener((adapter, view, position) -> {
                    QuestionFeedbackDetailsActivity.open(QuestionFeedbackHistoryListActivity.this, questionHistoryListAdapter.getItem(position).getCode());
                });
                return questionHistoryListAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

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
                        disMissLoading();
                    }
                });

            }
        });

        mRefreshHelper.init(10);

    }


}
