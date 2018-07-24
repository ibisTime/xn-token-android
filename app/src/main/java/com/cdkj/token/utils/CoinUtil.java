package com.cdkj.token.utils;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.R;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2018/2/8.
 */

public class CoinUtil {

    //自选分隔符
    public final static String COIN_SYMBOL_SPACE_SYMBOL = ",";


    /**
     * 更新本地缓存
     *
     * @param requestCoins
     */
    public static void updateLocalCoinList(List<LocalCoinDbModel> requestCoins) {
        //如果请求列表为空 则清空本地缓存
        if (requestCoins == null || requestCoins.size() == 0) {
            DataSupport.deleteAll(LocalCoinDbModel.class);
            return;
        }

        // 如果数据库没有数据，则保存所有数据
        if (!DataSupport.isExist(LocalCoinDbModel.class)) {
            DataSupport.saveAll(requestCoins);
            return;
        }

        List<LocalCoinDbModel> myLocalCoinList = WalletHelper.getLocalCoinList();  //本地缓存列表

        updateLocalCoinByAdd(requestCoins, myLocalCoinList);

        updateLocalCoinByDelete(requestCoins, myLocalCoinList);

    }

    /**
     * 更新后台删除的币种
     *
     * @param requestCoins    后台币种
     * @param myLocalCoinList 缓存币种
     */
    private static void updateLocalCoinByDelete(List<LocalCoinDbModel> requestCoins, List<LocalCoinDbModel> myLocalCoinList) {
        //本地缓存和请求到的数据比对 找出需要删除的币种
        for (LocalCoinDbModel localCoinDbModel : myLocalCoinList) {
            if (!requestCoins.contains(localCoinDbModel)) {  //数据不存在 则删除本地缓存
                WalletHelper.deleteLocalCoinBySymbol(localCoinDbModel.getSymbol());
                LogUtil.E("localCoin delete：" + localCoinDbModel.getSymbol());
            }
        }
    }

    /**
     * 更新后台新增的币种
     *
     * @param requestCoins    后台币种
     * @param myLocalCoinList 缓存币种
     */
    private static void updateLocalCoinByAdd(List<LocalCoinDbModel> requestCoins, List<LocalCoinDbModel> myLocalCoinList) {

        String userChooseCoinSymbolString = WalletHelper.getUserChooseCoinSymbolString(SPUtilHelper.getUserId()); //用户自选币种

        List<LocalCoinDbModel> saveLocals = new ArrayList<>();

        //请求到的数据和本地比对  找出需要保存的币种
        for (LocalCoinDbModel localCoinDbModel : requestCoins) {

            if (!myLocalCoinList.contains(localCoinDbModel)) {  //如果数据库不存在 则保存

                if (!TextUtils.isEmpty(userChooseCoinSymbolString)) {
                    userChooseCoinSymbolString += COIN_SYMBOL_SPACE_SYMBOL + localCoinDbModel.getSymbol();    //新增币种默认自选
                } else {
                    userChooseCoinSymbolString = localCoinDbModel.getSymbol();
                }

                saveLocals.add(localCoinDbModel);


                LogUtil.E("localCoin add：" + localCoinDbModel.getSymbol());

            }
        }

        if (!saveLocals.isEmpty()) {

            DataSupport.saveAll(saveLocals);

            if (SPUtilHelper.isLoginNoStart() && WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId())) {  //如果当前用户已经登录 并且添加过自选
                WalletHelper.updateUserChooseCoinString(userChooseCoinSymbolString, SPUtilHelper.getUserId());  //更新用户自选
            }
        }
    }

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
    public static int getPrivateCoinStataIconByState(String direction) {
        if (isInState(direction)) {
            return R.drawable.private_coin_in;
        }
        return R.drawable.private_coin_out;
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
