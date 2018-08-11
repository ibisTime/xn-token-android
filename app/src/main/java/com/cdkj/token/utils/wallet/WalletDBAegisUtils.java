package com.cdkj.token.utils.wallet;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cdkj.token.model.DbCoinInfo;
import com.cdkj.token.model.db.UserConfigDBModel;
import com.cdkj.token.model.db.WalletDBModel;

import org.litepal.crud.DataSupport;

import static com.cdkj.token.utils.wallet.WalletDBColumn.BTC_ADDRESS;
import static com.cdkj.token.utils.wallet.WalletDBColumn.BTC_PRIVATEKEY;
import static com.cdkj.token.utils.wallet.WalletDBColumn.FIND_USER_SQL;
import static com.cdkj.token.utils.wallet.WalletHelper.createBTCInfoByMnemonic;
import static com.cdkj.token.utils.wallet.WalletHelper.getHelpWordsListByUserId;

/**
 * 钱包数据库维护工具类
 * Created by cdkj on 2018/8/11.
 */

public class WalletDBAegisUtils {


    /**
     * 检测钱包数据库是否有btc信息
     */
    public static boolean checkBTCInfoNotNull(String userId) {

        Cursor cursor = WalletHelper.getUserInfoCursorByUserId(userId);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                String btcAddress = cursor.getString(cursor.getColumnIndex(BTC_ADDRESS)); //btc地址

                String btcPrivateKey = cursor.getString(cursor.getColumnIndex(BTC_PRIVATEKEY));//btc私钥

                if (TextUtils.isEmpty(btcAddress) || TextUtils.isEmpty(btcPrivateKey)) {
                    return false;
                }

                return true;

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }


    /**
     * 更新btc信息
     */
    private static void updateBTCInfo(String userId, DbCoinInfo dbCoinInfo) {
        ContentValues values = new ContentValues();
        values.put(WalletDBColumn.BTC_ADDRESS, dbCoinInfo.getAddress());
        values.put(WalletDBColumn.BTC_PRIVATEKEY, dbCoinInfo.getPrivateKey());
        DataSupport.updateAll(WalletDBModel.class, values, FIND_USER_SQL, userId);
    }

    /**
     * 生成BTC信息并更新
     *
     * @param userId
     */
    public static void createBTCInfoAndUpdate(String userId) {
        DbCoinInfo dbCoinInfo = createBTCInfoByMnemonic(getHelpWordsListByUserId(userId));
        if (dbCoinInfo != null) {
            updateBTCInfo(userId, dbCoinInfo);
        }
    }


}
