package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class MyManagementMoneyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public MyManagementMoneyAdapter(@Nullable List<String> data) {
        super(R.layout.item_my_management_money, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }


}
