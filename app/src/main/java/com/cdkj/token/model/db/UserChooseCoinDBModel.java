package com.cdkj.token.model.db;

import org.litepal.crud.DataSupport;

/**
 * 用户选择币种
 * Created by cdkj on 2018/6/30.
 */

public class UserChooseCoinDBModel extends DataSupport {

    public String userId;

    public String chooseCoins;  //用户选择的币种用，分割

    public int isChoosed;//用户是否进行过选择 0 没有 1有

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChooseCoins() {
        return chooseCoins;
    }

    public void setChooseCoins(String chooseCoins) {
        this.chooseCoins = chooseCoins;
    }

    public int getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(int isChoosed) {
        this.isChoosed = isChoosed;
    }
}
