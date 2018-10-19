package com.cdkj.token.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/8/16.
 */

public class ManagementMoney implements Serializable {


    /**
     * code : PD201810062207231658623
     * nameZhCn : mc3
     * nameEn : mc3
     * nameKo : mc3
     * symbol : ETH
     * buyDescZhCn : <p>mc3</p><p><br></p>
     * buyDescEn : <p>mc3</p><p><br></p>
     * buyDescKo : <p>mc3</p><p><br></p>
     * redeemDescZhCn : <p>mc3</p><p><br></p>
     * redeemDescEn : <p>mc3</p><p><br></p>
     * redeemDescKo : <p>mc3</p><p><br></p>
     * directionsZhCn : <p>mc3</p><p><br></p>
     * directionsEn : <p>mc3</p><p><br></p>
     * directionsKo : <p>mc3</p><p><br></p>
     * expectYield : 5.0E-4
     * actualYield : 5.0E-4
     * limitDays : 1
     * minAmount : 10000000000000000000
     * increAmount : 10000000000000000000
     * limitAmount : 90000000000000000000
     * amount : 100000000000000000000
     * avilAmount : 10000000000000000000
     * saleAmount : 90000000000000000000
     * successAmount : 50000000000000000000
     * saleNum : 1
     * status : 7
     * creator : admin
     * createDatetime : Oct 6, 2018 10:07:23 PM
     * startDatetime : Oct 6, 2018 10:06:19 PM
     * endDatetime : Oct 6, 2018 10:08:19 PM
     * incomeDatetime : Oct 6, 2018 10:10:19 PM
     * arriveDatetime : Oct 6, 2018 10:10:19 PM
     * repayDatetime : Oct 7, 2018 12:00:00 AM
     * paymentType : 0
     * approver : admin
     * approveDatetime : Oct 6, 2018 10:07:32 PM
     * updater : admin
     * updateDatetime : Oct 6, 2018 10:07:40 PM
     * remark : 平台上架
     * limitFen : 9
     * totalFen : 10
     * name : mc3
     * buyDesc : <p>mc3</p><p><br></p>
     * redeemDesc : <p>mc3</p><p><br></p>
     * directions : <p>mc3</p><p><br></p>
     * incomeFlag : true
     */

    private String code;
    private String nameZhCn;
    private String nameEn;
    private String nameKo;
    private String symbol;
    private String buyDescZhCn;
    private String buyDescEn;
    private String buyDescKo;
    private String redeemDescZhCn;
    private String redeemDescEn;
    private String redeemDescKo;
    private String directionsZhCn;
    private String directionsEn;
    private String directionsKo;
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
    private int saleNum;
    private String status;
    private String creator;
    private String createDatetime;
    private String startDatetime;
    private String endDatetime;
    private String incomeDatetime;
    private String arriveDatetime;
    private String repayDatetime;
    private String paymentType;
    private String approver;
    private String approveDatetime;
    private String updater;
    private String updateDatetime;
    private String remark;
    private int limitFen;
    private int totalFen;
    private String name;
    private String buyDesc;
    private String redeemDesc;
    private String directions;
    private boolean incomeFlag;

    //时间状态
    private String timeStatus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameZhCn() {
        return nameZhCn;
    }

    public void setNameZhCn(String nameZhCn) {
        this.nameZhCn = nameZhCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameKo() {
        return nameKo;
    }

    public void setNameKo(String nameKo) {
        this.nameKo = nameKo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBuyDescZhCn() {
        return buyDescZhCn;
    }

    public void setBuyDescZhCn(String buyDescZhCn) {
        this.buyDescZhCn = buyDescZhCn;
    }

    public String getBuyDescEn() {
        return buyDescEn;
    }

    public void setBuyDescEn(String buyDescEn) {
        this.buyDescEn = buyDescEn;
    }

    public String getBuyDescKo() {
        return buyDescKo;
    }

    public void setBuyDescKo(String buyDescKo) {
        this.buyDescKo = buyDescKo;
    }

    public String getRedeemDescZhCn() {
        return redeemDescZhCn;
    }

    public void setRedeemDescZhCn(String redeemDescZhCn) {
        this.redeemDescZhCn = redeemDescZhCn;
    }

    public String getRedeemDescEn() {
        return redeemDescEn;
    }

    public void setRedeemDescEn(String redeemDescEn) {
        this.redeemDescEn = redeemDescEn;
    }

    public String getRedeemDescKo() {
        return redeemDescKo;
    }

    public void setRedeemDescKo(String redeemDescKo) {
        this.redeemDescKo = redeemDescKo;
    }

    public String getDirectionsZhCn() {
        return directionsZhCn;
    }

    public void setDirectionsZhCn(String directionsZhCn) {
        this.directionsZhCn = directionsZhCn;
    }

    public String getDirectionsEn() {
        return directionsEn;
    }

    public void setDirectionsEn(String directionsEn) {
        this.directionsEn = directionsEn;
    }

    public String getDirectionsKo() {
        return directionsKo;
    }

    public void setDirectionsKo(String directionsKo) {
        this.directionsKo = directionsKo;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAvilAmount() {
        return avilAmount;
    }

    public void setAvilAmount(BigDecimal avilAmount) {
        this.avilAmount = avilAmount;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public int getLimitFen() {
        return limitFen;
    }

    public void setLimitFen(int limitFen) {
        this.limitFen = limitFen;
    }

    public int getTotalFen() {
        return totalFen;
    }

    public void setTotalFen(int totalFen) {
        this.totalFen = totalFen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyDesc() {
        return buyDesc;
    }

    public void setBuyDesc(String buyDesc) {
        this.buyDesc = buyDesc;
    }

    public String getRedeemDesc() {
        return redeemDesc;
    }

    public void setRedeemDesc(String redeemDesc) {
        this.redeemDesc = redeemDesc;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public boolean isIncomeFlag() {
        return incomeFlag;
    }

    public void setIncomeFlag(boolean incomeFlag) {
        this.incomeFlag = incomeFlag;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }
}
