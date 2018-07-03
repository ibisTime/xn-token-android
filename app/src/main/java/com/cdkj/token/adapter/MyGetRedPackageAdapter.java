package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MyGetRedPackageAdapter extends BaseQuickAdapter<MyGetRedPackageBean.ListBean, BaseViewHolder> {
    public MyGetRedPackageAdapter(@Nullable List<MyGetRedPackageBean.ListBean> data) {
        super(R.layout.item_red_package_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyGetRedPackageBean.ListBean item) {
        helper.setText(R.id.tv_user_name, mContext.getString(R.string.red_package_form_user) + item.getSendUserNickname());
        helper.setText(R.id.tv_b_money, item.getTotalCount() + item.getSymbol());
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDateTime(), DateUtil.DATE_MMddHHmm));
        helper.setText(R.id.tv_rmb_money, item.getTotalCountCNY() + "");
        ImageView iv_user_head = helper.getView(R.id.iv_user_head);
        ImgUtils.loadAvatar(mContext, item.getSendUserPhoto(), iv_user_head);
    }
}
