package com.cdkj.token.pop;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cdkj.token.R;
import com.cdkj.token.databinding.PopGasPriceChooseBinding;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by cdkj on 2018/5/11.
 */

public class GasTypeChoosePop extends BasePopupWindow {

    private PopGasPriceChooseBinding mBinding;

    private onItemClickListener itemClickListener;

    public GasTypeChoosePop(Context context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public View onCreatePopupView() {
        mBinding = DataBindingUtil.inflate(((Activity) getContext()).getLayoutInflater(), R.layout.pop_gas_price_choose, null, false);
        initClickListener();
        return mBinding.getRoot();
    }

    /**
     * 初始化点击事件
     */
    private void initClickListener() {

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


    public interface onItemClickListener {
        void onItemClick(View view);
    }

}
