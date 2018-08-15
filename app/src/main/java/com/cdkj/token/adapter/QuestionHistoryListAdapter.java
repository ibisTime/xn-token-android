package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.QuestionFeedbackModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 问题历史
 * Created by cdkj on 2018/5/25.
 */

public class QuestionHistoryListAdapter extends BaseQuickAdapter<QuestionFeedbackModel, BaseViewHolder> {


    public QuestionHistoryListAdapter(List<QuestionFeedbackModel> datas) {
        super(R.layout.layout_question_feedback_history, datas);
    }


    @Override
    protected void convert(BaseViewHolder helper, QuestionFeedbackModel item) {

        helper.setText(R.id.tv_name, SPUtilHelper.getUserName());
        helper.setText(R.id.tv_state, getStateString(item.getStatus()));
        helper.setText(R.id.tv_result, getResultString(item));
        helper.setText(R.id.tv_time, getTimeString(item));
        helper.setTextColor(R.id.tv_state, getStateColor(item.getStatus()));
    }


    /**
     * 状态颜色
     *
     * @param status
     * @return
     */
    public @ColorInt
    int getStateColor(String status) {
        if (TextUtils.isEmpty(status)) {
            return ContextCompat.getColor(mContext, R.color.gray_999999);
        }

        switch (status) {
            case "0":
                return Color.parseColor("#007AFF");
            case "1":
                return ContextCompat.getColor(mContext, R.color.gray_999999);
            case "2":
                return Color.parseColor("#FE4F4F");
            case "3":
                return ContextCompat.getColor(mContext, R.color.gray_999999);
        }

        return ContextCompat.getColor(mContext, R.color.gray_999999);
    }

    /*
      * 待确认 提交时间
      * 复现不成功 已确认  审批时间
      * 以奖励  支付时间

       * 时间
       *
       * @param item
       * @return
       */
    public String getTimeString(QuestionFeedbackModel item) {

        if (TextUtils.isEmpty(item.getStatus())) {
            return "";
        }

        String dataTime = "";
        switch (item.getStatus()) {
            case "0":
                dataTime = item.getCommitDatetime();
                break;
            case "1":
                dataTime = item.getApproveDatetime();
                break;
            case "2":
                dataTime = item.getCommitDatetime();
                break;
            case "3":
                dataTime = item.getPayDatetime();
                break;
        }


        return DateUtil.formatStringData(dataTime, "MM-dd HH:mm");
    }


    /**
     * 状态结果
     *
     * @param questionFeedbackModel
     * @return
     */
    public CharSequence getResultString(QuestionFeedbackModel questionFeedbackModel) {
        if (TextUtils.isEmpty(questionFeedbackModel.getStatus())) {
            return "";
        }

        switch (questionFeedbackModel.getStatus()) {
            case "0":
                return mContext.getString(R.string.question_state_5);
            case "1":
                return mContext.getString(R.string.question_state_8);
            case "2":
                return mContext.getString(R.string.question_state_4) + "0";
            case "3":
                String money = AmountUtil.amountFormatUnitForShow(questionFeedbackModel.getPayAmount(), WalletHelper.COIN_WAN, AmountUtil.ALLSCALE) + "wan";
                return Html.fromHtml(mContext.getString(R.string.question_done,
                        money, getLevelString(questionFeedbackModel.getLevel()), questionFeedbackModel.getRepairVersionCode()));
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
                return mContext.getString(R.string.bug_level_1);
            case "2":
                return mContext.getString(R.string.bug_level_2);
            case "3":
                return mContext.getString(R.string.bug_level_3);
        }

        return "";
    }


    /*0待审批 1审批通过待支付 2审批不通过 3已支付*/

    /**
     * 状态
     *
     * @param status
     * @return
     */
    public String getStateString(String status) {

        if (TextUtils.isEmpty(status)) {
            return "";
        }

        switch (status) {
            case "0":
                return mContext.getString(R.string.question_state_1);
            case "1":
                return mContext.getString(R.string.question_state_6);
            case "2":
                return mContext.getString(R.string.question_state_2);
            case "3":
                return mContext.getString(R.string.question_state_7);
        }

        return "";
    }


}
