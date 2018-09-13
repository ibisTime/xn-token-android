package com.cdkj.token.find.product_application.red_package;

import com.cdkj.baselibrary.base.mvp.MVPView;
import com.cdkj.token.model.CoinModel;

/**
 * 发送红包
 * Created by cdkj on 2018/9/12.
 */

public interface SendRedPacketView extends MVPView {

    void setOrdinaryStatus();//设置普通红包状态

    void setluckyStatus();//设置拼手气红包状态

    void setBalance(String balanceString);//设置余额

    void setDefaluteCoin(CoinModel.AccountListBean defaluteCoin);//设置默认币种

    void setSendSuccessStatus(String redPacketCode);//红包发送成功

    void setSendFailStatus();//红包发送失败

    void showLoadDialog();

    void disMissLoadDialog();

    void showInfoDialog(String msgSgring);


}
