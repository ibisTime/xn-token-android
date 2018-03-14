package com.cdkj.token.model;

/**
 * Created by lei on 2018/3/7.
 */

public class ConsultModel {


    /**
     * address : 门牌号
     * advPic : IOS_1502332783370819_1080_1440.jpg
     * area : 滨江区
     * bookMobile : 13868074590
     * city : 杭州市
     * code : SJ201708101643082336285
     * companyCode : CD-CZH000001
     * description : 百合
     * name : iOS
     * owner : U20170810163312661723
     * pic : IOS_1502332783645850_750_1334.jpg
     * province : 浙江省
     * slogan : 恍恍惚惚
     * status : 1(1=待上架，2=已上架，4=已下架)
     * systemCode : CD-CZH000001
     * uiOrder : 1
     */

    private String address;
    private String advPic;
    private String bookMobile;
    private String city;
    private String code;
    private String description;
    private String name;
    private String pic;
    private String slogan;
    private String province;
    private String area;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getBookMobile() {
        return bookMobile;
    }

    public void setBookMobile(String bookMobile) {
        this.bookMobile = bookMobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
