package com.cdkj.baselibrary.interfaces;

/**
 * 网络请求接口
 * Created by cdkj on 18//30.
 */
public interface NetReaquestErrorVerify {

    void onReqFailure(String code, String desc);

    void onLoginFailure(String code, String desc);

    void onNoNet();
}
