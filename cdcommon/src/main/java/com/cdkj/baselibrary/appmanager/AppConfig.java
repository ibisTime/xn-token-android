package com.cdkj.baselibrary.appmanager;


import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;

import java.util.Locale;


public class AppConfig {

    //系统参数
    public final static String COMPANYCODE = "CD-TOKEN00018";
    public final static String SYSTEMCODE = "CD-TOKEN00018";
    public final static String USERTYPE = "C";//用户类型


    //    public static final String TRADITIONAL = "traditional";
    public static final String SIMPLIFIED = "ZH_CN";
    public static final String ENGLISH = "EN";
    public static final String KOREA = "KO";


    // app运行环境
    public static final String BUILD_TYPE_TEST = "0";
    public static final String BUILD_TYPE_DEBUG = "1";
    public static final String BUILD_TYPE_RELEASE = "2";


    //以太网络节点
    public final static int NODE_DEV = 0; //研发测试环境
    public final static int NODE_REALSE = 1;//真实环境


    public final static String LOCAL_MARKET_CNY = "CNY";// 币种显示类型 人民币
    public final static String LOCAL_MARKET_USD = "USD";// 币种显示类型 美元
    public final static String LOCAL_MARKET_KRW = "KRW";// 币种显示类型 韩元


    public final static String LOCAL_COIN_USD_SYMBOL = "$";// 币种显示类型 美元
    public final static String LOCAL_COIN_CNY_SYMBOL = MoneyUtils.MONEYSING;// 币种显示类型 人民币
    public final static String LOCAL_COIN_KRW_SYMBOL = "₩";// 币种显示类型 韩币


    //默认七牛url
    public static String IMGURL = "http://pajvine9a.bkt.clouddn.com/";

    // 拍照文件保存路径
    public static final String CACHDIR = "tha_photo";


    // 环境访问地址
    public static final String BASE_URL_DEV = "http://m.dev.thachain.org/"; // 研发
    public static final String BASE_URL_TEST = "https://test.thachain.org/"; // 测试
    public static final String BASE_URL_ONLINE = "https://app.tha.hichengdai.com/"; // 线上


    public static String getZenDeskUrl() {
        return "https://thachainhelp.zendesk.com" + AppConfig.getZenDeskUrlLanguage();
    }

    /**
     * 获取Zendesk语言类型
     *
     * @return
     */
    public static String getZenDeskUrlLanguage() {
        switch (SPUtilHelper.getLanguage()) {
            case ENGLISH:
                return "/hc/en-us/";
            case KOREA:
                return "/hc/ko/";
            case SIMPLIFIED:
                return "/hc/zh-cn/";
            default:
                return "";
        }
    }


    /**
     * 获取转账节点类型
     *
     * @return
     */
    public static int getThisNodeType() {
        if (LogUtil.isLog) {
            return NODE_DEV;
        }
        return NODE_REALSE;
    }

    /**
     * 获取网络请求URL
     *
     * @return
     */
    public static String getBaseURL() {
        if (LogUtil.isLog) {
            switch (SPUtilHelper.getAPPBuildType()) {
                case BUILD_TYPE_TEST: // 测试
                    return AppConfig.BASE_URL_TEST;
                default: // 研发
                    return AppConfig.BASE_URL_DEV;
            }
        } else {
            // 线上
            return AppConfig.BASE_URL_ONLINE;
        }
    }


    /**
     * 设置APP使用的语言
     */
    public static Locale getUserLanguageLocal() {
        switch (SPUtilHelper.getLanguage()) {
            case ENGLISH:
                return Locale.ENGLISH;
            case KOREA:
                return Locale.KOREA;
            case SIMPLIFIED:
                return Locale.SIMPLIFIED_CHINESE;
            default:
                return Locale.ENGLISH;
        }
    }

    /**
     * 选择国家的时候改变语言
     *
     * @param countryCode
     */
    public static void changeLanguageForCountry(Context context, String countryCode) {
        if (!TextUtils.isEmpty(countryCode)) {
            switch (countryCode) {
                case "0086":  //中国
                case "00886":  //中国台湾
                case "00852":  //中国香港
                case "00853":   //中国澳门
                    SPUtilHelper.saveLanguage(AppConfig.SIMPLIFIED);  //简体中文
                    break;
                case "0082":
                    SPUtilHelper.saveLanguage(AppConfig.KOREA);  //韩语
                    break;
                default:
                    SPUtilHelper.saveLanguage(AppConfig.ENGLISH);  //英语
            }
            AppUtils.setAppLanguage(context, getUserLanguageLocal());   //设置用户使用语言
        }
    }

    /**
     * 选择国家的时候改变语言
     *
     * @param countryCode
     */
    public static void changeLocalCoinTypeForCountry(String countryCode) {
        if (!TextUtils.isEmpty(countryCode)) {
            switch (countryCode) {
                case "0086":  //中国
                case "00886":  //中国台湾
                case "00852":  //中国香港
                case "00853":   //中国澳门
                    SPUtilHelper.saveLocalMarketSymbol(AppConfig.LOCAL_MARKET_CNY);  //人民币
                    break;
                case "0082":
                    SPUtilHelper.saveLocalMarketSymbol(AppConfig.LOCAL_MARKET_KRW);  //韩币
                    break;
                default:
                    SPUtilHelper.saveLocalMarketSymbol(AppConfig.LOCAL_MARKET_USD);  //美元
            }
        }
    }


    /**
     * 根据本地货币获取显示货币符号
     *
     * @param coinType
     * @return
     */

    public static String getSymbolByType(String coinType) {
        if (isLocalMarketCNY(coinType)) {
            return AppConfig.LOCAL_COIN_CNY_SYMBOL;
        } else if (isLocalMarketUSD(coinType)) {
            return AppConfig.LOCAL_COIN_USD_SYMBOL;
        } else if (isLocalMarketKRW(coinType)) {
            return AppConfig.LOCAL_COIN_KRW_SYMBOL;
        }
        return "";
    }

    /**
     * 是否cny价格
     *
     * @param coinType
     * @return
     */
    public static boolean isLocalMarketCNY(String coinType) {
        return TextUtils.equals(coinType, AppConfig.LOCAL_MARKET_CNY);
    }

    /**
     * 是否usd价格
     *
     * @param coinType
     * @return
     */
    public static boolean isLocalMarketUSD(String coinType) {
        return TextUtils.equals(coinType, AppConfig.LOCAL_MARKET_USD);
    }

    /**
     * 是否KRW价格
     *
     * @param coinType
     * @return
     */
    public static boolean isLocalMarketKRW(String coinType) {
        return TextUtils.equals(coinType, AppConfig.LOCAL_MARKET_KRW);
    }


}
