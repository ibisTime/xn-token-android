package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.DAppGuideModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class DAppGuideAdapter extends BaseQuickAdapter<DAppGuideModel.DAppGuide, BaseViewHolder> {


    public DAppGuideAdapter(@Nullable List<DAppGuideModel.DAppGuide> data) {
        super(R.layout.item_dapp_guide, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppGuideModel.DAppGuide item) {

        helper.setText(R.id.tv_title, item.getTitle());

    }
}
