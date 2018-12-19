package com.cdkj.token.user;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.activitys.NickModifyActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.NickNameUpdate;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentUserBinding;
import com.cdkj.token.find.product_application.management_money.MyIncomeActivity;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.BjbMyIncome;
import com.cdkj.token.user.question_feedback.QuestionFeedbackSubmitActivity;
import com.cdkj.token.user.setting.UserSettingActivity;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.trade_pwd.TradePwdActivity;
import com.zqzn.idauth.sdk.DetectEngine;
import com.zqzn.idauth.sdk.IdResultCallback;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2018/6/28.
 */

public class UserFragment extends BaseLazyFragment implements UserInfoInterface,IdResultCallback {

    DetectEngine detectEngine = new DetectEngine();

    private FragmentUserBinding mBinding;

    public final int PHOTOFLAG = 110;

    private CommonDialog commonDialog;

    private UserInfoPresenter mGetUserInfoPresenter;//获取用户信息

    private UserInfoModel mData;

    private QiNiuHelper qiNiuHelper;
    private String frontImage;
    private String backImage;
    private String faceImage;

    private boolean isHasInfo;

    public static UserFragment getInstance() {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, null, false);

        init();
        initClickListener();

        mGetUserInfoPresenter = new UserInfoPresenter(this, mActivity);

        return mBinding.getRoot();
    }


    @Override
    public void onDestroy() {
        if (commonDialog != null) {
            commonDialog.closeDialog();
        }
        if (mGetUserInfoPresenter != null) {
            mGetUserInfoPresenter.clear();
        }
        super.onDestroy();
    }

    private void init() {
        isHasInfo = WalletHelper.isUserAddedWallet(WalletHelper.WALLET_USER);

        mBinding.tvPropertyHint.setVisibility(isHasInfo ? View.GONE : View.VISIBLE);
    }

    private void initClickListener() {

        mBinding.linLayoutIden.setOnClickListener(view -> {
            if (mData == null)
                return;

            if (TextUtils.isEmpty(mData.getRealName())){
                detectEngine.id_ocr(mActivity, "nJXnQp568zYcnBdPQxC7TANqakUUCjRZqZK8TrwGt7", "887DE27B914988C9CF7B2DEE15E3EDF8",this);
            } else {
                UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_iden_ok));
            }

        });

        // 问题反馈
        mBinding.linLayoutFeedback.setOnClickListener(view -> {
            QuestionFeedbackSubmitActivity.open(mActivity);
        });

        // 修改昵称
        mBinding.tvNickName.setOnClickListener(view -> {
            NickModifyActivity.open(mActivity, SPUtilHelper.getUserName());
        });

        // 我的收益
        mBinding.linLayoutIncome.setOnClickListener(view -> {
            MyIncomeActivity.open(mActivity);
        });

        //更换头像
        mBinding.imgLogo.setOnClickListener(view -> {
            ImageSelectActivity.launchFragment(this, PHOTOFLAG);
        });

        // 账户与安全
        mBinding.linLayoutUserAccount.setOnClickListener(view -> {
            UserSecurityActivity.open(mActivity);
        });

        // 加入社群
        mBinding.joinUs.setOnClickListener(view -> UserJoinActivity.open(mActivity));

        // 钱包工具
        mBinding.walletTool.setOnClickListener(view -> {

            if (!isHasInfo) {
                TradePwdActivity.open(mActivity, TradePwdActivity.CREATE, null, null);
                return;
            }
            WalletToolActivity.open(mActivity);
        });

        //帮助中心
        mBinding.helper.setOnClickListener(view -> {
            OtherLibManager.openZendeskHelpCenter(mActivity);
//            WebViewImgBgActivity.openkey(mActivity, getStrRes(R.string.user_issue), ThaAppConstant.getH5UrlLangage(ThaAppConstant.QUESTIONS));
        });

        //设置
        mBinding.setting.setOnClickListener(view -> UserSettingActivity.open(mActivity));

        //信用积分 保留
//        mBinding.tvCreditAmount.setOnClickListener(view -> {
//            CreditActivity.open(mActivity);
//        });
    }

    @Override
    protected void lazyLoad() {
        if (mBinding != null && mGetUserInfoPresenter != null) {
            mGetUserInfoPresenter.getUserInfoRequest();
        }

        // 页面显示时改变资产与收益的币种
        mBinding.tvPropertyTitle.setText(getString(R.string.wallet_assets, SPUtilHelper.getLocalMarketSymbol()));

        MainActivity activity = (MainActivity) getActivity();
        BigDecimal totalAsset = activity.getTotalAsset();
        if (null != totalAsset){
            mBinding.tvProperty.setText(totalAsset.toPlainString());
        }

        getTotalIncome();
    }

    @Override
    protected void onInvisible() {

    }


    /**
     * 设置用户数据显示
     *
     * @param data
     */
    private void setShowData(UserInfoModel data) {
        if (data == null) {
            mBinding.tvNickName.setText("");
            mBinding.tvPhoneNumber.setText("");
            mBinding.imgLogo.setImageResource(R.drawable.photo_default);

            return;
        }

        mData = data;

//        if (isShowUserCrown(data.getCreateNo())) {
//            mBinding.imgCrown.setVisibility(View.VISIBLE);
//        } else {
//            mBinding.imgCrown.setVisibility(View.GONE);
//        }

        if (data.getNickname() == null) return;

        mBinding.tvNickName.setText(data.getNickname());

        if (TextUtils.isEmpty(data.getMobile())){
            mBinding.tvPhoneNumber.setText(data.getEmail());
        }else {
            mBinding.tvPhoneNumber.setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()) + " " + StringUtils.ttransformShowPhone(data.getMobile()));
        }

        ImgUtils.loadLogo(mActivity, data.getPhoto(), mBinding.imgLogo);

        if (!TextUtils.isEmpty(data.getRealName())){
            mBinding.ivIden.setBackgroundResource(R.mipmap.user_iden_ok);
            mBinding.tvIden.setText(R.string.user_iden_ok);
            mBinding.tvIdenFlag.setText(R.string.user_iden_ok);
        } else {
            mBinding.ivIden.setBackgroundResource(R.mipmap.user_iden);
            mBinding.tvIden.setText(R.string.user_iden_not_ok);
            mBinding.tvIdenFlag.setText(R.string.user_iden_not_ok);
        }
    }

    /**
     * 判断能否显示创始图标
     *
     * @param userNo 用户编号
     * @return
     */
    private boolean isShowUserCrown(long userNo) {
        return userNo < 10000;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            showLoadingDialog();
            String path = data.getStringExtra(CameraHelper.staticPath);
            new QiNiuHelper(mActivity).uploadSinglePic(new QiNiuHelper.QiNiuCallBack() {
                @Override
                public void onSuccess(String key) {
                    updateUserPhoto(key);
                }


                @Override
                public void onFal(String info) {
                    disMissLoading();
                    ToastUtil.show(mActivity, info);
                }
            }, path);

        }
    }

    /**
     * 更新用户头像
     *
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<>();
        map.put("photo", key);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getRequestJsonString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess() && mGetUserInfoPresenter != null) {

                    mGetUserInfoPresenter.getUserInfoRequest();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 显示确认取消dialog
     *
     * @param str
     * @param onPositiveListener
     */
    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        if (commonDialog == null) {
            commonDialog = new CommonDialog(mActivity).builder()
                    .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(str)
                    .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                    .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);
        }

        commonDialog.show();
    }


    /**
     * 更新昵称
     *
     * @param nickNameUpdate
     */
    @Subscribe
    public void nickNameUpdate(NickNameUpdate nickNameUpdate) {
        if (mBinding != null) {
            mBinding.tvNickName.setText(SPUtilHelper.getUserName());
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public void onStartGetUserInfo() {

    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        setShowData(userInfo);
    }


    @Override
    public void notifyResult(IdResult idResult) {
        LogUtil.E(idResult.result_code+"");
        LogUtil.E(idResult.front_image+"");
        LogUtil.E(idResult.back_image+"");
        LogUtil.E(idResult.face_image+"");

        if (idResult.result_code == 0){

            List<Bitmap> dataList = new ArrayList<>();
            dataList.add(idResult.front_image);
            dataList.add(idResult.back_image);
            dataList.add(idResult.face_image);

            if (qiNiuHelper == null) {
                qiNiuHelper = new QiNiuHelper(mActivity);
            }

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
                    disMissLoading();
                    ToastUtil.show(mActivity, info);
                }

                @Override
                public void onError(String info) {
                    disMissLoading();
                    ToastUtil.show(mActivity, info);
                }
            });

        }else {

        }
    }

    /**
     * 提交请求
     */
    private void submitRequest() {
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("frontImage", frontImage);
        map.put("backImage", backImage);
        map.put("faceImage", faceImage);

        Call<BaseResponseModel<String>> call = RetrofitUtils.getBaseAPiService().stringRequest("805197", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<String>(mActivity) {
            @Override
            protected void onSuccess(String data, String SucMessage) {

                mGetUserInfoPresenter.getUserInfoRequest();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void getTotalIncome(){
        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<BjbMyIncome>> call = RetrofitUtils.createApi(MyApi.class).getMyIncome("625800", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<BjbMyIncome>(mActivity) {
            @Override
            protected void onSuccess(BjbMyIncome data, String SucMessage) {
                mBinding.tvIncome.setText(AmountUtil.transformFormatToString2(data.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
