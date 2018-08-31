package com.cdkj.token.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CommunityListAdapter;
import com.cdkj.token.databinding.ActivityJoinUsBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 加入社群
 * Created by cdkj on 2018/5/26.
 */

public class UserJoinActivity extends AbsStatusBarTranslucentActivity {

    private ActivityJoinUsBinding mBindin;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserJoinActivity.class));
    }


    @Override
    public View addContentView() {
        mBindin = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_join_us, null, false);
        return mBindin.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(getStrRes(R.string.user_title_join));
        setPageBgImage(R.drawable.my_bg);
        getCommnityList();
    }


    private void copyText(String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, text);
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(UserJoinActivity.this, getString(R.string.copy_success));
        }
    }

    /**
     * 获取社区列表
     *
     * @return
     */
    private void getCommnityList() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "followUs");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call<BaseResponseListModel<IntroductionInfoModel>> call = RetrofitUtils.getBaseAPiService().getKeySystemInfoList("660919", StringUtils.getJsonToString(map));

        showLoadingDialog();

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(List<IntroductionInfoModel> data, String SucMessage) {
                if (data == null || data.isEmpty()) return;
                mBindin.rv.setLayoutManager(new LinearLayoutManager(UserJoinActivity.this,LinearLayoutManager.VERTICAL,false));
                mBindin.rv.setAdapter(getListAdapter(data));
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 数据适配器
     *
     * @param listData
     * @return
     */
    public RecyclerView.Adapter getListAdapter(List listData) {
        CommunityListAdapter communityListAdapter = new CommunityListAdapter(listData);
        communityListAdapter.setOnItemClickListener((adapter, view, position) -> {
            copyText(communityListAdapter.getItem(position).getCvalue());
        });
        return communityListAdapter;
    }


}
