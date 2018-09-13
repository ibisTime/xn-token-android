package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.mvp.BaseMVPModel;
import com.cdkj.baselibrary.base.mvp.BasePresenter;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.RedPackageHistoryBean;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/9/12.
 */

public class SendRedPacketPresenter extends BasePresenter<SendRedPacketView> {

    public final static int TYPE_ORDINARY = 0; //普通红包
    public final static int TYPE_LUCKY = 1; //手气红包

    public int redPacketType = TYPE_LUCKY;


    private final int maxRedPacketNumber = 10000; //最大红包个数

    private final int maxCoinCount = 1000000;//最大币数量

    private final float minCoinCount = 0.001f;//最小币数量


    public void setLayoutByType(int type) {
        redPacketType = type;
        switch (type) {
            case TYPE_ORDINARY:
                getMvpView().setOrdinaryStatus();
                break;
            case TYPE_LUCKY:
                getMvpView().setluckyStatus();
                break;
        }
    }


    /**
     * 获取默认币种
     */
    public void getDefalutCoin() {

        if (isViewDetached()) {
            return;
        }

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        getMvpView().showLoadDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(null) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                if (data == null || StringUtils.checkPostionCrossingInList(data.getAccountList(), 0)) {
                    return;
                }
                getMvpView().setDefaluteCoin(data.getAccountList().get(0));
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                getMvpView().onError(errorMessage, errorCode);
            }

            @Override
            protected void onFinish() {
                getMvpView().disMissLoadDialog();
            }
        });

    }


    /**
     * 发红包
     *
     * @param tradePwd    支付密码
     * @param sendNumber  发送数量
     * @param moneyNumber 发送金额
     * @param greeting    祝福语
     * @param coinSymbol  币种
     */
    public void sendRedPacket(String tradePwd, String sendNumber, String moneyNumber, String greeting, String coinSymbol) {

        if (isViewDetached()) {
            return;
        }


        getMvpView().showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("symbol", coinSymbol);//币种
        map.put("type", redPacketType + "");
        map.put("count", moneyNumber);
        map.put("sendNum", sendNumber);
        map.put("greeting", greeting);
        map.put("tradePwd", tradePwd);//支付密码
        Call<BaseResponseModel<RedPackageHistoryBean>> baseResponseModelCall = RetrofitUtils.createApi(MyApi.class).sendRedPackage("623000", StringUtils.getJsonToString(map));
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<RedPackageHistoryBean>(null) {
            @Override
            protected void onSuccess(RedPackageHistoryBean data, String SucMessage) {
                getMvpView().setSendSuccessStatus(data.getCode());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                getMvpView().setSendFailStatus();
                getMvpView().showInfoDialog(errorMessage);
            }

            @Override
            protected void onFinish() {
                getMvpView().disMissLoadDialog();
            }
        });

    }

    /**
     * 检测输入状态
     *
     * @param sendNumber
     * @param moneyNumber
     * @param coinSymbol
     * @return
     */

    public boolean checkInputState(String sendNumber, String moneyNumber, String coinSymbol) {
        if (TextUtils.isEmpty(coinSymbol)) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_please_type));
            return false;
        }

        if (TextUtils.isEmpty(moneyNumber)) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_piease_send_number));
            return false;
        }

        Double moneyDoubl = Double.parseDouble(moneyNumber);

        if (moneyDoubl < minCoinCount) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_min_numer, minCoinCount + ""));
            return false;
        }

        if (moneyDoubl > maxCoinCount) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_max_numer, maxCoinCount));
            return false;
        }

        if (TextUtils.isEmpty(sendNumber)) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_pleas_number));
            return false;
        }

        Double sendDoubl = Double.parseDouble(sendNumber);

        if (sendDoubl < 1) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_min_number));

            return false;
        }

        if (sendDoubl > maxRedPacketNumber) {
            getMvpView().showInfoDialog(MyApplication.getInstance().getString(R.string.red_package_mxn_number, maxRedPacketNumber));
            return false;
        }

        return true;
    }

    /**
     * 计算普通红包总额
     *
     * @param amount
     * @param number
     * @return
     */
    public String calculateInputTotal(String amount, String number) {

        if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(number)) {
            return "0";
        }

        if (redPacketType == TYPE_ORDINARY) {

            double v1 = Double.parseDouble(amount);
            double v2 = Double.parseDouble(number);
            if (v1 <= 0 || v2 <= 0) {
                return "0";
            }
            double total = v1 * v2;
            DecimalFormat df = new DecimalFormat("#######0.###");
            String sendTotalCoinString = df.format(total);

            return sendTotalCoinString;
        }

        return "0";
    }

    @Override
    protected BaseMVPModel createBaseMVPModel() {
        return null;
    }


}
