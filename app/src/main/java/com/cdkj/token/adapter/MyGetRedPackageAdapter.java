package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MyGetRedPackageAdapter extends BaseQuickAdapter<MyGetRedPackageBean, BaseViewHolder> {
    public MyGetRedPackageAdapter(@Nullable List<MyGetRedPackageBean> data) {
        super(R.layout.item_red_package_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyGetRedPackageBean item) {
        if (item == null || item.getRedPacketInfo() == null) return;

        helper.setText(R.id.tv_user_name, item.getRedPacketInfo().getSendUserNickname());
        helper.setText(R.id.tv_b_money, item.getCount() + item.getRedPacketInfo().getSymbol());
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getRedPacketInfo().getCreateDateTime(), DateUtil.DATE_MMddHHmm));
        helper.setText(R.id.tv_rmb_money, MoneyUtils.MONEYSING + item.getRedPacketInfo().getTotalCountCNY());
        ImageView iv_user_head = helper.getView(R.id.iv_user_head);

        ImgUtils.loadLogo(mContext, item.getRedPacketInfo().getSendUserPhoto(), iv_user_head);
    }
}
