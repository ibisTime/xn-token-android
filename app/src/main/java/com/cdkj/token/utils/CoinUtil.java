package com.cdkj.token.utils;

import android.text.TextUtils;

import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2018/2/8.
 */

public class CoinUtil {


    public static String getCoinENameWithCurrency(String currency) {

        for (LocalCoinDbModel model : DataSupport.findAll(LocalCoinDbModel.class)) {

            if (model.getSymbol().equals(currency)) {
                return model.getEname();
            }
        }

        return "";

    }


    /**
     * @param currency 币种
     * @param position 要哪张图片 0:icon官方图标,1:Pic1钱包水印图标,2:Pic2流水加钱图标,3:Pic3流水减钱图标
     * @return
     */
    public static String getCoinWatermarkWithCurrency(String currency, int position) {

        for (LocalCoinDbModel model : DataSupport.findAll(LocalCoinDbModel.class)) {

            if (model.getSymbol().equals(currency)) {
                if (position == 0) {
                    return model.getIcon();
                } else if (position == 1) {
                    return model.getPic1();
                } else if (position == 2) {
                    return model.getPic2();
                } else {
                    return model.getPic3();
                }
            }

        }

        return "";

    }

    /**
     * 获取所有第一个币种的简称
     *
     * @return
     */
    public static String getFirstTokenCoin() {

        for (LocalCoinDbModel model : DataSupport.findAll(LocalCoinDbModel.class)) {

            // type = 1: Token币
            if (model.getType().equals("1")) {
                return model.getSymbol();
            }

        }

        // 默认返回OGC
        return AccountUtil.OGC;

    }




    /**
     * 根据状态获取展示图片       0出钱 1进钱
     *
     * @param direction
     * @return
     */
    public static int getStataIconByState(String direction) {
        if (isInState(direction)) {
            return R.drawable.money_in;
        }
        return R.drawable.money_out;
    }

    /**
     * 根据状态获取展示图片       0出钱 1进钱
     *
     * @param direction
     * @return
     */
    public static String getMoneyStateByState(String direction) {

        if (isInState(direction)) {
            return "+";
        }
        return "-";

    }

    /**
     * 判断是否收款 0出钱 1进钱
     *
     * @param direction
     * @return
     */
    public static boolean isInState(String direction) {
        return TextUtils.equals(direction, "1");
    }

}
