package com.cdkj.baselibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by cdkj on 2017/6/8.
 */

public class AppUtils {


    /**
     * 判断一个Activity 是否存在
     *
     * @param clz
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isActivityExist(Activity clz) {

        Activity activity = clz;
        if (activity == null) {
            return false;
        }

        if (activity.isFinishing()) {
            return false;
        }

        if (!checkAndroidVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) //如果版本小于 4.2
        {
            return true;
        }

        if (activity.isDestroyed()) {
            return false;
        }

        return true;
    }

    public static Boolean checkAndroidVersion(int version) {
        if (Build.VERSION.SDK_INT >= version) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * 根据包名跳转到系统自带的应用程序信息界面
     *
     * @param activity
     */
    public static void startDetailsSetting(Activity activity) {
        try {
            Uri packageURI = Uri.parse("package:" + getPackgeName(activity));
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            activity.startActivity(intent);
        } catch (Exception e) {
        }
    }


    /*获取版本信息*/
    public static String getPackgeName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.packageName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /*获取版本信息*/
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /*获取版本信息*/
    public static int getAppVersionCode(Context context) {
        int versionName = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionCode;

        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    /**
     * 验证码倒计时
     *
     * @param count 秒数
     * @param btn   按钮
     * @return
     */
    public static Disposable startCodeDown(final int count, final Button btn) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count)//只发射开始的N项数据或者一定时间内的数据
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        btn.setEnabled(false);
                        btn.setText(CdApplication.getContext().getString(R.string.code_down, count));
                    }
                })

                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   btn.setEnabled(false);
                                   btn.setText(CdApplication.getContext().getString(R.string.code_down, count - aLong));
                                   btn.setBackgroundResource(R.drawable.btn_no_click_gray);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   btn.setEnabled(true);
                                   btn.setText(CdApplication.getContext().getString(R.string.code_down, count));
                                   btn.setBackgroundResource(R.drawable.selector_blue);

                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   btn.setEnabled(true);
                                   btn.setText(R.string.resend_code);
                                   btn.setBackgroundResource(R.drawable.selector_blue);
                               }
                           }
                );
    }


    /**
     * @param count
     * @param btn
     * @param enableTrueRes  可以点击样式
     * @param enableFalseRes 不可以点击样式
     * @return
     */
    public static Disposable startCodeDown(final int count, final Button btn,
                                           final int enableTrueRes, final int enableFalseRes,
                                           final int enableTrueTextColor, final int enableFalseTextColor) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count)//只发射开始的N项数据或者一定时间内的数据
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        btn.setEnabled(false);
                        btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count));
                        btn.setBackgroundResource(enableFalseRes);
                        btn.setTextColor(enableFalseTextColor);
                    }
                })
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   btn.setEnabled(false);
                                   btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count - aLong));
                                   btn.setBackgroundResource(enableFalseRes);
                                   btn.setTextColor(enableFalseTextColor);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   btn.setEnabled(true);
                                   btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count));
                                   btn.setBackgroundResource(enableTrueRes);
                                   btn.setTextColor(enableTrueTextColor);
                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   btn.setEnabled(true);
                                   btn.setTextColor(enableTrueTextColor);
                                   btn.setText(com.cdkj.baselibrary.R.string.resend_code);
                                   btn.setBackgroundResource(enableTrueRes);
                               }
                           }
                );
    }


    public static void startWeb(Context context, String url) {

        LogUtil.E("downloadUrl___" + url);

        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            if (!hasPreferredApplication(context, intent)) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.please_choose_browser)));
            } else {
                ToastUtil.show(context, context.getString(R.string.no_browser));
            }
        } catch (Exception e) {
            ToastUtil.show(context, context.getString(R.string.open_browser_error));
            LogUtil.E("startWeb error");
        }
    }

    //如果info.activityInfo.packageName为android,则没有设置,否则,有默认的程序.
    //判断系统是否设置了默认浏览器
    public static boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }


//    /**
//     * 获取手机联系人
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
//     *
//     * @return 联系人链表
//     */
//    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
//
////        SystemClock.sleep(3000);
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//        // 1.获取内容解析者
//        ContentResolver resolver = context.getContentResolver();
//        // 2.获取内容提供者的地址:com.android.contacts
//        // raw_contacts表的地址 :raw_contacts
//        // view_data表的地址 : data
//        // 3.生成查询地址
//        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
////        Uri raw_uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        Uri date_uri = Uri.parse("content://com.android.contacts/data");
//        // 4.查询操作,先查询raw_contacts,查询contact_id
//        // projection : 查询的字段
//        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
//        try {
//            // 5.解析cursor
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    // 6.获取查询的数据
//                    String contact_id = cursor.getString(0);
//                    // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
//                    // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
//                    // 判断contact_id是否为空
//                    if (!TextUtils.isEmpty(contact_id)) {//null   ""
//                        // 7.根据contact_id查询view_data表中的数据
//                        // selection : 查询条件
//                        // selectionArgs :查询条件的参数
//                        // sortOrder : 排序
//                        // 空指针: 1.null.方法 2.参数为null
//                        // android.provider.ContactsContract.Data.MIMETYPE,
//                        //  android.provider.ContactsContract.Data.DATA1
////                        Cursor c = resolver.query(date_uri, new String[]{"data1",
////                                        "mimetype"}, "raw_contact_id=?",
////                                new String[]{contact_id}, null);
//                        Cursor c = resolver.query(date_uri, new String[]{ContactsContract.Data.DATA1,
//                                        ContactsContract.Data.MIMETYPE}, "raw_contact_id=?",
//                                new String[]{contact_id}, null);
//                        // 8.解析c
//                        if (c != null) {
//                            HashMap<String, String> map = new HashMap<String, String>();
//
//                            while (c.moveToNext()) {
//                                // 9.获取数据
//                                String data1 = c.getString(0); //0
//                                String mimetype = c.getString(1);//1
//                                // 10.根据类型去判断获取的data1数据并保存
//
//                                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
//                                    // 电话
//                                    map.put("mobile", data1);
//                                } else if (mimetype.equals("vnd.android.cursor.item/name")) {
//                                    // 姓名
//                                    map.put("name", data1);
//                                }
//                                // 11.添加到集合中数据
//
//
//                            }
//
//                            list.add(map);
//                        }
//
//                        // 12.关闭cursor
//                        if (c != null) {
//                            c.close();
//                        }
//                    }
//                }
//            }
//        } finally {
//            // 12.关闭cursor
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return list;
//    }
//
//
//    /**
//     * 得到手机SIM卡联系人人信息
//     **/
//    public static List<HashMap<String, String>> getSIMContacts(Context context) {
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//        String[] PHONES_PROJECTION = new String[]{
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
//        ContentResolver resolver = context.getContentResolver();
//// 获取Sims卡联系人
//        Uri uri = Uri.parse("content://icc/adn");
//        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
//                null);
//
//        if (phoneCursor != null) {
//            while (phoneCursor.moveToNext()) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                // 得到手机号码
//                String phoneNumber = phoneCursor.getString(1);
//
//                // 得到联系人名称
//                String contactName = phoneCursor
//                        .getString(0);
//
//                //Sim卡中没有联系人头像
//
//                map.put("name", contactName);
//                map.put("mobile", phoneNumber);
//
//                list.add(map);
//            }
//
//            phoneCursor.close();
//        }
//        return list;
//    }


    /**
     * 获取所有拥有手机号的联系人
     * * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @param context
     * @return
     */
    public static List<HashMap<String, String>> getAllPhoneContacts(Context context) {
        List<HashMap<String, String>> listContacts = new ArrayList<HashMap<String, String>>();

        ContentResolver cr = context.getContentResolver();

        String[] mContactsProjection = new String[]{
            /*    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,*/ //id
                ContactsContract.CommonDataKinds.Phone.NUMBER,   //电话号码
                ContactsContract.Contacts.DISPLAY_NAME,      //姓名
        };

        //查询contacts表中的所有数据
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);

        try {

            if (cursor != null && cursor.getCount() > 0) {
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                while (cursor.moveToNext()) {
                    String phoneNum = cursor.getString(phoneIndex);
                    String name = cursor.getString(nameIndex);

                    if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(name)) {
                        continue;
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    //格式化手机号
                    phoneNum = phoneNum.replace("-", "");
                    phoneNum = phoneNum.replace(" ", "");

                    map.put("name", name);
                    map.put("mobile", phoneNum);
                    listContacts.add(map);
                }
            }
        } catch (Exception e) {
            LogUtil.E("通讯录获取失败" + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listContacts;
    }


    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName) {
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bfr = new BufferedReader(isr);
            String in = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((in = bfr.readLine()) != null) {
                stringBuffer.append(in);
            }
            bfr.close();
            isr.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            LogUtil.E("地址" + e);
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     *              需要权限
     *              <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    public static void callPhonePage(Activity activity, String phone) {
        if (activity == null || TextUtils.isEmpty(phone)) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            LogUtil.E("call phone error" + e);
        }
    }


    /**
     * 设置APP使用的语言
     */
    public static void setAppLanguage(Context context, Locale locale) {
        if (context == null || locale == null) return;
        try {
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
        }
    }


}
