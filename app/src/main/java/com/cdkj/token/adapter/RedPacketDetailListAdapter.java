package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.RedPacketDetails;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class RedPacketDetailListAdapter extends BaseQuickAdapter<RedPacketDetails.ReceiverListBean, BaseViewHolder> {


    public RedPacketDetailListAdapter(@Nullable List<RedPacketDetails.ReceiverListBean> data) {
        super(R.layout.item_red_packet_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedPacketDetails.ReceiverListBean item) {

        if (item == null) return;

        helper.setText(R.id.tv_user_name, item.getUserNickname());
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(), "MM-dd HH:mm"));
        helper.setText(R.id.tv_get_total, item.getCount()+"");

    }
}
