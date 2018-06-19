package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.ConsultModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2018/3/7.
 */

public class ConsultListAdapter extends BaseQuickAdapter<ConsultModel, BaseViewHolder> {


    public ConsultListAdapter(@Nullable List<ConsultModel> data) {
        super(R.layout.item_consult_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ConsultModel item) {

        if (item == null) return;

        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_address, mContext.getString(R.string.address) + ": " + getAddress(item));
        helper.setText(R.id.tv_slogan, item.getSlogan());

        ImgUtils.loadImage(mContext, item.getAdvPic(), helper.getView(R.id.img_consult));

//        helper.setText(R.id.tv_date,item.getDate()+" 橙袋科技");

    }


    private String getAddress(ConsultModel model) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(model.getProvince());
        stringBuffer.append(model.getCity());
        stringBuffer.append(model.getArea());
        stringBuffer.append(model.getAddress());

        return stringBuffer.toString();

    }

}


