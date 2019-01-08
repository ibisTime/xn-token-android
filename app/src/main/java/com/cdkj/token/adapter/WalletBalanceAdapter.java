package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
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
        super(R.layout.item_coin_assets_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBalanceModel item) {
        //!TextUtils.isEmpty(item.getAddress()) && !TextUtils.isEmpty(SPUtilHelper.getPastBtcInfo().split("\\+")[0]) &&
        if (TextUtils.equals(item.getAddress(), SPUtilHelper.getPastBtcInfo().split("\\+")[0])) {
            LogUtil.E("获取的地址" + item.getAddress() + "本地的地址" + SPUtilHelper.getPastBtcInfo().split("\\+")[0]);
            // 老BTC地址需要加上老版本标示

            //如果余额为0的话  也不显示老地址
            String availablemountString = AmountUtil.transformFormatToString(item.getAvailableAmount(), item.getCoinSymbol(), 8);
            if (Double.parseDouble(availablemountString) <= 0.0) {
                helper.setText(R.id.tv_coin_name, item.getCoinSymbol());
                helper.setGone(R.id.rl_normal, true);
                helper.setGone(R.id.rl_load, false);
                setViewData(helper, item);
            } else {
                helper.setText(R.id.tv_coin_name_load, item.getCoinSymbol() + "(old version)");
                helper.setGone(R.id.rl_normal, false);
                helper.setGone(R.id.rl_load, true);
                setViewDataLoad(helper, item);
            }

        } else {
            helper.setText(R.id.tv_coin_name, item.getCoinSymbol());
            helper.setGone(R.id.rl_normal, true);
            helper.setGone(R.id.rl_load, false);
            setViewData(helper, item);
        }
        helper.addOnClickListener(R.id.ll_item);
        helper.addOnClickListener(R.id.btn_charge);
        helper.addOnClickListener(R.id.btn_withdraw);
    }

    private void setViewData(BaseViewHolder helper, WalletBalanceModel item) {
        String availablemountString = AmountUtil.transformFormatToString(item.getAvailableAmount(), item.getCoinSymbol(), 8);

        helper.setText(R.id.tv_amount, availablemountString);
        helper.setText(R.id.tv_symbol, item.getCoinSymbol());

        ImgUtils.loadImage(mContext, item.getCoinImgUrl(), helper.getView(R.id.img_coin_logo));

        helper.setText(R.id.tv_market_price, getMarketPriceString(item));

        helper.setText(R.id.tv_amount_cny, getAmountString(item));

        if (Double.parseDouble(item.getPercentChange24h()) >= 0) {
            helper.setBackgroundRes(R.id.iv_rate, R.mipmap.wallet_percent_up);
            helper.setText(R.id.tv_rate, "+" + item.getPercentChange24h() + "%");
        } else {
            helper.setBackgroundRes(R.id.iv_rate, R.mipmap.wallet_percent_down);
            helper.setText(R.id.tv_rate, item.getPercentChange24h() + "%");
        }
    }

    private void setViewDataLoad(BaseViewHolder helper, WalletBalanceModel item) {
        String availablemountString = AmountUtil.transformFormatToString(item.getAvailableAmount(), item.getCoinSymbol(), 8);

        helper.setText(R.id.tv_amount_load, availablemountString);
        helper.setText(R.id.tv_symbol_load, item.getCoinSymbol());

        ImgUtils.loadImage(mContext, item.getCoinImgUrl(), helper.getView(R.id.img_coin_logo_load));

        helper.setText(R.id.tv_market_price_load, getMarketPriceString(item));

        helper.setText(R.id.tv_amount_cny_load, getAmountString(item));

        if (Double.parseDouble(item.getPercentChange24h()) >= 0) {
            helper.setBackgroundRes(R.id.iv_rate_load, R.mipmap.wallet_percent_up);
            helper.setText(R.id.tv_rate_load, "+" + item.getPercentChange24h() + "%");
        } else {
            helper.setBackgroundRes(R.id.iv_rate_load, R.mipmap.wallet_percent_down);
            helper.setText(R.id.tv_rate_load, item.getPercentChange24h() + "%");
        }
    }

    public String getMarketPriceString(WalletBalanceModel item) {
        String priceString = item.getLocalMarketPrice();
        return "≈ " + priceString + SPUtilHelper.getLocalMarketSymbol();
    }

    public String getAmountString(WalletBalanceModel item) {
        String priceString = item.getLocalAmount();
        return "≈ " + priceString + SPUtilHelper.getLocalMarketSymbol();
    }

}
