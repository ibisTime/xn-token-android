package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyNickBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.NickNameUpdate;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.StringUtils.stringFilter;

/**
 * Created by lei on 2017/8/22.
 */

public class NickModifyActivity extends AbsActivity {

    private ActivityModifyNickBinding mBinding;

    private String nick;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String nick) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, NickModifyActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, nick);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_nick, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_nick_title));
        setSubLeftImgState(true);

        if (getIntent() != null) {
            nick = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
            mBinding.edtNickname.setHint(nick);
        }

        initListener();
    }

    private void initListener() {
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtNickname.getText().toString())) {
                    UITipDialog.showInfoNoIcon(NickModifyActivity.this, getString(R.string.activity_nick_name_hint));
                    return;
                }
                modifyNick();
            }
        });

        mBinding.edtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mBinding.edtNickname.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    mBinding.edtNickname.setText(str);
                    //设置新的光标所在位置
                    mBinding.edtNickname.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * 修改昵称
     */
    public void modifyNick() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("nickname", mBinding.edtNickname.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("companyCode", AppConfig.COMPANYCODE);
        map.put("token", SPUtilHelper.getUserToken());


        Call call = RetrofitUtils.getBaseAPiService().successRequest("805075", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    UITipDialog.showSuccess(NickModifyActivity.this, getString(R.string.activity_nick_success), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            SPUtilHelper.saveUserName(mBinding.edtNickname.getText().toString());
                            EventBus.getDefault().post(new NickNameUpdate());
                            finish();
                        }
                    });
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }
}
