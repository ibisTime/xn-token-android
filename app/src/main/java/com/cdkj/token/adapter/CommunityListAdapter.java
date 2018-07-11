package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.token.R;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/25.
 */

public class CommunityListAdapter extends BaseQuickAdapter<IntroductionInfoModel, BaseViewHolder> {

    public CommunityListAdapter(@Nullable List<IntroductionInfoModel> data) {
        super(R.layout.item_join_community, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntroductionInfoModel item) {

        helper.setText(R.id.tv_title, item.getCkey());
        helper.setText(R.id.tv_info, item.getCvalue());

    }
}
