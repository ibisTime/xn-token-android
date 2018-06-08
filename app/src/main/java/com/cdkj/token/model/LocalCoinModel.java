package com.cdkj.token.model;

import org.litepal.crud.DataSupport;

/**
 * 本地币种类型
 * Created by cdkj on 2018/6/8.
 */

public class LocalCoinModel{

    private String coinEName;//币种英文名称

    private String coinCName;//币种中文名称

    private String coinType;//币种类型 用于区分是什么币种

    public String getCoinEName() {
        return coinEName;
    }

    public void setCoinEName(String coinEName) {
        this.coinEName = coinEName;
    }

    public String getCoinCName() {
        return coinCName;
    }

    public void setCoinCName(String coinCName) {
        this.coinCName = coinCName;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }
}
