package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class DAppIntroAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public DAppIntroAdapter(@Nullable List<String> data) {
        super(R.layout.item_dapp_intro, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImgUtils.loadImage(mContext, item, helper.getView(R.id.iv_intro));
    }
}
