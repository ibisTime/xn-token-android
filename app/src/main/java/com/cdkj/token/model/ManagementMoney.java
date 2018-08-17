package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/8/16.
 */

public class ManagementMoney {


    /**
     * code : CP545435353342
     * name : 币币赢第一期
     * symbol : BTC
     * description : 产品详情描述
     * expectYield : 0.07
     * actualYield : 0.08
     * limitDays : 360
     * minAmount : 100
     * increAmount : 10
     * limitAmount : 500
     * amount : 1000
     * avilAmount : 100
     * saleAmount : 100
     * successAmount : 1000
     * saleNum : 4
     * status : 0
     * createDatetime : 2018-08-09 00:00:00
     * startDatetime : 2018-08-09 00:00:00
     * endDatetime : 2018-08-09 00:00:00
     * incomeDatetime : 2018-08-09 00:00:00
     * arriveDatetime : 2018-08-09 00:00:00
     * repayDatetime : 2018-08-09 00:00:00
     * paymentType : 0
     * approver : admin
     * approveDatetime : 2018-08-09 00:00:00
     * approveNote : 通过
     * updater : admin
     * updateDatetime : 2018-08-09 00:00:00
     * remark : 无
     */

    private String code;
    private String name;
    private String symbol;
    private String description;
    private float expectYield;
    private float actualYield;
    private int limitDays;
    private BigDecimal minAmount;
    private BigDecimal increAmount;
    private BigDecimal limitAmount;
    private BigDecimal amount;
    private BigDecimal avilAmount;
    private BigDecimal saleAmount;
    private BigDecimal successAmount;
    private String saleNum;
    private String status;
    private String createDatetime;
    private String startDatetime;
    private String endDatetime;
    private String incomeDatetime;
    private String arriveDatetime;
    private String repayDatetime;
    private String paymentType;
    private String approver;
    private String approveDatetime;
    private String approveNote;
    private String updater;
    private String updateDatetime;
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getExpectYield() {
        return expectYield;
    }

    public void setExpectYield(float expectYield) {
        this.expectYield = expectYield;
    }

    public float getActualYield() {
        return actualYield;
    }

    public void setActualYield(float actualYield) {
        this.actualYield = actualYield;
    }

    public int getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(int limitDays) {
        this.limitDays = limitDays;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getIncreAmount() {
        return increAmount;
    }

    public void setIncreAmount(BigDecimal increAmount) {
        this.increAmount = increAmount;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getAvilAmount() {
        return avilAmount;
    }

    public void setAvilAmount(BigDecimal avilAmount) {
        this.avilAmount = avilAmount;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getIncomeDatetime() {
        return incomeDatetime;
    }

    public void setIncomeDatetime(String incomeDatetime) {
        this.incomeDatetime = incomeDatetime;
    }

    public String getArriveDatetime() {
        return arriveDatetime;
    }

    public void setArriveDatetime(String arriveDatetime) {
        this.arriveDatetime = arriveDatetime;
    }

    public String getRepayDatetime() {
        return repayDatetime;
    }

    public void setRepayDatetime(String repayDatetime) {
        this.repayDatetime = repayDatetime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
