package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by lei on 2017/10/25.
 */

public class WalletBalanceAdapter extends BaseQuickAdapter<WalletBalanceModel, BaseViewHolder> {

    public WalletBalanceAdapter(@Nullable List<WalletBalanceModel> data) {
        super(R.layout.item_coin_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBalanceModel item) {

        helper.setText(R.id.tv_coin_name, item.getCoinName());

        helper.setText(R.id.tv_amount, item.getAmount());

        ImgUtils.loadImage(mContext, item.getCoinImgUrl(), helper.getView(R.id.img_coin_logo));

        if (item.getMarketPrice() == null) {
            helper.setText(R.id.tv_market_price, "≈ 0CNY");
        } else {
            helper.setText(R.id.tv_market_price, "≈ " + item.getMarketPrice() + "CNY");
        }

        if (item.getAmountCny() == null) {
            helper.setText(R.id.tv_amount_cny, "0CNY");
        } else {
            helper.setText(R.id.tv_amount_cny, item.getAmountCny() + "CNY");
        }

    }
}
