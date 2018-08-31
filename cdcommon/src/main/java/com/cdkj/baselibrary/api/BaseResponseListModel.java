package com.cdkj.baselibrary.api;

import java.util.List;

/**
 * Created by cdkj on 2017/6/8.
 */

public class BaseResponseListModel<T>{

    private String errorCode;//状态码
    private String errorInfo;//状态描述
    private String errorBizCode;//业务错误
    private List<T> data;          //数据

    public String getErrorBizCode() {
        return errorBizCode;
    }

    public void setErrorBizCode(String errorBizCode) {
        this.errorBizCode = errorBizCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
