package com.cdkj.token.views;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cdkj.token.R;

/**
 * Created by cdkj on 2018/12/5.
 */

public class ButtonSpan extends ClickableSpan {

    View.OnClickListener onClickListener;
    private Context context;
    private int colorId;

    public ButtonSpan(Context context, View.OnClickListener onClickListener) {
        this(context, onClickListener, R.color.blue_0064ff);
    }

    public ButtonSpan(Context context, View.OnClickListener onClickListener, int colorId){
        this.onClickListener = onClickListener;
        this.context = context;
        this.colorId = colorId;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(colorId));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }
}