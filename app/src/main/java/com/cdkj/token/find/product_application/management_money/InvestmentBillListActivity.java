package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.InvestmentBillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.InvestBillModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 投资账单
 * Created by cdkj on 2018/9/27.
 */

public class InvestmentBillListActivity extends AbsLoadActivity {

    protected LayoutCommonRecyclerRefreshBinding mBinding;

    private RefreshHelper refreshHelper;

    private TimePickerView timePickerView;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InvestmentBillListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_common_recycler_refresh, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        initPickerView();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.investment_bill);
        mBaseBinding.titleView.setRightImg(R.drawable.ic_calendar);

        inRefreshHelper();

        refreshHelper.onDefaluteMRefresh(true);

    }

    void inRefreshHelper() {
        refreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return new InvestmentBillListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        refreshHelper.init(RefreshHelper.LIMITE);
    }


    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<InvestBillModel>>> call = RetrofitUtils.createApi(MyApi.class).getInvestBillList("802525", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<InvestBillModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<InvestBillModel> data, String SucMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    public void initPickerView() {
        if (timePickerView == null) {

            timePickerView = new TimePickerBuilder(this, (date, v) -> {

            }).setSubmitColor(ContextCompat.getColor(this, R.color.text_black_cd))
                    .setCancelColor(ContextCompat.getColor(this, R.color.gray_999999))
                    .setType(new boolean[]{true, true, false, false, false, false})
                    .setLabel(getString(R.string.year), getString(R.string.month), null, "", "", "")
                    .setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.confirm)).build();//确认按钮文字;
        }

        timePickerView.show();
    }

}
