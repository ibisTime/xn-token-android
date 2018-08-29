package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CountryCodeMode;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


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

        if (item == null) return;

        helper.setText(R.id.tv_country, getSelectCountryName(helper.getLayoutPosition()) + "     " + StringUtils.transformShowCountryCode(item.getInterCode()));

        ImgUtils.loadImage(mContext, item.getPic(), helper.getView(R.id.img_country));

        if (TextUtils.equals(item.getInterCode(), SPUtilHelper.getCountryInterCode())) {  //显示选择图标
            helper.setVisible(R.id.img_choose, true);
        } else {
            helper.setVisible(R.id.img_choose, false);
        }
    }

    public String getSelectCountryName(int postion) {
        CountryCodeMode countryCodeMode = getItem(postion);
        if (countryCodeMode == null) return "";
        return countryCodeMode.getInterName();
    }

    public String getSelectPic(int postion) {
        CountryCodeMode countryCodeMode = getItem(postion);
        if (countryCodeMode == null) return "";
        return countryCodeMode.getPic();
    }

    public String getSelectInterCode(int postion) {
        CountryCodeMode countryCodeMode = getItem(postion);
        if (countryCodeMode == null) return "";
        return countryCodeMode.getInterCode();
    }

    public String getSelectCountryCode(int postion) {
        CountryCodeMode countryCodeMode = getItem(postion);
        if (countryCodeMode == null) return "";
        return countryCodeMode.getCode();
    }


}


