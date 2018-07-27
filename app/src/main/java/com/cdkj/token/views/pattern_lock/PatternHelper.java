package com.cdkj.token.views.pattern_lock;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.SPUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.R;
import com.cdkj.token.utils.StringUtil;

import java.util.List;

/**
 * Created by hsg on 14/10/2017.
 */

public class PatternHelper {
    public static final int MAX_SIZE = 4;
    public static final int MAX_TIMES = 5;

    private String message;
    private String storagePwd;
    private String tmpPwd;
    private int times;
    private boolean isFinish;
    private boolean isOk;

    public void validateForSetting(List<Integer> hitList) {
        this.isFinish = false;
        this.isOk = false;

        if ((hitList == null) || (hitList.size() < MAX_SIZE)) {
            this.tmpPwd = null;
            this.message = getSizeErrorMsg();
            return;
        }

        //1. draw first time
        if (TextUtils.isEmpty(this.tmpPwd)) {
            this.tmpPwd = convert2String(hitList);
            this.message = getReDrawMsg();
            this.isOk = true;
            return;
        }

        //2. draw second times
        if (this.tmpPwd.equals(convert2String(hitList))) {
            this.message = getSettingSuccessMsg();
            saveToStorage(this.tmpPwd);
            this.isOk = true;
            this.isFinish = true;
        } else {
            this.tmpPwd = null;
            this.message = getDiffPreErrorMsg();
        }
    }

    public void validateForChecking(List<Integer> hitList) {
        this.isOk = false;

        if ((hitList == null) || (hitList.size() < MAX_SIZE)) {
            this.times++;
            this.isFinish = this.times > MAX_SIZE;
            this.message = getPwdErrorMsg();
            return;
        }

        this.storagePwd = getFromStorage();
        if (!TextUtils.isEmpty(this.storagePwd) && this.storagePwd.equals(convert2String(hitList))) {
            this.message = getCheckingSuccessMsg();
            this.isOk = true;
            this.isFinish = true;
        } else {
            this.times++;
            this.isFinish = this.times > MAX_SIZE;
            this.message = getPwdErrorMsg();
        }
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean isOk() {
        return isOk;
    }

    private String getReDrawMsg() {
        return MyApplication.getInstance().getString(R.string.draw_pattern_pwd_2);
    }

    private String getSettingSuccessMsg() {
        return MyApplication.getInstance().getString(R.string.set_pattern_pwd_success);
    }

    private String getCheckingSuccessMsg() {
        return MyApplication.getInstance().getString(R.string.pattern_unlock_success);
    }

    private String getSizeErrorMsg() {
        return MyApplication.getInstance().getString(R.string.draw_pattern_pwd_max_hint, MAX_SIZE);
    }

    private String getDiffPreErrorMsg() {
        return MyApplication.getInstance().getString(R.string.reset_pattern_pwd);
    }

    private String getPwdErrorMsg() {
        return MyApplication.getInstance().getString(R.string.pattern_pwd_error_number, getRemainTimes());
    }

    private String convert2String(List<Integer> hitList) {
        return StringUtils.listToString(hitList);
    }

    private void saveToStorage(String gesturePwd) {
        SPUtilHelper.saveUserPatternPwd(gesturePwd);
    }

    private String getFromStorage() {
        return SPUtilHelper.getUserPatternPwd();
    }

    private int getRemainTimes() {
        return (times <= MAX_TIMES) ? (MAX_TIMES - times) : 0;
    }

    public boolean isAllClear() {
        return this.times >= MAX_SIZE;
    }
}
