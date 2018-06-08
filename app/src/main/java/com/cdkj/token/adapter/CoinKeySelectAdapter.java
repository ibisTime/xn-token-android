package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.utils.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class CoinKeySelectAdapter extends BaseQuickAdapter<LocalCoinModel, BaseViewHolder> {

    public CoinKeySelectAdapter(@Nullable List<LocalCoinModel> data) {
        super(R.layout.item_coin_key_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalCoinModel item) {
        helper.setText(R.id.tv_name, item.getCoinShortName() + mContext.getString(R.string.private_key));

        helper.setImageResource(R.id.img_icon, WalletHelper.getCoinIconByType(item.getCoinType()));
    }

}
