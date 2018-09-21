package com.cdkj.baselibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.views.LoadingView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * QMUI
 *
 * @author cginechen
 * @date 2016-10-14
 */

public class UITipDialog extends Dialog {

    private static UITipDialog tipDialog;

    public UITipDialog(Context context) {
        this(context, R.style.TipDialog);
    }

    public UITipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public static void showSuccess(Activity activity, String info) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(UITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(info)
                .create();
        tipDialog.show();
        timerDismiss();
    }

    public static void showSuccess(Activity activity, String info, OnDismissListener listener) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(UITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(info)
                .create();
        if (listener != null) {
            tipDialog.setOnDismissListener(listener);
        }
        tipDialog.show();
        timerDismiss();
    }

    public static void showFail(Activity activity, String info, OnDismissListener listener) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(Builder.ICON_TYPE_FAIL)
                .setTipWord(info)
                .create();
        if (listener != null) {
            tipDialog.setOnDismissListener(listener);
        }
        tipDialog.show();
        timerDismiss();
    }

    private static void timerDismiss() {
        if (tipDialog == null) return;
        Observable.timer(1350, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tipDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tipDialog.dismiss();
                    }
                });
    }

    public static void showFail(Activity activity, String info) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(Builder.ICON_TYPE_FAIL)
                .setTipWord(info)
                .create();

        tipDialog.show();
        timerDismiss();
    }

    public static void showInfo(Activity activity, String info) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(Builder.ICON_TYPE_INFO)
                .setTipWord(info)
                .create();
        tipDialog.show();
        timerDismiss();
    }

    public static void showInfoNoIcon(Activity activity, String info) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(Builder.ICON_TYPE_NOTHING)
                .setTipWord(info)
                .create();
        tipDialog.show();
        timerDismiss();
    }

    public static void showInfo(Activity activity, String info, OnDismissListener listener) {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        if (activity == null || activity.isFinishing()) {
            return;
        }
        tipDialog = new UITipDialog.Builder(activity)
                .setIconType(UITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord(info)
                .create();
        if (listener != null) {
            tipDialog.setOnDismissListener(listener);
        }
        tipDialog.show();
        timerDismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }

    /**
     * 生成默认的 {
     * <p>
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 {@link IconType}
     * </p>
     *
     * @see CustomBuilder
     */
    public static class Builder {
        /**
         * 不显示任何icon
         */
        public static final int ICON_TYPE_NOTHING = 0;
        /**
         * 显示 Loading 图标
         */
        public static final int ICON_TYPE_LOADING = 1;
        /**
         * 显示成功图标
         */
        public static final int ICON_TYPE_SUCCESS = 2;
        /**
         * 显示失败图标
         */
        public static final int ICON_TYPE_FAIL = 3;
        /**
         * 显示信息图标
         */
        public static final int ICON_TYPE_INFO = 4;

        @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }

        private
        @IconType
        int mCurrentIconType = ICON_TYPE_NOTHING;

        private Context mContext;

        private CharSequence mTipWord;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置 icon 显示的内容
         *
         * @see IconType
         */
        public Builder setIconType(@IconType int iconType) {
            mCurrentIconType = iconType;
            return this;
        }

        /**
         * 设置显示的文案
         */
        public Builder setTipWord(CharSequence tipWord) {
            mTipWord = tipWord;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public UITipDialog create() {
            UITipDialog dialog = new UITipDialog(mContext);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentWrap = (ViewGroup) dialog.findViewById(R.id.contentWrap);

            if (mCurrentIconType == ICON_TYPE_LOADING) {
                LoadingView loadingView = new LoadingView(mContext);
                loadingView.setColor(Color.WHITE);
                loadingView.setSize(DensityUtil.dp2px(32));
                LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                loadingView.setLayoutParams(loadingViewLP);
                contentWrap.addView(loadingView);

            } else if (mCurrentIconType == ICON_TYPE_SUCCESS || mCurrentIconType == ICON_TYPE_FAIL || mCurrentIconType == ICON_TYPE_INFO) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(imageViewLP);

                if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.tips_icon_notify_done));
                } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.tips_icon_notify_error));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.tips_icon_notify_info));
                }

                contentWrap.addView(imageView);

            }

            if (mTipWord != null && mTipWord.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (mCurrentIconType != ICON_TYPE_NOTHING) {
                    tipViewLP.topMargin = DensityUtil.dp2px(12);
                }
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(mTipWord);

                contentWrap.addView(tipView);
            }
            return dialog;
        }

    }

    /**
     * 传入自定义的布局并使用这个布局生成 TipDialog
     */
    public static class CustomBuilder {
        private Context mContext;
        private int mContentLayoutId;

        public CustomBuilder(Context context) {
            mContext = context;
        }

        public CustomBuilder setContent(@LayoutRes int layoutId) {
            mContentLayoutId = layoutId;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public UITipDialog create() {
            UITipDialog dialog = new UITipDialog(mContext);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentWrap = (ViewGroup) dialog.findViewById(R.id.contentWrap);
            LayoutInflater.from(mContext).inflate(mContentLayoutId, contentWrap, true);
            return dialog;
        }
    }
}
