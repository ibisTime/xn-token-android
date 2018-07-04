package com.cdkj.baselibrary.appmanager;


public class MyConfig {

    public final static String COMPANYCODE = "CD-TOKEN00018";
    public final static String SYSTEMCODE = "CD-TOKEN00018";

    public final static String USERTYPE = "C";//用户类型


    public final static int NODE_DEV = 0; //研发测试环境
    public final static int NODE_REALSE = 1;//真实环境

    //默认七牛url
    public static String IMGURL = "http://pajvine9a.bkt.clouddn.com/";

    // 拍照文件保存路径
    public static final String CACHDIR = "tha_photo";

    /**
     * 获取当前节点类型
     *
     * @return
     */
    public static int getThisNodeType() {
        return NODE_DEV;
    }

    // 环境访问地址
    public static final String BASE_URL_DEV = "http://120.26.6.213:2101/forward-service/"; // 研发

    public static final String BASE_URL_TEST = "http://120.26.6.213:2101/forward-service/"; // 测试

    public static final String BASE_URL_ONLINE = "http://120.26.6.213:2101/forward-service/"; // 测试

//    public static final String BASE_URL_ONLINE = "http://47.75.165.70:2101/forward-service/"; // 线上

}
