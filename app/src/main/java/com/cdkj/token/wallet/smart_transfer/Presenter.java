package com.cdkj.token.wallet.smart_transfer;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Subscription;

/**
 * Created by cdkj on 2018/9/10.
 */

abstract class Presenter<T extends MvpView> implements BasePresenter<T> {

    public BaseModel baseModel;
    protected Reference<T> mViewRef;  //View接口类型的弱引用

    abstract BaseModel createBaseModel();

    @Override
    public void attachView(T mvpView) {
        mViewRef = new WeakReference<T>(mvpView);
        baseModel = createBaseModel();
    }

    @Override
    public void detachView() {
        if (baseModel != null) {
            baseModel.clearCall();
            baseModel = null;
        }
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public boolean isViewDetached() {
        return mViewRef == null || mViewRef.get() == null || baseModel == null  ;
    }

    public T getMvpView() {
        return mViewRef.get();
    }

}

