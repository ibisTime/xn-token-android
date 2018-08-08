package com.cdkj.token.user;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.activitys.NickModifyActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.NickNameUpdate;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentUser2Binding;
import com.cdkj.token.user.question_feedback.QuestionFeedbackSubmitActivity;
import com.cdkj.token.user.setting.LocalCoinTypeChooseActivity;
import com.cdkj.token.user.setting.UserLanguageActivity;
import com.cdkj.token.user.setting.UserSettingActivity;
import com.cdkj.token.utils.ThaAppConstant;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.create_guide.CreateWalletStartActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2018/6/28.
 */

public class UserFragment extends BaseLazyFragment {

    private FragmentUser2Binding mBinding;

    public final int PHOTOFLAG = 110;

    private CommonDialog commonDialog;

    public static UserFragment getInstance() {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user2, null, false);

        initClickListener();

        return mBinding.getRoot();
    }


    private void initClickListener() {

        //问题反馈
        mBinding.linLayoutFeedback.setOnClickListener(view -> {
            QuestionFeedbackSubmitActivity.open(mActivity);
        });

        //修改昵称
        mBinding.linLayoutNickName.setOnClickListener(view -> {
            NickModifyActivity.open(mActivity, SPUtilHelper.getUserName());
        });

        //本地货币
        mBinding.linLayoutLocalCoin.setOnClickListener(view -> {
            LocalCoinTypeChooseActivity.open(mActivity);
        });

        //更换头像
        mBinding.imgLogo.setOnClickListener(view -> {
            ImageSelectActivity.launchFragment(this, PHOTOFLAG);
        });
        //账户与安全
        mBinding.linLayoutUserAccount.setOnClickListener(view -> UserSecurityActivity.open(mActivity));

        //语言
//        mBinding.languageChange.setOnClickListener(view -> UserLanguageActivity.open(mActivity));

        //加入社群
        mBinding.joinUs.setOnClickListener(view -> UserJoinActivity.open(mActivity));
        //钱包工具
        mBinding.walletTool.setOnClickListener(view -> {
            boolean isHasInfo = WalletHelper.isUserAddedWallet(SPUtilHelper.getUserId());
            if (!isHasInfo) {
                CreateWalletStartActivity.open(mActivity);
//                showDoubleWarnListen(getString(R.string.please_create_or_import_wallet), view1 -> {
//                    CreateWalletStartActivity.open(mActivity);
//                });
                return;
            }
            WalletToolActivity.open(mActivity);
        });

        //帮助中心
        mBinding.helper.setOnClickListener(view -> {
            WebViewImgBgActivity.openkey(mActivity, getStrRes(R.string.user_issue), ThaAppConstant.getH5UrlLangage(ThaAppConstant.QUESTIONS));
        });

        //设置
        mBinding.setting.setOnClickListener(view -> UserSettingActivity.open(mActivity));


    }

    @Override
    protected void lazyLoad() {
        if (mBinding != null) {
            getUserInfoRequest();
        }
    }

    @Override
    protected void onInvisible() {

    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {
            setShowData(null);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(mActivity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                if (data == null)
                    return;

                SPUtilHelper.saveSecretUserId(data.getSecretUserId());
                SPUtilHelper.saveUserPhoto(data.getPhoto());
                SPUtilHelper.saveUserEmail(data.getEmail());
                SPUtilHelper.saveUserName(data.getNickname());
                SPUtilHelper.saveRealName(data.getRealName());
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveLoginPwdFlag(data.isLoginPwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());

                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    private void setShowData(UserInfoModel data) {
        if (data == null) {
            mBinding.tvNickName.setText("");
            mBinding.tvPhoneNumber.setText("");
            mBinding.imgLogo.setImageResource(R.drawable.photo_default);
            return;
        }

        if (data.getCreateNo() < 10000) {
            mBinding.imgCrown.setVisibility(View.VISIBLE);
        } else {
            mBinding.imgCrown.setVisibility(View.GONE);
        }

        if (data.getNickname() == null) return;

        mBinding.tvNickName.setText(data.getNickname());
        mBinding.tvPhoneNumber.setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryCode()) + " " + StringUtils.ttransformShowPhone(data.getMobile()));
        ImgUtils.loadLogo(mActivity, data.getPhoto(), mBinding.imgLogo);
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

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getJsonToString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    getUserInfoRequest();
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

    @Override
    public void onDestroy() {
        if (commonDialog != null) {
            commonDialog.closeDialog();
        }
        super.onDestroy();
    }

    @Subscribe
    public void nickNameUpdate(NickNameUpdate nickNameUpdate) {
        if (mBinding != null) {
            mBinding.tvNickName.setText(SPUtilHelper.getUserName());
        }
    }

}
