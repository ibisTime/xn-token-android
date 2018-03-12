package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.StatisticsAdapter;
import com.cdkj.token.model.StatisticsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by lei on 2018/3/7.
 */

public class StatisticsActivity extends BaseRefreshActivity<StatisticsModel> {

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, StatisticsActivity.class);
        activity.startActivity(intent);

    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getString(R.string.consult_statistics_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex,limit,true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<StatisticsModel> mDataList) {
        return new StatisticsAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.consult_statistics_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
