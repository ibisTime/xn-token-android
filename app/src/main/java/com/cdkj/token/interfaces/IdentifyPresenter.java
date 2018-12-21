package com.cdkj.token.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.zqzn.idauth.sdk.DetectEngine;
import com.zqzn.idauth.sdk.ErrorCode;
import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.AppConfig.ZHIQU_APP_KEY;
import static com.cdkj.baselibrary.appmanager.AppConfig.ZHIQU_SECRET_KEY;

/**
 * Created by cdkj on 2018/12/20.
 */

public class IdentifyPresenter implements IdResultCallback, FaceResultCallback {

    private Activity mActivity;
    private IdentifyInterface mListener;

    private QiNiuHelper qiNiuHelper;
    private DetectEngine detectEngine;

    private Call call;

    private String frontImage;
    private String backImage;
    private String faceImage;
    private List<Bitmap> dataList = new ArrayList<>();

    public IdentifyPresenter(Activity activity, IdentifyInterface listener) {
        this.mActivity = activity;
        this.mListener = listener;

        detectEngine = new DetectEngine();
    }

    public void startCardIndentify(){
        if (null == detectEngine)
            return;

        try {

            if (detectEngine.id_ocr(mActivity, ZHIQU_APP_KEY, ZHIQU_SECRET_KEY, this) != ErrorCode.SUCCESS.getCode()){
                ToastUtil.show(mActivity, "证件接口调用失败");
            }

            mListener.onIdStart();

        } catch (Exception e) {
            ToastUtil.show(mActivity, e.getMessage());
        }
    }

    private void startFaceIndentify(){
        if (null == detectEngine)
            return;

        try {

            if (detectEngine.face_liveness(mActivity, ZHIQU_APP_KEY, ZHIQU_SECRET_KEY, 1, this) != ErrorCode.SUCCESS.getCode()){
                ToastUtil.show(mActivity, "活体接口调用失败");
            }

            mListener.onFaceStart();

        } catch (Exception e) {
            ToastUtil.show(mActivity, e.getMessage());
        }
    }

    @Override
    public void notifyResult(IdResult result) {

        mListener.onIdEnd(result);

        if (ErrorCode.SUCCESS.getCode() == result.result_code){

            // 保存证件照正反面
            dataList.add(result.front_image);
            dataList.add(result.back_image);

            // 再启动活体检测
            startFaceIndentify();

        } else {
            ToastUtil.show(mActivity, "证件认证失败");
        }



    }

    @Override
    public void notifyResult(FaceResult result) {

        mListener.onFaceEnd(result);

        if (ErrorCode.SUCCESS.getCode() == result.result_code){

            // 保存脸图
            dataList.add(result.face_image);

            if (dataList.size() == 3){
                uploadPic();
            }

        } else {
            ToastUtil.show(mActivity, "活体认证失败");
        }

    }

    private void uploadPic(){
        if (qiNiuHelper == null) {
            qiNiuHelper = new QiNiuHelper(mActivity);
        }

        mListener.onUpLoadStart();

        qiNiuHelper.upLoadListPicByBitmap(dataList, new QiNiuHelper.upLoadListImageListener() {
            @Override
            public void onChange(int index, String url) {
                switch (index){
                    case 0:
                        frontImage = url;
                        break;

                    case 1:
                        backImage = url;
                        break;

                    case 2:
                        faceImage = url;
                        break;
                }
            }

            @Override
            public void onSuccess() {
                submitRequest();
            }

            @Override
            public void onFal(String info) {
                mListener.onUpLoadFailure(info);
            }

            @Override
            public void onError(String info) {
                mListener.onUpLoadFailure(info);
            }
        });
    }

    /**
     * 提交请求
     */
    private void submitRequest() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("frontImage", frontImage);
        map.put("backImage", backImage);
        map.put("faceImage", faceImage);

        Call<BaseResponseModel<String>> call = RetrofitUtils.getBaseAPiService().stringRequest("805197", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<String>(mActivity) {
            @Override
            protected void onSuccess(String data, String SucMessage) {
                mListener.onUpLoadSuccess();
            }

            @Override
            protected void onFinish() {
                mListener.onUpLoadFinish();
            }
        });

    }
}
