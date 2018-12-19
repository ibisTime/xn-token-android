package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cdkj.baselibrary.interfaces.AmountShowTypeInterface;
import com.cdkj.baselibrary.interfaces.MarketShowTypeInterface;
import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinModel implements Serializable, AmountShowTypeInterface {


    /**
     * accountList : [{"accountNumber":"A201711212030187096126","userId":"U201711212030187015235","realName":"18984955240","type":"C","status":"0","currency":"ETH","amountString":"0","frozenAmountString":"0","md5":"4122cb13c7a474c1976c9706ae36521d","addAmountString":"0","inAmountString":"0","outAmountString":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","systemCode":"CD-COIN000017","companyCode":"CD-COIN000017","coinAddress":"0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5"}]
     * totalAmountCNY : 0.0000
     * totalAmountUSD : 0.00
     * totalAmountHKD : 0.00
     */

    private String totalAmountCNY;
    private String totalAmountUSD;
    private String totalAmountHKD;
    private String totalAmountKRW;
    private List<AccountListBean> accountList;

    public String getTotalAmountKRW() {
        return totalAmountKRW;
    }

    public void setTotalAmountKRW(String totalAmountKRW) {
        this.totalAmountKRW = totalAmountKRW;
    }

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

    @Override
    public String _getAmountStringUSD() {
        return totalAmountUSD;
    }

    @Override
    public String _getAmountStringCNY() {
        return totalAmountCNY;
    }

    @Override
    public String _getAmountStringKRW() {
        return totalAmountKRW;
    }

    public static class AccountListBean implements Serializable, IPickerViewData, AmountShowTypeInterface, MarketShowTypeInterface, Parcelable {
        /**
         * accountNumber : A201711212030187096126
         * userId : U201711212030187015235
         * realName : 18984955240
         * type : C
         * status : 0
         * currency : ETH
         * amountString : 0
         * frozenAmountString : 0
         * md5 : 4122cb13c7a474c1976c9706ae36521d
         * addAmountString : 0
         * inAmountString : 0
         * outAmountString : 0
         * createDatetime : Nov 21, 2017 8:30:18 PM
         * systemCode : CD-COIN000017
         * companyCode : CD-COIN000017
         * coinAddress : 0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5
         */

        private String accountNumber;
        private String userId;
        private String realName;
        private String type;
        private String status;
        private String currency;
        private String amountString;  //总余额
        private BigDecimal amount;  //总余额
        private String frozenAmountString;//冻结余额
        private BigDecimal frozenAmount;//冻结余额
        private String md5;
        private String addAmountString;
        private String inAmountString;
        private String outAmountString;
        private String createDatetime;
        private String systemCode;
        private String companyCode;
        private String coinAddress;
        private String coinBalance;

        private String amountCNY;
        private String amountUSD;
        private String amountHKD;
        private String amountKRW;
        private String priceCNY;
        private String priceUSD;
        private String priceHKD;
        private String priceKRW;

        private String percentChange24h;

        private String localCoinType;//本地类型
        private boolean isChoose;//本地是否配置选中

        public String getPercentChange24h() {
            return percentChange24h;
        }

        public void setPercentChange24h(String percentChange24h) {
            this.percentChange24h = percentChange24h;
        }

        public String getPriceKRW() {
            return priceKRW;
        }

        public void setPriceKRW(String priceKRW) {
            this.priceKRW = priceKRW;
        }

        public String getAmountKRW() {

            return amountKRW;
        }

        public void setAmountKRW(String amountKRW) {
            this.amountKRW = amountKRW;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(BigDecimal frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public String getLocalCoinType() {
            return localCoinType;
        }

        public void setLocalCoinType(String localCoinType) {
            this.localCoinType = localCoinType;
        }

        public String getAmountCNY() {
            return amountCNY;
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

        public void setAmountCNY(String amountCNY) {
            this.amountCNY = amountCNY;
        }

        public String getCoinBalance() {
            return coinBalance;
        }

        public void setCoinBalance(String coinBalance) {
            this.coinBalance = coinBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAmountString() {
            return amountString;
        }

        public void setAmountString(String amountString) {
            this.amountString = amountString;
        }

        public String getFrozenAmountString() {
            return frozenAmountString;
        }

        public void setFrozenAmountString(String frozenAmountString) {
            this.frozenAmountString = frozenAmountString;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getAddAmountString() {
            return addAmountString;
        }

        public void setAddAmountString(String addAmountString) {
            this.addAmountString = addAmountString;
        }

        public String getInAmountString() {
            return inAmountString;
        }

        public void setInAmountString(String inAmountString) {
            this.inAmountString = inAmountString;
        }

        public String getOutAmountString() {
            return outAmountString;
        }

        public void setOutAmountString(String outAmountString) {
            this.outAmountString = outAmountString;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCoinAddress() {
            return coinAddress;
        }

        public void setCoinAddress(String coinAddress) {
            this.coinAddress = coinAddress;
        }

        @Override
        public String getPickerViewText() {
            return currency;
        }

        @Override
        public String _getAmountStringUSD() {
            return getAmountUSD();
        }

        @Override
        public String _getAmountStringCNY() {
            return getAmountCNY();
        }

        @Override
        public String _getAmountStringKRW() {
            return getAmountCNY();
        }

        @Override
        public String _getMarketStringUSD() {
            return getPriceUSD();
        }

        @Override
        public String _getMarketStringCNY() {
            return getPriceCNY();
        }

        @Override
        public String _getMarketStringKRW() {
            return getPriceKRW();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.accountNumber);
            dest.writeString(this.userId);
            dest.writeString(this.realName);
            dest.writeString(this.type);
            dest.writeString(this.status);
            dest.writeString(this.currency);
            dest.writeString(this.amountString);
            dest.writeSerializable(this.amount);
            dest.writeString(this.frozenAmountString);
            dest.writeSerializable(this.frozenAmount);
            dest.writeString(this.md5);
            dest.writeString(this.addAmountString);
            dest.writeString(this.inAmountString);
            dest.writeString(this.outAmountString);
            dest.writeString(this.createDatetime);
            dest.writeString(this.systemCode);
            dest.writeString(this.companyCode);
            dest.writeString(this.coinAddress);
            dest.writeString(this.coinBalance);
            dest.writeString(this.amountCNY);
            dest.writeString(this.amountUSD);
            dest.writeString(this.amountHKD);
            dest.writeString(this.amountKRW);
            dest.writeString(this.priceCNY);
            dest.writeString(this.priceUSD);
            dest.writeString(this.priceHKD);
            dest.writeString(this.priceKRW);
            dest.writeString(this.percentChange24h);
            dest.writeString(this.localCoinType);
            dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
        }

        public AccountListBean() {
        }

        protected AccountListBean(Parcel in) {
            this.accountNumber = in.readString();
            this.userId = in.readString();
            this.realName = in.readString();
            this.type = in.readString();
            this.status = in.readString();
            this.currency = in.readString();
            this.amountString = in.readString();
            this.amount = (BigDecimal) in.readSerializable();
            this.frozenAmountString = in.readString();
            this.frozenAmount = (BigDecimal) in.readSerializable();
            this.md5 = in.readString();
            this.addAmountString = in.readString();
            this.inAmountString = in.readString();
            this.outAmountString = in.readString();
            this.createDatetime = in.readString();
            this.systemCode = in.readString();
            this.companyCode = in.readString();
            this.coinAddress = in.readString();
            this.coinBalance = in.readString();
            this.amountCNY = in.readString();
            this.amountUSD = in.readString();
            this.amountHKD = in.readString();
            this.amountKRW = in.readString();
            this.priceCNY = in.readString();
            this.priceUSD = in.readString();
            this.priceHKD = in.readString();
            this.priceKRW = in.readString();
            this.percentChange24h = in.readString();
            this.localCoinType = in.readString();
            this.isChoose = in.readByte() != 0;
        }

        public static final Parcelable.Creator<AccountListBean> CREATOR = new Parcelable.Creator<AccountListBean>() {
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
