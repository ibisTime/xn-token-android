package com.cdkj.token.model;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * Created by cdkj on 2018/8/4.
 */

public class ClientPickerModel implements Serializable, IPickerViewData {

    private String name;

    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
