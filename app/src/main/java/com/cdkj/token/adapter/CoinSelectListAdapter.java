package com.cdkj.token.adapter;

import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 添加相册图片适配器
 * Created by cdkj on 2018/5/25.
 */

public class CoinSelectListAdapter extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {

    public CoinSelectListAdapter(List<CoinModel.AccountListBean> data) {
        super(R.layout.item_coin_type_red_packet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {

        helper.setText(R.id.tv_name, item.getCurrency());
        ImgUtils.loadImage(mContext, LocalCoinDBUtils.getCoinIconByCoinSymbol(item.getCurrency()),helper.getView(R.id.img_icon));
    }

}
