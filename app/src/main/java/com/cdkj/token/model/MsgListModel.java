package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cdkj on 2017/7/20.
 */

public class MsgListModel implements Parcelable {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 2
     * totalPage : 1
     * list : [{"id":32,"fromSystemCode":"CD-CYC000009","channelType":"4","pushType":"41","toSystemCode":"CD-CYC000009","toKind":"2","smsType":"2","smsTitle":"试吃员消息1","smsContent":"试吃员消息1","status":"1","createDatetime":"Jul 20, 2017 8:17:06 PM","topushDatetime":"Jul 20, 2017 8:17:06 PM","pushedDatetime":"Jul 20, 2017 8:17:12 PM","updater":"admin","updateDatetime":"Jul 20, 2017 8:17:12 PM","remark":""},{"id":31,"fromSystemCode":"CD-CYC000009","channelType":"4","pushType":"41","toSystemCode":"CD-CYC000009","toKind":"2","smsType":"2","smsTitle":"试吃员消息2","smsContent":"试吃员消息2","status":"1","createDatetime":"Jul 20, 2017 8:16:50 PM","topushDatetime":"Jul 20, 2017 8:16:50 PM","pushedDatetime":"Jul 20, 2017 8:16:54 PM","updater":"admin","updateDatetime":"Jul 20, 2017 8:16:54 PM","remark":""}]
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> listX) {
        this.list = listX;
    }

    public static class ListBean implements Parcelable {
        /**
         * id : 32
         * fromSystemCode : CD-CYC000009
         * channelType : 4
         * pushType : 41
         * toSystemCode : CD-CYC000009
         * toKind : 2
         * smsType : 2
         * smsTitle : 试吃员消息1
         * smsContent : 试吃员消息1
         * status : 1
         * createDatetime : Jul 20, 2017 8:17:06 PM
         * topushDatetime : Jul 20, 2017 8:17:06 PM
         * pushedDatetime : Jul 20, 2017 8:17:12 PM
         * updater : admin
         * updateDatetime : Jul 20, 2017 8:17:12 PM
         * remark :
         */

        private int id;
        private String fromSystemCode;
        private String channelType;
        private String pushType;
        private String toSystemCode;
        private String toKind;
        private String smsType;
        private String smsTitle;
        private String smsContent;
        private String status;
        private String createDatetime;
        private String topushDatetime;
        private String pushedDatetime;
        private String updater;
        private String updateDatetime;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFromSystemCode() {
            return fromSystemCode;
        }

        public void setFromSystemCode(String fromSystemCode) {
            this.fromSystemCode = fromSystemCode;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getPushType() {
            return pushType;
        }

        public void setPushType(String pushType) {
            this.pushType = pushType;
        }

        public String getToSystemCode() {
            return toSystemCode;
        }

        public void setToSystemCode(String toSystemCode) {
            this.toSystemCode = toSystemCode;
        }

        public String getToKind() {
            return toKind;
        }

        public void setToKind(String toKind) {
            this.toKind = toKind;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        public String getSmsTitle() {
            return smsTitle;
        }

        public void setSmsTitle(String smsTitle) {
            this.smsTitle = smsTitle;
        }

        public String getSmsContent() {
            return smsContent;
        }

        public void setSmsContent(String smsContent) {
            this.smsContent = smsContent;
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

        public String getTopushDatetime() {
            return topushDatetime;
        }

        public void setTopushDatetime(String topushDatetime) {
            this.topushDatetime = topushDatetime;
        }

        public String getPushedDatetime() {
            return pushedDatetime;
        }

        public void setPushedDatetime(String pushedDatetime) {
            this.pushedDatetime = pushedDatetime;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.fromSystemCode);
            dest.writeString(this.channelType);
            dest.writeString(this.pushType);
            dest.writeString(this.toSystemCode);
            dest.writeString(this.toKind);
            dest.writeString(this.smsType);
            dest.writeString(this.smsTitle);
            dest.writeString(this.smsContent);
            dest.writeString(this.status);
            dest.writeString(this.createDatetime);
            dest.writeString(this.topushDatetime);
            dest.writeString(this.pushedDatetime);
            dest.writeString(this.updater);
            dest.writeString(this.updateDatetime);
            dest.writeString(this.remark);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.id = in.readInt();
            this.fromSystemCode = in.readString();
            this.channelType = in.readString();
            this.pushType = in.readString();
            this.toSystemCode = in.readString();
            this.toKind = in.readString();
            this.smsType = in.readString();
            this.smsTitle = in.readString();
            this.smsContent = in.readString();
            this.status = in.readString();
            this.createDatetime = in.readString();
            this.topushDatetime = in.readString();
            this.pushedDatetime = in.readString();
            this.updater = in.readString();
            this.updateDatetime = in.readString();
            this.remark = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public MsgListModel() {
    }

    protected MsgListModel(Parcel in) {
        this.list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Creator<MsgListModel> CREATOR = new Creator<MsgListModel>() {
        @Override
        public MsgListModel createFromParcel(Parcel source) {
            return new MsgListModel(source);
        }

        @Override
        public MsgListModel[] newArray(int size) {
            return new MsgListModel[size];
        }
    };
}
