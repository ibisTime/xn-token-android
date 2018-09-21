package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.MySendRedPackageBean;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.wallet.WalletFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class RedPacketSendAdapter extends BaseQuickAdapter<MySendRedPackageBean.ListBean, BaseViewHolder> {
    public RedPacketSendAdapter(@Nullable List<MySendRedPackageBean.ListBean> data) {
        super(R.layout.item_red_packet_send, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySendRedPackageBean.ListBean item) {

        if (item == null) {
            return;
        }

        if (TextUtils.equals(item.getType(), "0")) {  //普通

            helper.setImageResource(R.id.img_type, R.drawable.ordinary_redpacket_icon);

        } else {
            helper.setImageResource(R.id.img_type, R.drawable.lucky_redpacket_icon);
        }

        helper.setText(R.id.tv_user_name, R.string.me);

        if (!SPUtilHelper.isAssetsShow()) {
            helper.setText(R.id.tv_total, WalletFragment.HIND_SIGN + mContext.getString(R.string.red_package_unit));
        } else {
            helper.setText(R.id.tv_total, item.getTotalCount() + mContext.getString(R.string.red_package_unit));
        }


        helper.setText(R.id.tv_send_, item.getReceivedNum() + " / " + item.getSendNum() + mContext.getString(R.string.red_package_number_unit));

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDateTime(), "MM-dd HH:mm"));

        ImgUtils.loadImage(mContext, LocalCoinDBUtils.getCoinIconByCoinSymbol(item.getSymbol()), helper.getView(R.id.img_icon));


    }
}
