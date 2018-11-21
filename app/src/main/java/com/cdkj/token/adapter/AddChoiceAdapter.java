package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.ChoiceCoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinIconByCoinSymbol;

/**
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceAdapter extends BaseQuickAdapter<ChoiceCoinModel, BaseViewHolder> {


    public AddChoiceAdapter(@Nullable List<ChoiceCoinModel> data) {
        super(R.layout.item_add_choice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChoiceCoinModel item) {

        helper.setText(R.id.tv_name, item.getCoin().getSymbol());

        ImgUtils.loadImage(mContext, getCoinIconByCoinSymbol(item.getCoin().getSymbol()), helper.getView(R.id.iv_watermark));

        if (item.getIsDisplay().equals("1")) {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_confirm);
        } else {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_cancel);
        }

    }
}
