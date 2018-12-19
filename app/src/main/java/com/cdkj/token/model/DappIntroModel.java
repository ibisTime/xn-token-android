package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cdkj on 2018/12/5.
 */

public class DappIntroModel implements Parcelable {

    private String content;

    private List<String> pics;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public DappIntroModel(){

    }

    public DappIntroModel(Parcel in) {
        content = in.readString();
        pics = in.createStringArrayList();
    }

    public static final Creator<DappIntroModel> CREATOR = new Creator<DappIntroModel>() {
        @Override
        public DappIntroModel createFromParcel(Parcel in) {
            return new DappIntroModel(in);
        }

        @Override
        public DappIntroModel[] newArray(int size) {
            return new DappIntroModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeStringList(pics);
    }
}
