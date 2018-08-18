package com.cdkj.token.utils;

import android.database.Cursor;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.R;
import com.cdkj.token.utils.wallet.WalletDBColumn;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.cdkj.token.utils.wallet.WalletDBColumn.DELETE_LOCAL_COIN;
import static com.cdkj.token.utils.wallet.WalletDBColumn.getLocalCoinAttributesSqlByCoinSymbol;

/**
 * 本地缓存币种数据库
 * Created by lei on 2018/2/8.
 */

public class LocalCoinDBUtils {

    //自选分隔符
    public final static String COIN_SYMBOL_SPACE_SYMBOL = ",";

    //0 公链币（ETH BTC WAN） 1 ethtoken（ETH） 2 wantoken（WAN）


    /**
     * 判断是否公链币
     *
     * @param coinType 0 公链币（ETH BTC WAN）
     * @return
     */
    public static boolean isCommonChainCoin(String coinType) {
        return TextUtils.equals("0", coinType);
    }


    /**
     * 判断是否ETH token币
     *
     * @param coinType 1 ethtoken（ETH）
     * @return
     */
    public static boolean isEthTokenCoin(String coinType) {
        return TextUtils.equals("1", coinType);
    }

    /**
     * 判断是否ETH token币
     *
     * @param coinName
     * @return
     */
    public static boolean isEthTokenCoinByName(String coinName) {
        return isEthTokenCoin(getLocalCoinType(coinName));
    }

    /**
     * 判断是否Wan token币
     *
     * @param coinType 2 wantoken（WAN）
     * @return
     */
    public static boolean isWanTokenCoin(String coinType) {
        return TextUtils.equals("2", coinType);
    }


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

        List<LocalCoinDbModel> myLocalCoinList = getLocalCoinList();  //本地缓存列表

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
                deleteLocalCoinBySymbol(localCoinDbModel.getSymbol());
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
            }
        }

        if (!saveLocals.isEmpty()) {

            DataSupport.saveAll(saveLocals);

            if (SPUtilHelper.isLoginNoStart() && WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId())) {  //如果当前用户已经登录 并且添加过自选
                WalletHelper.updateUserChooseCoinString(userChooseCoinSymbolString, SPUtilHelper.getUserId());  //更新用户自选
            }
        }
    }

    //lxjtest  findALl查找优化
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


    /**
     * 根据货币获取最小单位
     *
     * @param
     * @return
     */
    public static BigDecimal getLocalCoinUnit(String coinSymbol) {

        Cursor cursor = DataSupport.findBySQL(getLocalCoinAttributesSqlByCoinSymbol(WalletDBColumn.COIN_UNIT), coinSymbol);

        int unit = 1;

        if (cursor != null && cursor.moveToFirst()) {

            try {
                unit = cursor.getInt(cursor.getColumnIndex(WalletDBColumn.COIN_UNIT));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return BigDecimal.TEN.pow(unit);
    }


    /**
     * 根据货币获取币种类型
     *
     * @param
     * @return
     */
    public static String getLocalCoinType(String coinSymbol) {

        Cursor cursor = DataSupport.findBySQL(getLocalCoinAttributesSqlByCoinSymbol(WalletDBColumn.COIN_TYPE), coinSymbol);

        String coinType = "";

        if (cursor != null && cursor.moveToFirst()) {

            try {
                coinType = cursor.getString(cursor.getColumnIndex(WalletDBColumn.COIN_TYPE));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return coinType;
    }


    /**
     * 根据货币获取币种合约地址
     *
     * @param
     * @return
     */
    public static String getLocalCoinContractAddress(String coinSymbol) {

        Cursor cursor = DataSupport.findBySQL(getLocalCoinAttributesSqlByCoinSymbol(WalletDBColumn.COIN_CONTRACTADDRESS), coinSymbol);

        String contractaddress = "";

        if (cursor != null && cursor.moveToFirst()) {

            try {
                contractaddress = cursor.getString(cursor.getColumnIndex(WalletDBColumn.COIN_CONTRACTADDRESS));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return contractaddress;
    }

    //获取本地所有缓存币种
    public static List<LocalCoinDbModel> getLocalCoinList() {
        return DataSupport.findAll(LocalCoinDbModel.class);
    }

    public static boolean deleteLocalCoinBySymbol(String symbol) {
        return DataSupport.deleteAll(LocalCoinDbModel.class, DELETE_LOCAL_COIN, symbol) > 0;
    }


    /**
     * 根据币种获取当前币种的名称 公链币显示当前名称 token币显示 wan 或 eth
     *
     * @param coin
     * @return
     */
    public static String getCoinUnitName(String coin) {

        String coinType = getLocalCoinType(coin);

        if (isWanTokenCoin(coinType)) {
            return WalletHelper.COIN_WAN;
        }
        if (isEthTokenCoin(coinType)) {
            return WalletHelper.COIN_ETH;
        }
        return coin;
    }


}
