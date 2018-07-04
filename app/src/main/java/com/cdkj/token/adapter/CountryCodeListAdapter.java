package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.token.R;
import com.cdkj.token.model.CountryCodeMode;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.base.BaseActivity.ENGLISH;
import static com.cdkj.baselibrary.utils.StringUtils.transformShowCountryCode;

/**
 * 国家编号列表
 * Created by lei on 2018/3/7.
 */

public class CountryCodeListAdapter extends BaseQuickAdapter<CountryCodeMode, BaseViewHolder> {


    public CountryCodeListAdapter(@Nullable List<CountryCodeMode> data) {
        super(R.layout.item_country_code, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CountryCodeMode item) {
        helper.setText(R.id.tv_country, item.getInterName() + " " + item.getChineseName() + "     " + transformShowCountryCode(item.getInterCode()));
    }

    public String getSelectCountryName(int postion) {
        if (TextUtils.equals(SPUtilHelper.getLanguage(), ENGLISH)) {
            return mData.get(postion).getInterName();
        } else {
            return mData.get(postion).getChineseName();
        }
    }

    public String getSelectCountryCode(int postion) {
        return mData.get(postion).getInterCode();
    }


}


