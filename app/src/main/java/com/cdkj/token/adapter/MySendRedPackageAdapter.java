package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.MySendRedPackageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MySendRedPackageAdapter extends BaseQuickAdapter<MySendRedPackageBean, BaseViewHolder> {
    public MySendRedPackageAdapter(@Nullable List<MySendRedPackageBean> data) {
        super(R.layout.item_red_package_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySendRedPackageBean item) {

    }
}
