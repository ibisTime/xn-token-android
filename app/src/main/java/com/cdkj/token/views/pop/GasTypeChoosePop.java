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

import razerdp.basepopup.BasePopupWindow;

/**
 * 转账手续费选择
 * Created by cdkj on 2018/5/11.
 */

public class GasTypeChoosePop extends BasePopupWindow implements View.OnClickListener {

    private PopGasPriceChooseBinding mBinding;

    private onItemClickListener itemClickListener;

    public static final int FIRST = 0;
    public static final int ORDINARY = 1;
    public static final int ECONOMICS = 2;


    /**
     * @param context
     * @param chooseType
     */
    public GasTypeChoosePop(Context context, int chooseType) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        switch (chooseType) {
            case FIRST:
                mBinding.imgChooseFirst.setVisibility(View.VISIBLE);
                mBinding.imgChooseOrdinary.setVisibility(View.GONE);
                mBinding.imgChooseEconomics.setVisibility(View.GONE);

                break;
            case ORDINARY:
                mBinding.imgChooseFirst.setVisibility(View.GONE);
                mBinding.imgChooseOrdinary.setVisibility(View.VISIBLE);
                mBinding.imgChooseEconomics.setVisibility(View.GONE);
                break;
            case ECONOMICS:
                mBinding.imgChooseFirst.setVisibility(View.GONE);
                mBinding.imgChooseOrdinary.setVisibility(View.GONE);
                mBinding.imgChooseEconomics.setVisibility(View.VISIBLE);
                break;
        }
    }


    public GasTypeChoosePop setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
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
        mBinding.fraLayoutChooseEconomics.setOnClickListener(this);
        mBinding.fraLayoutChooseFirst.setOnClickListener(this);
        mBinding.fraLayoutChooseOrdinary.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {

        if (itemClickListener != null) {

            switch (view.getId()) {
                case R.id.fraLayout_choose_first:
                    itemClickListener.onItemClick(FIRST, getContext().getString(R.string.priority));
                    break;
                case R.id.fraLayout_choose_ordinary:
                    itemClickListener.onItemClick(ORDINARY, getContext().getString(R.string.ordinary));
                    break;
                case R.id.fraLayout_choose_economics:
                    itemClickListener.onItemClick(ECONOMICS, getContext().getString(R.string.economics));
                    break;
            }
        }

        dismiss();
    }

    public interface onItemClickListener {
        void onItemClick(int chooseType, String typeString);
    }

}
