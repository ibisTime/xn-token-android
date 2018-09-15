package com.cdkj.token.find.product_application.red_package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.RedPacketGetAdapter;
import com.cdkj.token.adapter.RedPacketSendAdapter;
import com.cdkj.token.databinding.ActivityRedpacketHistoryBinding;
import com.cdkj.token.find.product_application.question.ApplicationQuestionListActivity;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.MyGetRedPackageBean;
import com.cdkj.token.model.MySendRedPackageBean;
import com.cdkj.token.model.PickerViewModel;
import com.cdkj.token.wallet.WalletFragment;

import java.util.ArrayList;
import java.util.List;

import static com.cdkj.token.find.product_application.red_package.RedPacketHistoryPresenter.TYPE_SEND;

/**
 * 发出的红包历史
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketSendHistoryActivity extends AbsLoadActivity implements RedPacketHistoryView, UserInfoInterface {


    private UserInfoPresenter userInfoPresenter;

    private RedPacketHistoryPresenter redPacketHistoryPresenter;
    private OptionsPickerView yearPickerView;
    private ActivityRedpacketHistoryBinding binding;

    private RedPacketSendAdapter redPacketSendAdapter;
    private RedPacketGetAdapter redPacketGetAdapter;

    private RefreshHelper mRefreshHelper;

    private List<PickerViewModel> filterYears;
    private String selectYear;
    private String showTotla;

    private OptionsPickerView menuPickerView;
    private List<PickerViewModel> pickerViewModels;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPacketSendHistoryActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_redpacket_history, null, false);
        return binding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        showMenuPickView();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(R.string.redpacket_history);

        mBaseBinding.titleView.setRightImg(R.drawable.topbar_more);

        userInfoPresenter = new UserInfoPresenter(this, this);
        userInfoPresenter.getUserInfoRequest();

        redPacketHistoryPresenter = new RedPacketHistoryPresenter();
        redPacketHistoryPresenter.attachView(this);
        redPacketHistoryPresenter.setRedPacketType(TYPE_SEND);
        redPacketHistoryPresenter.getFilterDates();

        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return binding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {

                binding.recyclerFocus.setNestedScrollingEnabled(false);

                return binding.recyclerFocus;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return null;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                redPacketHistoryPresenter.getHistoryData(pageindex + "", limit + "", selectYear, isShowDialog);
            }
        });

        mRefreshHelper.init(RefreshHelper.LIMITE);

        initClickListener();

        if (SPUtilHelper.isAssetsShow()) {
            binding.imgEye.setImageResource(R.drawable.eye_open);
        } else {
            binding.imgEye.setImageResource(R.drawable.eye_close);
        }
        pickerViewModels = new ArrayList<>();
    }

    private void initClickListener() {

        binding.linLayoutYear.setOnClickListener(view -> {
            showYearPickView();
        });

        binding.fraEye.setOnClickListener(view -> {

            boolean isAssetsShow = !SPUtilHelper.isAssetsShow();

            String sendTotal = showTotla;

            if (!isAssetsShow) {
                sendTotal = WalletFragment.HIND_SIGN;
            }

            binding.tvTotal.setText(Html.fromHtml(getString(R.string.redpacket_send_total, sendTotal)));


            if (redPacketSendAdapter != null) {
                redPacketSendAdapter.notifyDataSetChanged();
            }
            if (redPacketGetAdapter != null) {
                redPacketGetAdapter.notifyDataSetChanged();
            }

            SPUtilHelper.saveIsAssetsShow(isAssetsShow);

            if (isAssetsShow) {
                binding.imgEye.setImageResource(R.drawable.eye_open);
            } else {
                binding.imgEye.setImageResource(R.drawable.eye_close);
            }

        });
    }


    /**
     * 显示选择pickView
     */
    private void showMenuPickView() {

        if (menuPickerView == null) {
            //条件选择器
            menuPickerView = new OptionsPickerBuilder(RedPacketSendHistoryActivity.this, new OnOptionsSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    menuPickerView.dismiss();

                    switch (options1) {
                        case 0:
                            redPacketHistoryPresenter.toggleRedPacketType();
                            redPacketHistoryPresenter.getHistoryData(selectYear, true);
                            break;
                        case 1:

                            break;
                    }

                }
            })
                    .setSubmitColor(ContextCompat.getColor(this, R.color.text_black_cd))
                    .setCancelColor(ContextCompat.getColor(this, R.color.gray_999999))
                    .setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.confirm))//确认按钮文字
                    .build();

        }

        pickerViewModels.clear();
        if (redPacketHistoryPresenter.isSendStatus()) {
            pickerViewModels.add(new PickerViewModel(getString(R.string.my_send)));
        } else {
            pickerViewModels.add(new PickerViewModel(getString(R.string.my_get)));
        }
//        pickerViewModels.add(new PickerViewModel(getString(R.string.share)));
        menuPickerView.setPicker(pickerViewModels);
        menuPickerView.show();
    }

    @Override
    public void setSendTotal(String sendTotal) {
        showTotla = sendTotal;
        if (!SPUtilHelper.isAssetsShow()) {
            sendTotal = WalletFragment.HIND_SIGN;
        }
        binding.tvTotal.setText(Html.fromHtml(getString(R.string.redpacket_send_total, sendTotal)));
    }

    @Override
    public void setGetTotal(String getTotal) {
        showTotla = getTotal;
        if (!SPUtilHelper.isAssetsShow()) {
            getTotal = WalletFragment.HIND_SIGN;
        }
        binding.tvTotal.setText(Html.fromHtml(getString(R.string.redpacket_get_total, getTotal)));
    }

    @Override
    public void setFilterYears(List<String> years) {
        if (years == null || years.size() == 0) {
            return;
        }

        redPacketHistoryPresenter.getHistoryData(years.get(0), true);//获取到年份后加载第一年数据

        filterYears = new ArrayList<>();
        for (String year : years) {
            filterYears.add(new PickerViewModel(year + getString(R.string.year)));
        }

        binding.tvYear.setText(years.get(0) + getString(R.string.year));

        binding.linLayoutYear.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSendRedPacketHistoryData(ResponseInListModel<MySendRedPackageBean.ListBean> sendRedPacketHistoryData) {
        redPacketGetAdapter = null;
        if (redPacketSendAdapter == null) {
            redPacketSendAdapter = new RedPacketSendAdapter(sendRedPacketHistoryData.getList());
            mRefreshHelper.setPageIndex(1);
            mRefreshHelper.reSetAdapter(redPacketSendAdapter);
            redPacketSendAdapter.setOnItemClickListener((adapter, view, position) -> {
                RedPacketDetailsActivity.open(this, redPacketSendAdapter.getItem(position).getCode());
            });
        }

        mRefreshHelper.setData2(sendRedPacketHistoryData.getList(), getString(R.string.red_package_send_empty), 0);
    }

    @Override
    public void setGetRedPacketListData(ResponseInListModel<MyGetRedPackageBean> getRedPacketListData) {
        redPacketSendAdapter = null;
        if (redPacketGetAdapter == null) {
            redPacketGetAdapter = new RedPacketGetAdapter(getRedPacketListData.getList());
            mRefreshHelper.setPageIndex(1);
            mRefreshHelper.reSetAdapter(redPacketGetAdapter);
            redPacketGetAdapter.setOnItemClickListener((adapter, view, position) -> {
                RedPacketDetailsActivity.open(this, redPacketGetAdapter.getItem(position).getRedPacketCode());
            });
        }

        mRefreshHelper.setData2(getRedPacketListData.getList(), getString(R.string.red_package_get_empty), 0);
    }

    @Override
    public void shlowLoadDialog() {
        showLoadingDialog();
    }

    @Override
    public void disMissLoadDialog() {
        disMissLoadingDialog();
    }

    @Override
    public void showInfoDialog(String msg) {
        UITipDialog.showInfo(this, msg);
    }

    @Override
    public void onStartGetUserInfo() {
        showLoadingDialog();
    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        disMissLoadingDialog();
        if (userInfo == null) return;

        ImgUtils.loadLogo(this, userInfo.getPhoto(), binding.imgUserLogo);
        binding.tvUserName.setText(userInfo.getNickname());

    }


    /**
     * 显示选择pickView
     */
    private void showYearPickView() {

        if (yearPickerView == null) {
            //条件选择器
            yearPickerView = new OptionsPickerBuilder(RedPacketSendHistoryActivity.this, new OnOptionsSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    yearPickerView.dismiss();

                    selectYear = filterYears.get(options1).getItemString();

                    selectYear = StringUtils.subStringEnd(selectYear, 0);

                    redPacketHistoryPresenter.getHistoryData(selectYear, true);

                }
            })
                    .setSubmitColor(ContextCompat.getColor(this, R.color.text_black_cd))
                    .setCancelColor(ContextCompat.getColor(this, R.color.gray_999999))
                    .setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.confirm))//确认按钮文字
                    .build();

        }

        if (filterYears != null) {
            yearPickerView.setPicker(filterYears);
            yearPickerView.show();
        }

    }


    @Override
    protected void onDestroy() {
        if (redPacketHistoryPresenter != null) {
            redPacketHistoryPresenter.detachView();
        }
        if (userInfoPresenter != null) {
            userInfoPresenter.clear();
        }
        if (yearPickerView != null) {
            yearPickerView.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onError(String msg, String code) {

    }
}
