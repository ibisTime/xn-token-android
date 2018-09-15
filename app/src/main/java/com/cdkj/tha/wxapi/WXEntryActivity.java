package com.cdkj.tha.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private UITipDialog tipDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWx();
    }

    private void initWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxUtil.APPID, false);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法！
    @Override
    public void onResp(BaseResp resp) {

        System.out.print("resp.getType()=" + resp.getType());

        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {// 分享
            String result = "";
            System.out.println("resp.errCode=" + resp.errCode);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = getString(R.string.share_succ);
                    showDialog(0, result);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = getString(R.string.share_cancel);
                    showDialog(3, result);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = getString(R.string.share_fail);
                    showDialog(1, result);
                    break;
                default:
                    result = getString(R.string.share_fail);
                    showDialog(1, result);
                    break;
            }
            return;
        }

    }

    public void showDialog(int type, String info) {

        if (type == 0) {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(info)
                    .create();
            tipDialog.show();
        } else if (type == 1) {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord(info)
                    .create();

        } else {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_INFO)
                    .setTipWord(info)
                    .create();
        }

        tipDialog.show();

        mSubscription.add(Observable.timer(1200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tipDialog.dismiss();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tipDialog.dismiss();
                        finish();
                    }
                }));
    }


}