package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by cdkj on 2018/6/10.
 */

public class BalanceListModel {


    /**
     * totalAmountCNY : 0.00
     * totalAmountUSD : 0.00
     * totalAmountHKD : 0.00
     * accountList : [{"symbol":"ETH","address":"0x349B45A0249744B0De8E9Cb140a1D152F587cd5A","balance":0,"amountCNY":"0.00","amountUSD":"0.00","amountHKD":"0.00","priceCNY":"3861.77","priceUSD":"603.37","priceHKD":"4733.73"},{"symbol":"ETH","address":"0x349B45A0249744B0De8E9Cb140a1D152F587cd5A","balance":0,"amountCNY":"0.00","amountUSD":"0.00","amountHKD":"0.00","priceCNY":"3861.77","priceUSD":"603.37","priceHKD":"4733.73"}]
     */

    private String totalAmountCNY;
    private String totalAmountUSD;
    private String totalAmountHKD;
    private List<AccountListBean> accountList;

    public String getTotalAmountCNY() {
        return totalAmountCNY;
    }

    public void setTotalAmountCNY(String totalAmountCNY) {
        this.totalAmountCNY = totalAmountCNY;
    }

    public String getTotalAmountUSD() {
        return totalAmountUSD;
    }

    public void setTotalAmountUSD(String totalAmountUSD) {
        this.totalAmountUSD = totalAmountUSD;
    }

    public String getTotalAmountHKD() {
        return totalAmountHKD;
    }

    public void setTotalAmountHKD(String totalAmountHKD) {
        this.totalAmountHKD = totalAmountHKD;
    }

    public List<AccountListBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountListBean> accountList) {
        this.accountList = accountList;
    }

    public static class AccountListBean implements Parcelable {

        /**
         * symbol : ETH
         * address : 0x349B45A0249744B0De8E9Cb140a1D152F587cd5A
         * balance : 0
         * amountCNY : 0.00
         * amountUSD : 0.00
         * amountHKD : 0.00
         * priceCNY : 3861.77
         * priceUSD : 603.37
         * priceHKD : 4733.73
         */

        private String symbol;
        private String address;
        private BigInteger balance;
        private String amountCNY;
        private String amountUSD;
        private String amountHKD;
        private String priceCNY;
        private String priceUSD;
        private String priceHKD;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public BigInteger getBalance() {
            return balance;
        }

        public void setBalance(BigInteger balance) {
            this.balance = balance;
        }

        public String getAmountCNY() {
            return amountCNY;
        }

        public void setAmountCNY(String amountCNY) {
            this.amountCNY = amountCNY;
        }

        public String getAmountUSD() {
            return amountUSD;
        }

        public void setAmountUSD(String amountUSD) {
            this.amountUSD = amountUSD;
        }

        public String getAmountHKD() {
            return amountHKD;
        }

        public void setAmountHKD(String amountHKD) {
            this.amountHKD = amountHKD;
        }

        public String getPriceCNY() {
            return priceCNY;
        }

        public void setPriceCNY(String priceCNY) {
            this.priceCNY = priceCNY;
        }

        public String getPriceUSD() {
            return priceUSD;
        }

        public void setPriceUSD(String priceUSD) {
            this.priceUSD = priceUSD;
        }

        public String getPriceHKD() {
            return priceHKD;
        }

        public void setPriceHKD(String priceHKD) {
            this.priceHKD = priceHKD;
        }

        public AccountListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.symbol);
            dest.writeString(this.address);
            dest.writeSerializable(this.balance);
            dest.writeString(this.amountCNY);
            dest.writeString(this.amountUSD);
            dest.writeString(this.amountHKD);
            dest.writeString(this.priceCNY);
            dest.writeString(this.priceUSD);
            dest.writeString(this.priceHKD);
        }

        protected AccountListBean(Parcel in) {
            this.symbol = in.readString();
            this.address = in.readString();
            this.balance = (BigInteger) in.readSerializable();
            this.amountCNY = in.readString();
            this.amountUSD = in.readString();
            this.amountHKD = in.readString();
            this.priceCNY = in.readString();
            this.priceUSD = in.readString();
            this.priceHKD = in.readString();
        }

        public static final Creator<AccountListBean> CREATOR = new Creator<AccountListBean>() {
            @Override
            public AccountListBean createFromParcel(Parcel source) {
                return new AccountListBean(source);
            }

            @Override
            public AccountListBean[] newArray(int size) {
                return new AccountListBean[size];
            }
        };
    }
}
