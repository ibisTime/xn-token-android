package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.Util.AccountUtil;
import com.cdkj.token.model.StatisticsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.token.Util.AccountUtil.OGCSCALE;

/**
 * Created by lei on 2018/3/7.
 */

public class StatisticsAdapter extends BaseQuickAdapter<StatisticsModel, BaseViewHolder> {


    public StatisticsAdapter(@Nullable List<StatisticsModel> data) {
        super(R.layout.item_statistics, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, StatisticsModel item) {

        if (item == null) return;

        helper.setText(R.id.tv_code, item.getHash());
        helper.setText(R.id.tv_date, item.getCreates());

        helper.setText(R.id.tv_address_from, item.getTokenFrom());
        helper.setText(R.id.tv_address_to, item.getTokenTo());
        helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(item.getTokenValue(), AccountUtil.OGC, OGCSCALE) + " " + AccountUtil.OGC);


    }

}


