package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * btc账单流水
 * Created by cdkj on 2018/8/13.
 */

public class BTCBillModel implements Parcelable {


    /**
     * txHash : 4bf64da1bafbd3c41696eb2ef0456a49191b556c970b7e90c2dc26ceff37c557
     * height : 1383049
     * direction : 1
     * value : 232458916
     * txFee : 113000
     * valueIn : 450206435702
     * valueOut : 450206322702
     * size : 226
     * transDatetime : Aug 11, 2018 3:21:13 PM
     * vin : [{"txid":"48be8f7ced984d9255069f88a903f36696f945a22f65fa3435a9b5c1f566a019","n":0,"vout":1,"addr":"mpBMVZG67JLd35aqZPKoba5Cevp8L6AKKi","valueSat":450206435702,"value":4502.06435702}]
     * vout : [{"n":0,"scriptPubKey":"76a9141c317dd81b87e270f54cdb62340cb7bc4088726888ac","addr":"mi62WAG1oJsCudkYvZFuDMnXyFu2z2HoYQ","valueSat":449973863786,"value":4499.73863786},{"n":0,"scriptPubKey":"76a9141c317dd81b87e270f54cdb62340cb7bc4088726888ac","addr":"mi62WAG1oJsCudkYvZFuDMnXyFu2z2HoYQ","valueSat":449973863786,"value":4499.73863786}]
     */

    private String txHash;
    private String height;
    private String direction;
    private BigDecimal value;
    private BigDecimal txFee;
    private long valueIn;
    private long valueOut;
    private int size;
    private String transDatetime;
    private List<VinBean> vin;
    private List<VoutBean> vout;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public long getValueIn() {
        return valueIn;
    }

    public void setValueIn(long valueIn) {
        this.valueIn = valueIn;
    }

    public long getValueOut() {
        return valueOut;
    }

    public void setValueOut(long valueOut) {
        this.valueOut = valueOut;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(String transDatetime) {
        this.transDatetime = transDatetime;
    }

    public List<VinBean> getVin() {
        return vin;
    }

    public void setVin(List<VinBean> vin) {
        this.vin = vin;
    }

    public List<VoutBean> getVout() {
        return vout;
    }

    public void setVout(List<VoutBean> vout) {
        this.vout = vout;
    }

    public static class VinBean implements Parcelable {
        /**
         * txid : 48be8f7ced984d9255069f88a903f36696f945a22f65fa3435a9b5c1f566a019
         * n : 0
         * vout : 1
         * addr : mpBMVZG67JLd35aqZPKoba5Cevp8L6AKKi
         * valueSat : 450206435702
         * value : 4502.06435702
         */

        private String txid;
        private int n;
        private int vout;
        private String addr;
        private long valueSat;
        private double value;

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getVout() {
            return vout;
        }

        public void setVout(int vout) {
            this.vout = vout;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public long getValueSat() {
            return valueSat;
        }

        public void setValueSat(long valueSat) {
            this.valueSat = valueSat;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.txid);
            dest.writeInt(this.n);
            dest.writeInt(this.vout);
            dest.writeString(this.addr);
            dest.writeLong(this.valueSat);
            dest.writeDouble(this.value);
        }

        public VinBean() {
        }

        protected VinBean(Parcel in) {
            this.txid = in.readString();
            this.n = in.readInt();
            this.vout = in.readInt();
            this.addr = in.readString();
            this.valueSat = in.readLong();
            this.value = in.readDouble();
        }

        public static final Creator<VinBean> CREATOR = new Creator<VinBean>() {
            @Override
            public VinBean createFromParcel(Parcel source) {
                return new VinBean(source);
            }

            @Override
            public VinBean[] newArray(int size) {
                return new VinBean[size];
            }
        };
    }

    public static class VoutBean implements Parcelable {
        /**
         * n : 0
         * scriptPubKey : 76a9141c317dd81b87e270f54cdb62340cb7bc4088726888ac
         * addr : mi62WAG1oJsCudkYvZFuDMnXyFu2z2HoYQ
         * valueSat : 449973863786
         * value : 4499.73863786
         */

        private int n;
        private String scriptPubKey;
        private String addr;
        private long valueSat;
        private double value;

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getScriptPubKey() {
            return scriptPubKey;
        }

        public void setScriptPubKey(String scriptPubKey) {
            this.scriptPubKey = scriptPubKey;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public long getValueSat() {
            return valueSat;
        }

        public void setValueSat(long valueSat) {
            this.valueSat = valueSat;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.n);
            dest.writeString(this.scriptPubKey);
            dest.writeString(this.addr);
            dest.writeLong(this.valueSat);
            dest.writeDouble(this.value);
        }

        public VoutBean() {
        }

        protected VoutBean(Parcel in) {
            this.n = in.readInt();
            this.scriptPubKey = in.readString();
            this.addr = in.readString();
            this.valueSat = in.readLong();
            this.value = in.readDouble();
        }

        public static final Creator<VoutBean> CREATOR = new Creator<VoutBean>() {
            @Override
            public VoutBean createFromParcel(Parcel source) {
                return new VoutBean(source);
            }

            @Override
            public VoutBean[] newArray(int size) {
                return new VoutBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.txHash);
        dest.writeString(this.height);
        dest.writeString(this.direction);
        dest.writeSerializable(this.value);
        dest.writeSerializable(this.txFee);
        dest.writeLong(this.valueIn);
        dest.writeLong(this.valueOut);
        dest.writeInt(this.size);
        dest.writeString(this.transDatetime);
        dest.writeList(this.vin);
        dest.writeList(this.vout);
    }

    public BTCBillModel() {
    }

    protected BTCBillModel(Parcel in) {
        this.txHash = in.readString();
        this.height = in.readString();
        this.direction = in.readString();
        this.value = (BigDecimal) in.readSerializable();
        this.txFee = (BigDecimal) in.readSerializable();
        this.valueIn = in.readLong();
        this.valueOut = in.readLong();
        this.size = in.readInt();
        this.transDatetime = in.readString();
        this.vin = new ArrayList<VinBean>();
        in.readList(this.vin, VinBean.class.getClassLoader());
        this.vout = new ArrayList<VoutBean>();
        in.readList(this.vout, VoutBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<BTCBillModel> CREATOR = new Parcelable.Creator<BTCBillModel>() {
        @Override
        public BTCBillModel createFromParcel(Parcel source) {
            return new BTCBillModel(source);
        }

        @Override
        public BTCBillModel[] newArray(int size) {
            return new BTCBillModel[size];
        }
    };
}
