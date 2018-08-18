package com.cdkj.token.interfaces;

import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.token.model.db.LocalCoinDbModel;

import java.util.List;

/**
 * Created by cdkj on 2017/8/8.
 */

public interface UserInfoInterface {
    void onStartGetUserInfo();
    void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg);
}
