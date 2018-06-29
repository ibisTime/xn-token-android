package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceAdapter extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {


    public AddChoiceAdapter(@Nullable List<CoinModel.AccountListBean> data) {
        super(R.layout.item_add_choice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {

//        helper.setText(R.id.tv_name, item.getCurrency() + "--" + getCoinENameWithCurrency(item.getCurrency()));
        helper.setText(R.id.tv_name, item.getCurrency());

        helper.setImageResource(R.id.iv_watermark, WalletHelper.getCoinIconByType(item.getLocalCoinType()));

        if (item.isChoose()) {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_confirm);
        } else {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_cancel);
        }

    }
}
