package com.cdkj.token.model;

import java.math.BigInteger;

/**
 * Created by cdkj on 2018/8/11.
 */

public class UTXOModel {

    private String txid;
    private BigInteger vout;
    private BigInteger count;
    private String scriptPubKey;
    private String address;


    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public BigInteger getVout() {
        return vout;
    }

    public void setVout(BigInteger vout) {
        this.vout = vout;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
