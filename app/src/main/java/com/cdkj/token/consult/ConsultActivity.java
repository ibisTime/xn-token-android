package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityConsultBinding;
import com.zzhoujay.richtext.RichText;

/**
 * Created by lei on 2018/3/7.
 */

public class ConsultActivity extends AbsBaseActivity {

    private ActivityConsultBinding mBinding;

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity, String title, String richText, String date) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, ConsultActivity.class);
        intent.putExtra("richText", richText);
        intent.putExtra("title", title);
        intent.putExtra("date", date);
        activity.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_consult, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);
        setTopLineState(true);

        initData();
    }

    private void initData() {

        TextPaint paint = mBinding.tvTitle.getPaint();
        paint.setFakeBoldText(true);

        if (getIntent() == null) {
            return;
        }

        try{
            setTopTitle(getString(R.string.consult_activity_title));

            mBinding.tvTitle.setText(getIntent().getStringExtra("title"));
            mBinding.tvDate.setText(getIntent().getStringExtra("date"));
            RichText.from(getIntent().getStringExtra("richText")).into(mBinding.tvRichText);
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
