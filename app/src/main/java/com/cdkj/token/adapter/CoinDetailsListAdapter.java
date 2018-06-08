package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by lei on 2017/10/25.
 */

public class CoinDetailsListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CoinDetailsListAdapter(@Nullable List<String> data) {
        super(R.layout.item_coin2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        BigDecimal amount;
        BigDecimal frozenAmount;

//        helper.setText(R.id.tv_name, item.getCurrency());
//        helper.setImageResource(R.id.iv_watermark, WalletHelper.getCoinIconByType(item.getLocalCoinType()));

//        amount = new BigDecimal(item.getAmountString());
//        frozenAmount = new BigDecimal(item.getFrozenAmountString());
//        helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), item.getCurrency(), 8));
//
//        ImageView ivCoin = helper.getView(R.id.iv_watermark);
//        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(),1), ivCoin);
//
//        if (item.getPriceCNY() == null){
//            helper.setText(R.id.tv_market_price, "≈ 0CNY");
//        }else {
//            helper.setText(R.id.tv_market_price, "≈ " + item.getPriceCNY()+"CNY");
//
//        }
//
//        if (item.getAmountCNY() == null){
//            helper.setText(R.id.tv_amount_cny, "0CNY");
//        }else {
//            helper.setText(R.id.tv_amount_cny, item.getAmountCNY()+"CNY");
//        }


    }
}
