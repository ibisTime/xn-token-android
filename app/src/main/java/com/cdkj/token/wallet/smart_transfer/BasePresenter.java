package com.cdkj.token.wallet.smart_transfer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by 李先俊 on 2018/9/10.
 */

public interface BasePresenter<V extends MvpView> {

    void attachView (V mvpView);

    void detachView ();
}