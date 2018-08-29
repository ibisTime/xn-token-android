package com.cdkj.token.interfaces;

import com.cdkj.token.model.VersionModel;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/8/29.
 */

public interface StartPageView {

    void addNetCall(Call call);//添加请求对象

    void versionUpdateDialog(VersionModel versionModel);//显示更新dialog

    void onStartPageEnd();

}
