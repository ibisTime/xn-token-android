package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.cdkj.token.model.MySendRedPackageBean;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class RedPacketGetAdapter extends BaseQuickAdapter<MyGetRedPackageBean, BaseViewHolder> {
    public RedPacketGetAdapter(@Nullable List<MyGetRedPackageBean> data) {
        super(R.layout.item_red_packet_get, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyGetRedPackageBean item) {

        if (item == null || item.getRedPacketInfo() == null) {
            return;
        }
        TextView tvType = helper.getView(R.id.tv_type);
        if (TextUtils.equals(item.getRedPacketInfo().getType(), "0")) {//普通红包
            tvType.setText(R.string.change_type_2);
            tvType.setBackgroundResource(R.drawable.redpacket__lucky_bg);

            tvType.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            tvType.setBackgroundResource(R.drawable.redpacket_ordinary_bg);
            tvType.setText(R.string.change_type_1);
            tvType.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }

        helper.setText(R.id.tv_user_name, item.getRedPacketInfo().getSendUserNickname());

        helper.setText(R.id.tv_get_total, item.getCount());

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getRedPacketInfo().getCreateDateTime(), "MM-dd HH:mm"));

        ImgUtils.loadImage(mContext, item.getRedPacketInfo().getSendUserPhoto(), helper.getView(R.id.img_user_logo));

        ImgUtils.loadImage(mContext, LocalCoinDBUtils.getCoinIconByCoinSymbol(item.getRedPacketInfo().getSymbol()), helper.getView(R.id.img_icon));


    }
}
