package com.cdkj.token.wallet.smart_transfer;

/**
 * Created by 李先俊 on 2018/9/10.
 */
public interface MvpView {

    void onShowLoadingDialog();

    void onDisMissLoadingDialog();

    void onError(String msg, String code);
}