package com.cdkj.token.model;

import com.cdkj.token.views.ScrollPicker;

/**
 * 账单筛选
 * Created by cdkj on 2018/7/26.
 */
public class BillFilterModel implements ScrollPicker.ScrollPickerData {

    private String itemText;

    private String type;

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getShowString() {
        return itemText;
    }

    @Override
    public String getSelectType() {
        return type;
    }


}
