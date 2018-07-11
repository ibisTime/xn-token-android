package com.cdkj.baselibrary.model;

/**
 * Created by cdkj on 2018/7/5.
 */

public class CountrySelectEvent {

    private String countryName;
    private String countryCode;
    private String countryFlag;

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
