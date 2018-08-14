package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.model.RecommendAppModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.AccountUtil.ETHSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.getPrivateCoinStataIconByState;
import static com.cdkj.token.utils.LocalCoinDBUtils.isInState;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class AppListAdapter extends BaseQuickAdapter<RecommendAppModel, BaseViewHolder> {

    public AppListAdapter(@Nullable List<RecommendAppModel> data) {
        super(R.layout.item_app_find, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendAppModel item) {
        if (item == null) return;

        helper.setText(R.id.tv_content_app, item.getSlogan());
        helper.setText(R.id.tv_title_app, item.getName());
        ImgUtils.loadImage(mContext, item.getIcon(), helper.getView(R.id.img_app));

    }


}
