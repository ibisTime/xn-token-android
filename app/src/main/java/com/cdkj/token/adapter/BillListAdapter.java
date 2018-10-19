package com.cdkj.token.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.model.BillModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinWatermarkWithCurrency;

/**
 * 中心化钱包账单流水
 * Created by lei on 2017/8/22.
 */

public class BillListAdapter extends BaseQuickAdapter<BillModel.ListBean, BaseViewHolder> {

    public BillListAdapter(@Nullable List<BillModel.ListBean> data) {
        super(R.layout.item_bill_2, data);
    }

    @NonNull
    @Override
    public List<BillModel.ListBean> getData() {
        return super.getData();
    }

    @Override
    protected void convert(BaseViewHolder helper, BillModel.ListBean item) {

        helper.setGone(R.id.tv_address, false);

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(), DATE_MMddHHmm));

        helper.setText(R.id.tv_remark, item.getBizNote());

        BigDecimal tas = new BigDecimal(item.getTransAmountString());
        int i = tas.compareTo(BigDecimal.ZERO);
        if (i == 1) {
            helper.setText(R.id.tv_amount, "+" + AmountUtil.transformFormatToString(tas, item.getCurrency(), 8));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.in_money));
        } else {
            helper.setText(R.id.tv_amount, AmountUtil.transformFormatToString(tas, item.getCurrency(), 8));
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.out_money));
        }


        ImageView ivType = helper.getView(R.id.iv_type);

        if (TextUtils.equals(item.getKind(), "0")) { // 非冻结流水

            switch (item.getBizType()) {
                case "charge": // 充值
                case "o2o_in": // o2o店铺消费收入
                case "invite": // 推荐好友分成
                    ivType.setImageResource(R.drawable.coin_in);
                    break;

                case "withdraw": // 取现
                case "tradefee": // 手续费
                case "withdrawfee": // 手续费
                case "o2o_out": // o2o店铺消费支出
//                    ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 3), ivType);
                    ivType.setImageResource(R.drawable.coin_out);
                    break;
                case "redpacket_back": // 红包退回
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
                case "sendredpacket_out": // 发红包
                    ivType.setImageResource(R.drawable.coin_out);
                    break;
                case "sendredpacket_in": // 抢红包
                    ivType.setImageResource(R.drawable.coin_in);
                    break;

                case "lhlc_invest": // 量化理财投资
                    ivType.setImageResource(R.drawable.coin_out);
                    break;
                case "lhlc_repay": // 量化理财还款
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
                case "jf_lottery_in": // 积分抽奖
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
                case "invite_income_pop_in": // 邀请好友量化分成
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
                case "lhlc_income_in": // 量化理财收益
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
                case "lhlc_invest_out": // 量化理财投资
                    ivType.setImageResource(R.drawable.coin_out);
                    break;
                case "lhlc_repay_in": // 量化理财本金回款
                    ivType.setImageResource(R.drawable.coin_in);
                    break;
            }

        } else { // 冻结流水

            if (item.getTransAmountString().contains("-")) { // 金额是负数
                ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 3), ivType);
            } else {
                ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 2), ivType);
            }

        }

    }


}
