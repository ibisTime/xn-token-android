package com.cdkj.token.model;

import java.math.BigDecimal;

/**空投信息
 * Created by cdkj on 2018/3/14.
 */

public class KtInfoModel {


    /**
     * address : 0x8E979a7621314e0BA506c54A2482F7F49a99Ef44
     * balance : 9999980000000000
     * initialBalance : 10000000000000000
     * useBalance : 20000000000
     * useRate : 0.000002
     */

    private String address;
    private String balance;
    private BigDecimal initialBalance;
    private BigDecimal useBalance;
    private BigDecimal useRate;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getUseBalance() {
        return useBalance;
    }

    public void setUseBalance(BigDecimal useBalance) {
        this.useBalance = useBalance;
    }

    public BigDecimal getUseRate() {
        return useRate;
    }

    public void setUseRate(BigDecimal useRate) {
        this.useRate = useRate;
    }
}
