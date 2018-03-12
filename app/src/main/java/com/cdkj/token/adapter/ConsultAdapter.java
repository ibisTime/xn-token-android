package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.ConsultModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2018/3/7.
 */

public class ConsultAdapter extends BaseQuickAdapter<ConsultModel, BaseViewHolder> {


    public ConsultAdapter(@Nullable List<ConsultModel> data) {
        super(R.layout.item_consult, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ConsultModel item) {

        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_date,item.getDate()+" 橙袋科技");

    }

}


