package com.cdkj.token.utils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 常量类
 * Created by cdkj on 2018/7/28.
 */

public class ThaAppConstant {


    public static final String H5_PRIVACY = "privacy_"; //隐私政策
    public static final String H5_MNEMONIC = "mnemonic_"; //什么是助记词
    public static final String H5_MNEMONIC_BACKUP = "mnemonic_backup_"; //如何备份助记词
    public static final String H5_RED_PACKET_RULE = "red_packet_rule_"; //红包规则
    public static final String H5_GLOBAL_MASTER = "global_master_"; //首创玩法
    public static final String H5_QUANTITATIVE_FINANCE = "quantitative_finance_"; //量化理财
    public static final String H5_YUBIBAO = "yubibao_"; //余币宝
    public static final String QUESTIONS = "questions_"; //常见问题


    public static String getH5UrlLangage(String url) {
        return url + SPUtilHelper.getLanguage();
    }


}
