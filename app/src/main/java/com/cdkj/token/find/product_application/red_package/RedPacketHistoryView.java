package com.cdkj.token.find.product_application.red_package;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.base.mvp.MVPView;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.cdkj.token.model.MySendRedPackageBean;

import java.util.List;

/**
 * 红包历史
 * Created by cdkj on 2018/9/13.
 */

public interface RedPacketHistoryView extends MVPView {


    void setSendTotal(String sendTotal);//设置发送状态

    void setGetTotal(String getTotal);//设置获取状态

    void setFilterYears(List<String> years);//获取筛选年份

    void setSendRedPacketHistoryData(ResponseInListModel<MySendRedPackageBean.ListBean> sendRedPacketHistoryData);//设置发送红包列表数据

    void setGetRedPacketListData(ResponseInListModel<MyGetRedPackageBean> getRedPacketListData);//设置获取红包列表数据

    void shlowLoadDialog();

    void disMissLoadDialog();

    void showInfoDialog(String msg);

}
