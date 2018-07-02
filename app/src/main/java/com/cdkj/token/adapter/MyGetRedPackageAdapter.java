package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MyGetRedPackageAdapter extends BaseQuickAdapter<MyGetRedPackageBean,BaseViewHolder> {
    public MyGetRedPackageAdapter( @Nullable List<MyGetRedPackageBean> data) {
        super(R.layout.item_red_package_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyGetRedPackageBean item) {

    }
}
