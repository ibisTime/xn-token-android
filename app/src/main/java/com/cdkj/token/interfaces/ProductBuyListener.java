package com.cdkj.token.interfaces;

import com.cdkj.token.model.ProductBuyStep2Model;

/**
 * Created by cdkj on 2018/8/18.
 */

public interface ProductBuyListener {

    //购买监听
    void onBuyStep1(ProductBuyStep2Model buyStep2Model);

    void onBuyStep2(ProductBuyStep2Model buyStep2Model);


}
