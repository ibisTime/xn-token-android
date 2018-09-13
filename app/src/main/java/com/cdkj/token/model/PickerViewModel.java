package com.cdkj.token.model;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by cdkj on 2018/9/13.
 */

public class PickerViewModel implements IPickerViewData {

    private String itemString;

    public PickerViewModel(String itemString) {
        this.itemString = itemString;
    }

    public String getItemString() {
        return itemString;
    }

    public void setItemString(String itemString) {
        this.itemString = itemString;
    }

    @Override
    public String getPickerViewText() {
        return itemString;
    }
}
