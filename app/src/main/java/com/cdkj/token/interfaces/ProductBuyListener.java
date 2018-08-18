package com.cdkj.token.interfaces;

/**
 * Created by cdkj on 2018/8/18.
 */

public interface ProductBuyListener {

    //购买监听
    void onBuyStep1(String money);

    void onBuyStep2();

}
