package com.cdkj.token.utils.wan;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">
 * yellow paper</a>.
 */
public class WanRawTransaction {

    private BigInteger Txtype;

    private BigInteger nonce;

    private BigInteger gasPrice;

    private BigInteger gasLimit;

    private String to;

    private BigInteger value;

    private String data;

    protected WanRawTransaction(BigInteger nonce, BigInteger gasPrice,
            BigInteger gasLimit, String to, BigInteger value, String data) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        this.Txtype = BigInteger.ONE;

        if (data != null) {
            this.data = Numeric.cleanHexPrefix(data);
        }
    }

    public static WanRawTransaction createContractTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, BigInteger value,
            String init) {

        return new WanRawTransaction(nonce, gasPrice, gasLimit, "", value, init);
    }

    public static WanRawTransaction createEtherTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value) {

        return new WanRawTransaction(nonce, gasPrice, gasLimit, to, value, "");

    }

    public static WanRawTransaction createTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, String to, String data) {
        return createTransaction(nonce, gasPrice, gasLimit, to, BigInteger.ZERO,
            data);
    }

    public static WanRawTransaction createTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String data) {

        return new WanRawTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public BigInteger getTxtype() {
        return Txtype;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getData() {
        return data;
    }
}
