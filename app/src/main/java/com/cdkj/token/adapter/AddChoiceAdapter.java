package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceAdapter extends BaseQuickAdapter<LocalCoinDbModel, BaseViewHolder> {


    public AddChoiceAdapter(@Nullable List<LocalCoinDbModel> data) {
        super(R.layout.item_add_choice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalCoinDbModel item) {

        helper.setText(R.id.tv_name, item.getSymbol());

        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getSymbol(), 1), helper.getView(R.id.iv_watermark));

        if (item.isChoose()) {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_confirm);
        } else {
            helper.setImageResource(R.id.iv_choice, R.mipmap.choice_cancel);
        }

    }
}
