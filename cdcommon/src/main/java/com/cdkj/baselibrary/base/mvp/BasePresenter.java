package com.cdkj.baselibrary.base.mvp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by cdkj on 2018/9/10.
 */

public abstract class BasePresenter<T extends MVPView> implements MVPPresenter<T> {

    public BaseMVPModel baseModel;
    protected Reference<T> mViewRef;  //View接口类型的弱引用

    protected abstract BaseMVPModel createBaseMVPModel();

    @Override
    public void attachView(T mvpView) {
        mViewRef = new WeakReference<T>(mvpView);
        baseModel = createBaseMVPModel();
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
        return mViewRef == null || mViewRef.get() == null;
    }

    public T getMvpView() {
        return mViewRef.get();
    }

}

