package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2018/3/7.
 */

public class ConsultAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ConsultAdapter(@Nullable List<String> data) {
        super(R.layout.item_consult, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {


    }

}


