package com.cdkj.baselibrary.appmanager;

/**
 * Created by 李先俊 on 2017/7/18.
 */

public class EventTags {


    //修改手机号后刷新数据
    public final static String CHANGEPHONENUMBER_REFRESH = "1";
    //修改昵称后刷新数据
    public final static String CHANGENICK_REFRESH = "10";
    //修改资金密码
    public final static String CHANGE_PAY_PWD_REFRESH = "2";
    //结束所有界面
    public final static String AllFINISH = "3";
    //改变MainActivity显示界面
    public final static String MAIN_CHANGE_SHOW_INDEX = "4";
    //结束MAINACTIVITY
    public final static String MAINFINISH= "5";
    //登录成功刷新
    public final static String LOGINREFRESH= "6";
    //登录成功刷新
    public final static String BUILD_TYPE= "7";
    // 刷新语言
    public final static String EVENT_REFRESH_LANGUAGE= "8";

    public final static String CHANGE_CODE_BTN= "change_code_btn";

    // 提币地址选择
    public final static String ADDRESS_SELECT= "address_select";

    //
    public final static String BASE_COIN_LIST= "base_coin_list";
    public final static String BASE_COIN_LIST_NOTIFY_ALL= "base_coin_list_notify_all";
    public final static String BASE_COIN_LIST_NOTIFY_SINGEL= "base_coin_list_notify_single";
}
