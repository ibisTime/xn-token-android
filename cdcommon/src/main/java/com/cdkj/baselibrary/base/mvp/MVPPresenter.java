package com.cdkj.baselibrary.base.mvp;

/**
 * Created by 李先俊 on 2018/9/10.
 */

public interface MVPPresenter<V extends MVPView> {

    void attachView (V mvpView);

    void detachView ();
}