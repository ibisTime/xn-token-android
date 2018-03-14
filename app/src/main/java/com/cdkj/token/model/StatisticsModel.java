package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * Created by lei on 2018/3/7.
 */

public class StatisticsModel {


    /**
     * id : 1
     * hash : 0x37284535fc866ad808eafb0daa3ae70e774ce5c44521b30595f384d0eb125d4c
     * nonce : 100
     * blockHash : 0x361c5e03813f927be2ba1f355e608626065db07f09ca2593af4463e03d01c850
     * blockNumber : 52960
     * transactionIndex : 9
     * from : 0x8e979a7621314e0ba506c54a2482f7f49a99ef44
     * to : 0xa1b7f66d2c5cd89a848c75ccda085117825a0af9
     * value : 0
     * tokenFrom : 0x8e979a7621314e0ba506c54a2482f7f49a99ef44
     * tokenTo : 0xe6b167f1fbdf6363e509908e8594affa16410902
     * tokenValue : 1000000000
     * gasPrice : 18000000000
     * gasUsed : 39093
     * gas : 210000
     * input : 0xa9059cbb000000000000000000000000e6b167f1fbdf6363e509908e8594affa16410902000000000000000000000000000000000000000000000000000000003b9aca00
     * creates : 2018-03-12 17:00:16
     * refNo : CZ201803121701025452746
     * remark : 充值交易
     */

    private String id;
    private String hash;
    private String nonce;
    private String blockHash;
    private String blockNumber;
    private String transactionIndex;
    private String from;
    private String to;
    private String value;
    private String tokenFrom;
    private String tokenTo;
    private BigDecimal tokenValue;
    private String gasPrice;
    private String gasUsed;
    private String gas;
    private String input;
    private String creates;
    private String refNo;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTokenFrom() {
        return tokenFrom;
    }

    public void setTokenFrom(String tokenFrom) {
        this.tokenFrom = tokenFrom;
    }

    public String getTokenTo() {
        return tokenTo;
    }

    public void setTokenTo(String tokenTo) {
        this.tokenTo = tokenTo;
    }

    public BigDecimal getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(BigDecimal tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getCreates() {
        return creates;
    }

    public void setCreates(String creates) {
        this.creates = creates;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
