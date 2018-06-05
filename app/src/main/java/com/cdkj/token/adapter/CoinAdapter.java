package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.model.CoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinAdapter extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {

    public CoinAdapter(@Nullable List<CoinModel.AccountListBean> data) {
        super(R.layout.item_coin2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {
        BigDecimal amount;
        BigDecimal frozenAmount;

        helper.setText(R.id.tv_name, item.getCurrency());
//        helper.setText(R.id.tv_name, getCoinCNameWithCurrency(item.getCurrency())+"("+item.getCurrency()+")");

        amount = new BigDecimal(item.getAmountString());
        frozenAmount = new BigDecimal(item.getFrozenAmountString());
        helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), item.getCurrency(), 8));

        ImageView ivCoin = helper.getView(R.id.iv_watermark);
        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(),1), ivCoin);

        if (item.getPriceCNY() == null){
            helper.setText(R.id.tv_market_price, "≈ 0CNY");
        }else {
            helper.setText(R.id.tv_market_price, "≈ " + item.getPriceCNY()+"CNY");

        }

        if (item.getAmountCNY() == null){
            helper.setText(R.id.tv_amount_cny, "0CNY");
        }else {
            helper.setText(R.id.tv_amount_cny, item.getAmountCNY()+"CNY");
        }




    }
}
