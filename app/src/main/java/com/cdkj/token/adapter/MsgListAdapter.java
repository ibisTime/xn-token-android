package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.MsgListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class MsgListAdapter extends BaseQuickAdapter<MsgListModel.ListBean, BaseViewHolder> {

    public MsgListAdapter(@Nullable List<MsgListModel.ListBean> data) {
        super(R.layout.item_msg, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgListModel.ListBean item) {

//        helper.setText(R.id.tv_msg_title, item.getSmsTitle());
//        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getChannelType(), DateUtil.DEFAULT_DATE_FMT));

        helper.setText(R.id.tv_content, item.getSmsContent());
        helper.setText(R.id.tv_title, item.getSmsTitle());
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(), DateUtil.DATE_YMD));
    }
}
