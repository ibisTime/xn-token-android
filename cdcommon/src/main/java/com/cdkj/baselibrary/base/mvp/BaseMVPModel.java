package com.cdkj.baselibrary.base.mvp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;

/**
 * Created by cdkj on 2018/9/10.
 */

public class BaseMVPModel {

    private List<Call> mCallList = new ArrayList<>();

    protected CompositeDisposable mSubscription = new CompositeDisposable();

    protected void addCall(Call call) {
        mCallList.add(call);
    }

    protected void clearCall() {
        mSubscription.dispose();
        mSubscription.clear();
        for (Call call : mCallList) {
            if (call == null) {
                continue;
            }
            call.cancel();
        }

    }

}
