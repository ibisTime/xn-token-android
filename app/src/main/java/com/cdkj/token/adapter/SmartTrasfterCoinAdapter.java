package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 一键划转币种列表
 * Created by cdkj on 2018/5/25.
 */

public class SmartTrasfterCoinAdapter extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {

    private int selectPosition = -1;//用户选择的position

    public SmartTrasfterCoinAdapter(@Nullable List<CoinModel.AccountListBean> data) {
        super(R.layout.item_coin_smart_transfer, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {
        if (item == null) return;

        if (helper.getLayoutPosition() == selectPosition) {
            helper.setBackgroundRes(R.id.linLayout, R.drawable.rect_gray);
        } else {
            helper.setBackgroundRes(R.id.linLayout, R.drawable.rect_gray_line);
        }

        helper.setText(R.id.tv_name, item.getCurrency());

        ImgUtils.loadImage(mContext, LocalCoinDBUtils.getCoinIconByCoinSymbol(item.getCurrency()), helper.getView(R.id.img_icon));

    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
