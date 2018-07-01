package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.token.utils.AccountUtil.ALLSCALE;
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinAdapter2 extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {

    public CoinAdapter2(@Nullable List<CoinModel.AccountListBean> data) {
//        super(R.layout.item_coin_bill, data);
        super(R.layout.item_coin_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {

        helper.setText(R.id.tv_coin_name, item.getCurrency());

        if (!TextUtils.isEmpty(item.getAmountString()) && !TextUtils.isEmpty(item.getFrozenAmountString())) {
            BigDecimal amount = new BigDecimal(item.getAmountString());
            BigDecimal frozenAmount = new BigDecimal(item.getFrozenAmountString());
            helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), item.getCurrency(), 8));
        }


        ImageView ivCoin = helper.getView(R.id.img_coin_logo);

        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 1), ivCoin);

        if (item.getPriceCNY() == null) {
            helper.setText(R.id.tv_market_price, "≈ 0CNY");
        } else {
            helper.setText(R.id.tv_market_price, "≈ " + item.getPriceCNY() + "CNY");
        }

        if (item.getAmountCNY() == null) {
            helper.setText(R.id.tv_amount_cny, "0CNY");
        } else {
            helper.setText(R.id.tv_amount_cny, item.getAmountCNY() + "CNY");
        }


    }
}
