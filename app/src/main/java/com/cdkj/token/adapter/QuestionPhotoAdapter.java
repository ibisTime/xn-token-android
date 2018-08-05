package com.cdkj.token.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加相册图片适配器
 * Created by cdkj on 2018/5/25.
 */

public class QuestionPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public QuestionPhotoAdapter(List<String> data) {
        super(R.layout.item_photo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ViewGroup.LayoutParams params = helper.getView(R.id.img_center).getLayoutParams();
        params.height = DisplayHelper.getScreenWidth(mContext) / 4;  //设置item宽高
        params.width = params.height - DisplayHelper.dp2px(mContext, 15);
        helper.getView(R.id.img_center).setLayoutParams(params);

        ImgUtils.loadImage(mContext, item, helper.getView(R.id.img));

    }

}
