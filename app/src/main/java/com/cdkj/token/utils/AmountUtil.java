package com.cdkj.token.utils;


import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.db.LocalCoinDbModel;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import static com.cdkj.token.utils.LocalCoinDBUtils.getLocalCoinUnit;
import static java.math.BigDecimal.ROUND_HALF_EVEN;

/**
 * Created by lei on 2017/10/20.
 */

public class AmountUtil {

    public static final int ETH_UNIT_UNIT = 18;
    public static final int ETHSCALE = 8;
    public static final int ALLSCALE = 8;

    /**
     * 货币单位转换
     *
     * @param amount
     * @param coin
     * @return
     */
    public static String amountFormatUnit(BigDecimal amount, String coin, int scale) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        }

        if (TextUtils.isEmpty(coin)) {
            return scale(amount.divide(BigDecimal.TEN.pow(18)).toPlainString(), scale);
        }

        return scale(amount.divide(getLocalCoinUnit(coin)).toPlainString(), scale);
    }


    /**
     * 货币单位转换 带单位
     *
     * @param amount
     * @param
     * @return
     */
    public static String amountFormatUnitForShow(BigDecimal amount, String coinSymbol, int scale) {
        if (amount == null) {
            return "0";
        }

        if (TextUtils.isEmpty(coinSymbol)) {
            return formatCoinAmount(BigDecimalUtils.div(amount, BigDecimal.TEN.pow(18), scale));
        }

        return formatCoinAmount(BigDecimalUtils.div(amount, getLocalCoinUnit(coinSymbol), scale));
    }


    /**
     * 货币单位转换 带单位
     *
     * @param amount
     * @param
     * @return
     */
    public static String amountFormatUnitForShow(BigDecimal amount, BigDecimal unit, int scale) {
        if (amount == null) {
            return "0";
        }

        return formatCoinAmount(BigDecimalUtils.div(amount, unit, scale));
    }


    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigInteger bigIntegerFormat(BigDecimal amount, String coin) {

        if (amount == null) {
            return BigInteger.ZERO;
        }

        return amount.multiply(getLocalCoinUnit(coin)).toBigInteger();
    }

    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigDecimal bigDecimalFormat(BigDecimal amount, String coin) {
        return bigDecimalFormat(amount, getLocalCoinUnit(coin));
    }

    /**
     * BigInteger
     *
     * @param amount
     * @return
     */
    public static BigDecimal bigDecimalFormat(BigDecimal amount, BigDecimal coinUnit) {

        if (amount == null) {
            return BigDecimal.ZERO;
        }

        return amount.multiply(coinUnit);
    }

    /**
     * 格式化输出的金额格式，最多8位小数
     *
     * @param s
     * @return
     */
    public static String scale(String s, int scale) {
        String amount[] = s.split("\\.");
        if (amount.length > 1) {
            if (amount[1].length() > scale) {
                return amount[0] + "." + amount[1].substring(0, scale);
            } else {
                return amount[0] + "." + amount[1];
            }
        } else {
            return amount[0];
        }
    }


    /**
     * 根据货币获取提现手续费
     *
     * @param coin
     * @return
     */
    public static String getWithdrawFee(String coin) {

        for (LocalCoinDbModel model : DataSupport.findAll(LocalCoinDbModel.class)) {

            if (TextUtils.equals(model.getSymbol(), coin)) {
                return amountFormatUnit(new BigDecimal(model.getWithdrawFeeString()), coin, 8);
            }
        }

        return "";
    }


    public static String formatCoinAmount(BigDecimal money) {
        DecimalFormat df = new DecimalFormat("#######0.########");
        String showMoney = df.format(money);

        return showMoney;
    }

    public static String formatCoinAmount(float money) {
        DecimalFormat df = new DecimalFormat("#######0.########");
        String showMoney = df.format(money);

        return showMoney;
    }

    public static String formatInt(double money) {
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format(money);

        return showMoney.substring(0, showMoney.length() - 1).split("\\.")[0];
    }

    public static String formatDoubleDiv(String money, int scale) {

        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format((Double.parseDouble(money) / scale));

        return showMoney.substring(0, showMoney.length() - 1);
    }

    public static String getCurrency(String currency) {
        switch (currency) {
            case "ETH":
                return StringUtil.getString(R.string.coin_eth);

            case "BTC":
                return StringUtil.getString(R.string.coin_btc);

            default:
                return "";

        }
    }

    public static String formatBizType(String bizType) {
        switch (bizType) {
            case "charge":
                return StringUtil.getString(R.string.biz_type_charge);

            case "withdraw":
                return StringUtil.getString(R.string.bill_type_withdraw);

            case "tradefee":
                return StringUtil.getString(R.string.biz_type_tradefee);

            case "withdrawfee":
                return StringUtil.getString(R.string.biz_type_withdrawfee);

            default:
                return "";

        }
    }


    public static String formatBillStatus(String status) {

        switch (status) {

            case "1":
                return StringUtil.getString(R.string.biz_status_daiduizhang);

            case "3":
                return StringUtil.getString(R.string.biz_status_yiduiyiping);

            case "4":
                return StringUtil.getString(R.string.biz_status_zhangbuping);

            case "5":
                return StringUtil.getString(R.string.biz_status_yiduibuping);

            case "6":
                return StringUtil.getString(R.string.biz_status_wuxuduizhang);

            default:
                return "";
        }

    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static String add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).toPlainString();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static String sub(double value1, double value2, String coin) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return amountFormatUnit(b1.subtract(b2), coin, 8);
    }


    /**
     * 去掉末尾的零
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
            return trim(str.substring(0, str.length() - 1));
        } else {
            return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
        }
    }
}
