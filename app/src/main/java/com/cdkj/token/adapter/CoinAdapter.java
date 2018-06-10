package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinAdapter extends BaseQuickAdapter<BalanceListModel.AccountListBean, BaseViewHolder> {

    public CoinAdapter(@Nullable List<BalanceListModel.AccountListBean> data) {
        super(R.layout.item_coin2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceListModel.AccountListBean item) {

        helper.setText(R.id.tv_name, item.getSymbol() + " - " + WalletHelper.getCoinENameByType(item.getSymbol()));

        helper.setImageResource(R.id.iv_watermark, WalletHelper.getCoinIconByType(item.getSymbol()));

        helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnitForShow(new BigDecimal(item.getBalance()), 8));


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
