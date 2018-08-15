package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.RecommendAppModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class AppListAdapter extends BaseQuickAdapter<RecommendAppModel, BaseViewHolder> {

    public AppListAdapter(@Nullable List<RecommendAppModel> data) {
        super(R.layout.item_app_find, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendAppModel item) {
        if (item == null) return;

        helper.setText(R.id.tv_content_app, item.getSlogan());
        helper.setText(R.id.tv_title_app, item.getName());
        ImgUtils.loadImage(mContext, item.getIcon(), helper.getView(R.id.img_app));

    }


}
