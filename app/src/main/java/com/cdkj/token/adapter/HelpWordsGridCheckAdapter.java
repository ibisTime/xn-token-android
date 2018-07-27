package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.HelpWordsCheckModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 助记词
 * Created by lei on 2017/10/25.
 */

public class HelpWordsGridCheckAdapter extends BaseQuickAdapter<HelpWordsCheckModel, BaseViewHolder> {


    public HelpWordsGridCheckAdapter(@Nullable List<HelpWordsCheckModel> data) {
        super(R.layout.layout_words_text_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HelpWordsCheckModel item) {

        if (item.isChoose()) {
            helper.setTextColor(R.id.tv_words, Color.parseColor("#CCCCCC"));
            helper.setBackgroundColor(R.id.tv_words, Color.parseColor("#F8F8F8"));
        } else {
            helper.setTextColor(R.id.tv_words, Color.parseColor("#333333"));
            helper.setBackgroundColor(R.id.tv_words, Color.parseColor("#ffffff"));
        }

        helper.setText(R.id.tv_words, item.getWords());

    }


}
