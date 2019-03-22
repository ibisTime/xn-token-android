package com.cdkj.token.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.token.model.AddressModel;
import com.cdkj.token.model.AppQuestionModel;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.BiJiaBaoAvilModel;
import com.cdkj.token.model.BillModel;
import com.cdkj.token.model.BjbMyIncome;
import com.cdkj.token.model.BtcFeesModel;
import com.cdkj.token.model.ChoiceCoinModel;
import com.cdkj.token.model.CionAddressType;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.ConsultListModel;
import com.cdkj.token.model.ConsultModel;
import com.cdkj.token.model.ConsultingModel;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.model.DAppGuideModel;
import com.cdkj.token.model.DAppModel;
import com.cdkj.token.model.DealDetailModel;
import com.cdkj.token.model.DealHistoryModel;
import com.cdkj.token.model.DealModel;
import com.cdkj.token.model.DealResultModel;
import com.cdkj.token.model.DealUserDataModel;
import com.cdkj.token.model.ExchangeModel;
import com.cdkj.token.model.GasPrice;
import com.cdkj.token.model.IncomeRankTopModel;
import com.cdkj.token.model.InvestBillModel;
import com.cdkj.token.model.InvestmentAmountModel;
import com.cdkj.token.model.InviteModel;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.model.LocalEthTokenCoinBill;
import com.cdkj.token.model.LocalUSDTCoinBill;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.model.MarketCoinModel;
import com.cdkj.token.model.MarketModel;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.model.MySendRedPackageBean;
import com.cdkj.token.model.OrderDetailModel;
import com.cdkj.token.model.OrderModel;
import com.cdkj.token.model.QuestionFeedbackModel;
import com.cdkj.token.model.RateModel;
import com.cdkj.token.model.RecommendAppModel;
import com.cdkj.token.model.RedPackageDetialsBean;
import com.cdkj.token.model.RedPackageHistoryBean;
import com.cdkj.token.model.RedPacketDetails;
import com.cdkj.token.model.StatisticsListModel;
import com.cdkj.token.model.SystemMessageModel;
import com.cdkj.token.model.SystemParameterListModel;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.TrustModel;
import com.cdkj.token.model.TxHashModel;
import com.cdkj.token.model.UTXOListModel;
import com.cdkj.token.model.UserRefereeModel;
import com.cdkj.token.model.UserSettingModel;
import com.cdkj.token.model.VersionModel;
import com.cdkj.token.model.WithdrawOrderModel;
import com.cdkj.token.model.db.LocalCoinDbModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by lei on 2017/10/19.
 */

public interface MyApi {


    /**
     * 获取投资流水
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<InvestBillModel>>> getInvestBillList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取国家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UTXOListModel>> getUtxoList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取可购买区间
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BiJiaBaoAvilModel>> getBuyInterval(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户投资金额
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<InvestmentAmountModel>> getUserInvestAmount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取红包详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RedPacketDetails>> getRedPacketHistoryDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取红包详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AppQuestionModel>> getAPPQuestionList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取国家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<LocalCoinDbModel>> getCoinFees(@Field("code") String code, @Field("json") String json);

    /**
     * ETH网络gas均价格获取 WAN网络gas均价格获取
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<GasPrice>> getGasPrice(@Field("code") String code, @Field("json") String json);

    /**
     * 获取理财产品列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<ManagementMoney>>> getMoneyManageProductList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取理我的财产品列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MyManamentMoneyProduct>>> getMyMoneyManageProductList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取理财产品详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ManagementMoney>> getMoneyManageProductDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取应用列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<RecommendAppModel>> getAppList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取应用列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<DAppModel>>> getDAppList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取应用列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DAppModel>> getDApp(@Field("code") String code, @Field("json") String json);

    /**
     * 获取应用列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<DAppGuideModel.DAppGuide>>> getDAppGuideList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取BTC流水记录（去中心化钱包）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<BTCBillModel>>> getBTCBillList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取BTC矿工费用
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BtcFeesModel>> getBtcFees(@Field("code") String code, @Field("json") String json);

    /**
     * btc交易广播
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TxHashModel>> btcTransactionBroadcast(@Field("code") String code, @Field("json") String json);


    /**
     * 根据Ip获取国家信息
     *
     * @return
     */
    @Headers({
            "Accept: text/html, application/xhtml+xml, application/xml; q=0.9, */*; q=0.8",
            "Cache-Control: no-cache",
            "Connection: Keep-Alive",
            "Host: ip.taobao.com",
            "Upgrade-Insecure-Requests: 1",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134",
    })
    @GET
    Call<String> getCountryInfoByIp(@Url String url);

    /**
     * 获取国家列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<CountryCodeMode>> getCountryList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取问题反馈列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<QuestionFeedbackModel>>> getQuestionFeedbackList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取问题反馈列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<QuestionFeedbackModel>> getQuestionFeedbackDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取本地币种流水
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<LocalCoinBill>>> getLocalCoinBillList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取本地币种流水
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<LocalUSDTCoinBill>>> getUSDTCoinBillList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取本地币种流水
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<LocalEthTokenCoinBill>>> getEthTokenCoinBillList(@Field("code") String code, @Field("json") String json);

    /**
     * 根据币种列表获取相应的币种信息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BalanceListModel>> getBalanceList(@Field("code") String code, @Field("json") String json);

    /**
     * 根据币种列表获取相应的币种信息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Observable<BaseResponseModel<BalanceListModel>> getBalanceListObservable(@Field("code") String code, @Field("json") String json);

    /**
     * 根据币种列表获取相应的币种信息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinModel>> getBalanceList2(@Field("code") String code, @Field("json") String json);


    /**
     * 获取消息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MsgListModel>> getMsgList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取消息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Observable<BaseResponseModel<MsgListModel>> getMsgListObservable(@Field("code") String code, @Field("json") String json);

    /**
     * 获取空投流水
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<StatisticsListModel>> getKtBillList(@Field("code") String code, @Field("json") String json);


    /**
     * 获取空投信息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel> getTkInfo(@Field("code") String code, @Field("json") String json);


    /**
     * 获取店铺详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ConsultModel>> getConsultDetail(@Field("code") String code, @Field("json") String json);


    /**
     * 获取店铺列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ConsultListModel>> getConsultList(@Field("code") String code, @Field("json") String json);


    /**
     * 注册
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserLoginModel>> signUp(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinModel>> getAccount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Observable<BaseResponseModel<CoinModel>> getAccountObservable(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealHistoryModel>> getDealHistory(@Field("code") String code, @Field("json") String json);

    /**
     * 获取统计信息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealUserDataModel>> getDealUserData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BillModel>> getBillListData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户信息详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取轮播图
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<BannerModel>> getBanner(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterModel>> getSystemParameter(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterListModel>> getSystemParameterList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<SystemParameterModel>> getSystemInformation(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户设置
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<UserSettingModel>> getUserSetting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取邀请数据
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<InviteModel>> getInvite(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoin(@Field("code") String code, @Field("json") String json);


    /**
     * 数字货币，平台干预后的价格
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MarketCoinModel>> getTruePrice(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RateModel>> getRate(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<RateModel>> getRateList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketModel>> getMarket(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统消息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemMessageModel>> getSystemMessage(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统资讯
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ConsultingModel>> getConsulting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealModel>> getDeal(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<DealDetailModel>> getDealList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<WithdrawOrderModel>> getWithdrawOrder(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealDetailModel>> getDealDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取信任列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TrustModel>> getTrust(@Field("code") String code, @Field("json") String json);

    /**
     * 获取推荐列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserRefereeModel>> getUserReferee(@Field("code") String code, @Field("json") String json);

    /**
     * 获取汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ExchangeModel>> getExchange(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易结果
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealResultModel>> getDealResult(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderModel>> getOrder(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderDetailModel>> getOrderDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取地址
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AddressModel>> getAddress(@Field("code") String code, @Field("json") String json);

    /**
     * 获取版本
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<VersionModel>> getVersion(@Field("code") String code, @Field("json") String json);

    /**
     * 获取支持的币种
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<LocalCoinDbModel>> getCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 列表查询可自选的币种
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<ChoiceCoinModel>> getCoinChoiceList(@Field("code") String code, @Field("json") String json);

    /**
     * 添加自选/删除自选
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<IsSuccessModes>> doChoice(@Field("code") String code, @Field("json") String json);

    /**
     * 获取支持的币种
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Observable<BaseResponseListModel<LocalCoinDbModel>> getCoinListObservable(@Field("code") String code, @Field("json") String json);


    /**
     * 发红包
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RedPackageHistoryBean>> sendRedPackage(@Field("code") String code, @Field("json") String json);


    /**
     * 获取我发送的红包数据
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MySendRedPackageBean.ListBean>>> getSendRedPackage(@Field("code") String code, @Field("json") String json);

    /**
     * 获取我抢的红包数据
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ResponseInListModel<MyGetRedPackageBean>>> getGetRedPackage(@Field("code") String code, @Field("json") String json);

    /**
     * 红包详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RedPackageDetialsBean>> getRedPackageDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 我的收益查询
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BjbMyIncome>> getMyIncome(@Field("code") String code, @Field("json") String json);

    /**
     * 收益排行Top100
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<IncomeRankTopModel>> getIncomeRankTop(@Field("code") String code, @Field("json") String json);

    /**
     * 收益排行Top100
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CionAddressType>> transferInputCheckAddressType(@Field("code") String code, @Field("json") String json);

}
