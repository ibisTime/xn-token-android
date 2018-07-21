package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 助记词
 * Created by lei on 2017/10/25.
 */

public class HelpWordsGridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HelpWordsGridAdapter(@Nullable List<String> data) {
        super(R.layout.layout_words_text, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_words,item);

    }
}
