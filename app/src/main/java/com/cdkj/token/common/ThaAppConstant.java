package com.cdkj.token.common;

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
    public static final String H5_POP_PROTOCOL = "pop_protocol_"; //购买协议


    /**
     * 获取H5URL
     *
     * @param url
     * @return
     */
    public static String getH5UrlLangage(String url) {
        return url + SPUtilHelper.getLanguage();
    }

    /**
     * 获取红包分享路径
     *
     * @param redPackageCode
     * @return
     */
    public static String getRedPacketShareUrl(String redPackageCode, String inviteCode) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("/redPacket/receive.html?code=" + redPackageCode);//红包码

        stringBuffer.append("&inviteCode=" + inviteCode);// 邀请码

        stringBuffer.append("&lang=" + SPUtilHelper.getLanguage());//国际化

        return stringBuffer.toString();
    }

    /**
     * 获取红包分享路径
     *
     * @param
     * @return
     */
    public static String getCoinGameUrl() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/luckDraw/luckDraw.html?");

        stringBuffer.append("userId=" + SPUtilHelper.getUserId());// 邀请码

        stringBuffer.append("&lang=" + SPUtilHelper.getLanguage());//国际化

        return stringBuffer.toString();
    }


    /**
     * 获取邀请好友url :http://m.thadev.hichengdai.com/user/register.html?inviteCode=U201807030441369491006&lang=ZH_CN
     *
     * @param inviteCode
     * @return
     */
    public static String getInviteFriendUrl(String inviteCode) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("/user/register.html");

        stringBuffer.append("?inviteCode=" + inviteCode);// 邀请码

        stringBuffer.append("&lang=" + SPUtilHelper.getLanguage());//国际化

        return stringBuffer.toString();
    }


}
