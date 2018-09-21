package com.cdkj.token.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.AppQuestionModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加相册图片适配器
 * Created by cdkj on 2018/5/25.
 */

public class QuestionlistAdapter extends BaseQuickAdapter<AppQuestionModel.HelpListBean, BaseViewHolder> {

    public QuestionlistAdapter(List<AppQuestionModel.HelpListBean> data) {
        super(R.layout.item_question_title, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppQuestionModel.HelpListBean item) {

        if (item == null) return;
        helper.setText(R.id.tv_title, item.getQuestion());
    }

}
