package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.StatisticsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2018/3/7.
 */

public class StatisticsAdapter extends BaseQuickAdapter<StatisticsModel, BaseViewHolder> {


    public StatisticsAdapter(@Nullable List<StatisticsModel> data) {
        super(R.layout.item_statistics, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, StatisticsModel item) {

    }

}


