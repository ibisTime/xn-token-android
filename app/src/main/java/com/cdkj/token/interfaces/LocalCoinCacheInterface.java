package com.cdkj.token.interfaces;

import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.token.model.db.LocalCoinDbModel;

import java.util.List;

/**
 * Created by cdkj on 2017/8/8.
 */

public interface LocalCoinCacheInterface {
    void cacheEnd(List<LocalCoinDbModel> data);    //币种缓存结束 是否缓存结束
}
