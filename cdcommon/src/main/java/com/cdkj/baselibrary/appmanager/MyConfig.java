package com.cdkj.baselibrary.appmanager;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;

import java.util.Locale;


public class MyConfig {

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


    //默认七牛url
    public static String IMGURL = "http://pajvine9a.bkt.clouddn.com/";

    // 拍照文件保存路径
    public static final String CACHDIR = "tha_photo";


    // 环境访问地址
    public static final String BASE_URL_DEV = "http://120.26.6.213:2101/forward-service/"; // 研发
    public static final String BASE_URL_TEST = "http://120.26.6.213:2101/forward-service/"; // 测试
    //        public static final String BASE_URL_ONLINE = "http://120.26.6.213:2101/forward-service/"; // 测试
    public static final String BASE_URL_ONLINE = "http://47.75.165.70:2101/forward-service/"; // 线上


    /**
     * 获取转账节点类型
     *
     * @return
     */
    public static int getThisNodeType() {
        if (LogUtil.isLog) {
            return NODE_DEV;
        }
        return NODE_DEV;
    }

    /**
     * 获取网络请求URL
     *
     * @return
     */
    public static String getBaseURL() {
        if (false) {
            switch (SPUtilHelper.getAPPBuildType()) {
                case BUILD_TYPE_TEST: // 测试
                    return MyConfig.BASE_URL_DEV;
                default: // 研发
                    return MyConfig.BASE_URL_DEV;
            }
        } else {
            // 线上
            return MyConfig.BASE_URL_ONLINE;
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
                    SPUtilHelper.saveLanguage(MyConfig.SIMPLIFIED);  //简体中文
                    break;
                case "0082":
                    SPUtilHelper.saveLanguage(MyConfig.KOREA);  //韩语
                    break;
                default:
                    SPUtilHelper.saveLanguage(MyConfig.ENGLISH);  //英语
            }
            AppUtils.setAppLanguage(context, getUserLanguageLocal());   //设置用户使用语言
        }
    }

}
