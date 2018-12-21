package com.cdkj.token.interfaces;

import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;

/**
 * Created by cdkj on 2017/8/8.
 */

public interface IdentifyInterface {

    void onIdStart();
    void onIdEnd(IdResultCallback.IdResult result);
    void onFaceStart();
    void onFaceEnd(FaceResultCallback.FaceResult result);

    void onUpLoadStart();
    void onUpLoadFailure(String info);
    void onUpLoadSuccess();
    void onUpLoadFinish();
}
