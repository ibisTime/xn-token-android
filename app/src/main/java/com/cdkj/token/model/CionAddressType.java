package com.cdkj.token.model;

/**
 * @updateDts 2019/3/21
 */
public class CionAddressType {

    /**
     * isSuccess : false
     */

    private boolean isSuccess;  //true 是合约地址  false 是普通地址

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
