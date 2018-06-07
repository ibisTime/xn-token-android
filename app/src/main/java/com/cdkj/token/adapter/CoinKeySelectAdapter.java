package com.cdkj.token.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ItemCoinKeySelectBinding;
import com.cdkj.token.model.ExchangeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class CoinKeySelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public CoinKeySelectAdapter(@Nullable List<String> data) {
        super(R.layout.item_coin_key_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
    }
}
