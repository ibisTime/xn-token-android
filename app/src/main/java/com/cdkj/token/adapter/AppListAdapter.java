package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.DAppModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class AppListAdapter extends BaseQuickAdapter<DAppModel, BaseViewHolder> {

    public AppListAdapter(@Nullable List<DAppModel> data) {
        super(R.layout.item_app_find_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppModel item) {
        if (item == null) return;

        helper.setText(R.id.tv_title_app, item.getName());
        helper.setText(R.id.tv_intro, item.getDesc());

        ImgUtils.loadImage(mContext, item.getPicList(), helper.getView(R.id.img_app));

    }


}
