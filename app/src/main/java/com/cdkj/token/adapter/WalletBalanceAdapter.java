package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/25.
 */

public class WalletBalanceAdapter extends BaseQuickAdapter<WalletBalanceModel, BaseViewHolder> {

    public WalletBalanceAdapter(@Nullable List<WalletBalanceModel> data) {
        super(R.layout.item_coin_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBalanceModel item) {

        helper.setText(R.id.tv_coin_name, item.getCoinSymbol());

        String availablemountString = AmountUtil.amountFormatUnitForShow(item.getAvailableAmount(), item.getCoinSymbol(), 8) + " " + item.getCoinSymbol();

        helper.setText(R.id.tv_amount, availablemountString);

        ImgUtils.loadImage(mContext, item.getCoinImgUrl(), helper.getView(R.id.img_coin_logo));

        helper.setText(R.id.tv_market_price, getMarketPriceString(item));

        helper.setText(R.id.tv_amount_cny, getAmountString(item));

    }

    public String getMarketPriceString(WalletBalanceModel item) {
        String priceString = item.getLocalMarketPrice();
        return priceString + SPUtilHelper.getLocalMarketSymbol();
    }

    public String getAmountString(WalletBalanceModel item) {
        String priceString = item.getLocalAmount();
        return priceString + SPUtilHelper.getLocalMarketSymbol();
    }

}
