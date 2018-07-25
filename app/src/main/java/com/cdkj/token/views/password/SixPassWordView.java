package com.cdkj.token.views.password;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 6位数字密码  灰色线条加黑色原点 硬编码
 * Created by cdkj on 2018/7/25.
 */

public class SixPassWordView extends View {

    private Paint mPaint;

    private int maxLength = 6; //密码长度

    private List<Integer> mPassList;//用于记录用户输入密码

    private PasswordInputEndListener passwordInputEndListener;


    public void setPasswordInputEndListener(PasswordInputEndListener passwordInputEndListener) {
        this.passwordInputEndListener = passwordInputEndListener;
    }

    public interface PasswordInputEndListener {
        void passEnd(String password);
    }


    public SixPassWordView(Context context) {
        this(context, null);
    }

    public SixPassWordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SixPassWordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPassList = new ArrayList<>();

        //设置点击时弹出输入法
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(SixPassWordView.this, InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });
        this.setOnKeyListener(new SixPassWordView.MyKeyListener());//按键监听

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (modeWidth == MeasureSpec.EXACTLY) {//如果是精确测量 则直接返回值
            width = sizeWidth;
        } else {//指定宽度的大小
            width = 150;
            if (modeWidth == MeasureSpec.AT_MOST) {//如果是最大值模式  取当中的小值  防止超出父类控件的最大值
                width = Math.min(width, sizeWidth);
            }
        }

        if (modeHeight == MeasureSpec.EXACTLY) {//如果是精确测量 则直接返回值
            height = sizeHeight;
        } else {//指定高度的大小
            height = 55;
            if (modeHeight == MeasureSpec.AT_MOST) {//如果是最大值模式  取当中的小值  防止超出父类控件的最大值
                height = Math.min(height, sizeHeight);
            }
        }
        setMeasuredDimension(width, height);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // 设置“空心”的外框的宽度
        mPaint.setPathEffect(new CornerPathEffect(1));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#DEE0E5"));
        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setStrokeWidth(1.5f);
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rect, DisplayHelper.dp2px(getContext(), 4), DisplayHelper.dp2px(getContext(), 4), mPaint);

        float lineX = 0;                      //分割线宽度
        float lineX2 = getWidth() / maxLength;
        mPaint.setStrokeWidth(1f);
        for (int i = 1; i < maxLength; i++) {
            lineX = i * lineX2;
            canvas.drawLine(lineX, 0, lineX, getHeight(), mPaint);
        }

        mPaint.setStyle(Paint.Style.FILL);//实心
        float circleY = getHeight() / 2;
        mPaint.setColor(Color.parseColor("#333333"));
        for (int i = 0; i < mPassList.size(); i++) {

            float circleX = i * lineX2 + lineX2 / 2;

            canvas.drawCircle(circleX, circleY, 10, mPaint);  //绘制圆心
        }


    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;          //显示数字键盘
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        return new ZanyInputConnection(this, false);
    }


    private class ZanyInputConnection extends BaseInputConnection {


        public ZanyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    /**
     * 添加密码
     *
     * @param pwd
     */
    public void addPwd(int pwd) {
        if (mPassList == null) {
            return;
        }
        if (mPassList.size() < maxLength) {
            mPassList.add(pwd);
            invalidate();
        }

        if (mPassList.size() == maxLength && passwordInputEndListener != null) {
            passwordInputEndListener.passEnd(getPassString());
            mPassList.clear();
            invalidate();
        }

    }

    /**
     * 删除密码
     */
    public void removePwd() {
        if (mPassList != null && mPassList.size() > 0) {
            mPassList.remove(mPassList.size() - 1);
            invalidate();
        }
    }

    /**
     * 获取密码
     *
     * @return pwd
     */
    public String getPassString() {
        StringBuffer passString = new StringBuffer();
        for (int i : mPassList) {
            passString.append(i);
        }
        return passString.toString();
    }


    /**
     * 按键监听器
     */
    class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }

                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//处理数字
//                    addPwd(keyCode - 7 );              //点击添加密码
                    addPwdByKeyCode(keyCode);
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DEL) {       //点击删除
                    removePwd();
                    return true;
                }

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        }//onKey

        void addPwdByKeyCode(int keyCode) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_0:
                    addPwd(0);
                    break;
                case KeyEvent.KEYCODE_1:
                    addPwd(1);
                    break;
                case KeyEvent.KEYCODE_2:
                    addPwd(2);
                    break;
                case KeyEvent.KEYCODE_3:
                    addPwd(3);
                    break;
                case KeyEvent.KEYCODE_4:
                    addPwd(4);
                    break;
                case KeyEvent.KEYCODE_5:
                    addPwd(5);
                    break;
                case KeyEvent.KEYCODE_6:
                    addPwd(6);
                    break;
                case KeyEvent.KEYCODE_7:
                    addPwd(7);
                    break;
                case KeyEvent.KEYCODE_8:
                    addPwd(8);
                    break;
                case KeyEvent.KEYCODE_9:
                    addPwd(9);
                    break;
            }
        }
    }

}
