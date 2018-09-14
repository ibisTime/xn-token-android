package com.cdkj.token.find.product_application.red_package;

import com.cdkj.baselibrary.api.BaseApiServer;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.mvp.BaseMVPModel;
import com.cdkj.baselibrary.base.mvp.BasePresenter;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.cdkj.token.model.MySendRedPackageBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.wallet.WalletFragment.HIND_SIGN;


/**
 * 红包历史
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketHistoryPresenter extends BasePresenter<RedPacketHistoryView> {

    public final static int TYPE_SEND = 0; //发送红包
    public final static int TYPE_GET = 1; //获取红包

    public int redPacketType;

    public RedPacketHistoryPresenter() {

    }

    public int getRedPacketType() {
        return redPacketType;
    }

    public boolean isSendStatus() {
        return redPacketType == TYPE_SEND;
    }


    public void setRedPacketType(int redPacketType) {
        this.redPacketType = redPacketType;
        switch (redPacketType) {
            case TYPE_SEND:
                getMvpView().setSendTotal("0");
                break;
            case TYPE_GET:
                getMvpView().setGetTotal("0");
                break;
        }
    }

    public void toggleRedPacketType() {
        switch (redPacketType) {
            case TYPE_SEND:
                this.redPacketType = TYPE_GET;
                break;
            case TYPE_GET:
                this.redPacketType = TYPE_SEND;
                break;
        }
    }


    /**
     * 获取筛选时间
     */
    public void getFilterDates() {

        if (isViewDetached()) {
            return;
        }

        Call<BaseResponseListModel<String>> call = RetrofitUtils.createApi(BaseApiServer.class).stringListRequest("623008", "{}");

        call.enqueue(new BaseResponseListCallBack<String>(null) {
            @Override
            protected void onSuccess(List<String> data, String SucMessage) {
                getMvpView().setFilterYears(data);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取历史数据
     *
     * @param start
     * @param limit
     * @param year
     * @param isShlowDialog
     */
    public void getHistoryData(String start, String limit, String year, boolean isShlowDialog) {

        switch (redPacketType) {
            case TYPE_GET:
                getSendHistoryData(start, limit, year, isShlowDialog);
                break;
            case TYPE_SEND:
                getGetHistoryData(start, limit, year, isShlowDialog);
                break;
        }

    }

    /**
     * 获取历史数据
     *
     * @param year
     * @param isShlowDialog
     */
    public void getHistoryData(String year, boolean isShlowDialog) {

        switch (redPacketType) {
            case TYPE_GET:
                getGetHistoryData("1", "10", year, isShlowDialog);
                break;
            case TYPE_SEND:
                getSendHistoryData("1", "10", year, isShlowDialog);
                break;
        }

    }


    private void getSendHistoryData(String start, String limit, String year, boolean isShlowDialog) {

        if (isViewDetached()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", start);//红包类型
        map.put("limit", limit);
        map.put("year", year);

        if (isShlowDialog) {
            getMvpView().shlowLoadDialog();
        }

        Call<BaseResponseModel<ResponseInListModel<MySendRedPackageBean.ListBean>>> call = RetrofitUtils.createApi(MyApi.class).getSendRedPackage("623005", StringUtils.objectToJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MySendRedPackageBean.ListBean>>(null) {
            @Override
            protected void onSuccess(ResponseInListModel<MySendRedPackageBean.ListBean> data, String SucMessage) {
                getMvpView().setSendRedPacketHistoryData(data);
                getMvpView().setSendTotal(data.getTotalCount() + "");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                getMvpView().showInfoDialog(errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShlowDialog) {
                    getMvpView().disMissLoadDialog();
                }
            }
        });
    }


    private void getGetHistoryData(String start, String limit, String year, boolean isShlowDialog) {

        if (isViewDetached()) {
            return;
        }

        if (isShlowDialog) {
            getMvpView().shlowLoadDialog();
        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", start);
        map.put("limit", limit);
        map.put("year", year);
        Call<BaseResponseModel<ResponseInListModel<MyGetRedPackageBean>>> call = RetrofitUtils.createApi(MyApi.class).getGetRedPackage("623007", StringUtils.objectToJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyGetRedPackageBean>>(null) {
            @Override
            protected void onSuccess(ResponseInListModel<MyGetRedPackageBean> data, String SucMessage) {
                getMvpView().setGetRedPacketListData(data);
                getMvpView().setGetTotal(data.getTotalCount() + "");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

                getMvpView().showInfoDialog(errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShlowDialog) {
                    getMvpView().disMissLoadDialog();
                }
            }
        });

    }


    @Override
    protected BaseMVPModel createBaseMVPModel() {
        return null;
    }

}
