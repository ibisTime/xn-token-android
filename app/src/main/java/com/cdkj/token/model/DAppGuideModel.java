package com.cdkj.token.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cdkj on 2018/12/5.
 */

public class DAppGuideModel implements Serializable {

    private List<DAppGuide> guideList;

    public List<DAppGuide> getGuideList() {
        return guideList;
    }

    public void setGuideList(List<DAppGuide> guideList) {
        this.guideList = guideList;
    }

    public static class DAppGuide implements Serializable {


        /**
         * id : 1
         * dappId : 1
         * title : 强力球攻略一
         * author : Leo
         * content : 强力球攻略一强力球攻略一强力球攻略一强力球攻略一强力球攻略一强力球攻略一强力球攻略一
         * label : 1
         * likeCount : 0
         * scanCount : 0
         * likeCountFake : 0
         * scanCountFake : 0
         * orderNo : 0
         * status : 0
         * createDatetime : Dec 5, 2018 5:25:18 PM
         */

        private int id;
        private int dappId;
        private String title;
        private String author;
        private String content;
        private String label;
        private int likeCount;
        private int scanCount;
        private int likeCountFake;
        private int scanCountFake;
        private int orderNo;
        private String status;
        private String createDatetime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDappId() {
            return dappId;
        }

        public void setDappId(int dappId) {
            this.dappId = dappId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getScanCount() {
            return scanCount;
        }

        public void setScanCount(int scanCount) {
            this.scanCount = scanCount;
        }

        public int getLikeCountFake() {
            return likeCountFake;
        }

        public void setLikeCountFake(int likeCountFake) {
            this.likeCountFake = likeCountFake;
        }

        public int getScanCountFake() {
            return scanCountFake;
        }

        public void setScanCountFake(int scanCountFake) {
            this.scanCountFake = scanCountFake;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
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
    }

}
