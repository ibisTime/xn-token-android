package com.cdkj.token.views.pop;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cdkj.token.R;
import com.cdkj.token.databinding.PopGasPriceChooseBinding;
import com.cdkj.token.databinding.PopupPickerBinding;
import com.cdkj.token.views.ScrollPicker;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * pickerView选择
 * Created by cdkj on 2018/5/11.
 */

public class PickerPop extends BasePopupWindow {

    private PopupPickerBinding mBinding;

    private onSelectListener onSelectListener;


    public void setOnSelectListener(PickerPop.onSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    /**
     * @param context
     * @param
     */
    public PickerPop(Context context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public View onCreatePopupView() {
        mBinding = DataBindingUtil.inflate(((Activity) getContext()).getLayoutInflater(), R.layout.popup_picker, null, false);

        initClickListener();

        return mBinding.getRoot();
    }

    private void initClickListener() {
        mBinding.tvConfirm.setOnClickListener(view -> {
            if (onSelectListener != null) {
                onSelectListener.onSureSelect(mBinding.scrollpicker.getSelectedItem());
            }
            dismiss();
        });

        mBinding.tvCancel.setOnClickListener(view -> dismiss());
    }

    @Override
    public View initAnimaView() {
        return mBinding.content;
    }

    @Override
    protected Animation initShowAnimation() {
        return AnimationUtils.loadAnimation(getContext(), R.anim.pop_bottom_in);
    }

    @Override
    protected Animation initExitAnimation() {
        return AnimationUtils.loadAnimation(getContext(), R.anim.pop_bottom_out);
    }

    @Override
    public View getClickToDismissView() {
        return mBinding.dissmissView;
    }


    public void setPickerViewData(List<ScrollPicker.ScrollPickerData> pickerViewData) {
        mBinding.scrollpicker.setData(pickerViewData);
    }

    public void setIsCirculationt(boolean isCirculationt) {
        mBinding.scrollpicker.setIsCirculationt(isCirculationt);
    }

    public interface onSelectListener {
        void onSureSelect(ScrollPicker.ScrollPickerData selectPosition);
    }

}
