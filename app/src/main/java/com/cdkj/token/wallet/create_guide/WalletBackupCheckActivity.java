package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletWordsCheckClickBinding;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 钱包备份验证(顺序点击)
 * Created by cdkj on 2018/6/6.
 */

public class WalletBackupCheckActivity extends AbsBaseLoadActivity {

    private ActivityWalletWordsCheckClickBinding mBinding;

    private List<TextView> mWordsTextViews = new ArrayList<>();//储存显示单词的TextView

    private List<String> mBackupWords = new ArrayList<>();//储存用户点击的单词

    private boolean isFromBackup;

    /**
     * @param context
     * @param isFromBackup 是否来自备份界面
     */
    public static void open(Context context, boolean isFromBackup) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletBackupCheckActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromBackup);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_words_check_click, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        isFromBackup = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);

        List<String> words = WalletHelper.getHelpWordsListByUserId(SPUtilHelper.getUserId());

        Collections.shuffle(words);//打乱数组顺序

        for (String word : words) {

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 25;
            layoutParams.bottomMargin = 10;
            TextView textView = createText(word);
            mWordsTextViews.add(textView);
            mBinding.flexLayout.addView(textView, layoutParams);

        }

        mBinding.btnNowBackup.setOnClickListener(view -> {

            if (!WalletHelper.checkMnenonic(mBackupWords)) {
                UITipDialog.showFail(this, getString(R.string.check_words_fail));
                return;
            }

            UITipDialog.showSuccess(this, getString(R.string.wallet_backup_success), dialogInterface -> {
                WalletHelper.saveWalletFirstCheck(true);
                if (!isFromBackup) {
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                    MainActivity.open(this);
                }
                finish();
            });

        });
    }

    /**
     * 根据文本创建TextView用于显示助记词
     *
     * @param word
     */
    public TextView createText(String word) {

        TextView textView = new TextView(this);

        textView.setText(word);

        textView.setBackgroundResource(R.drawable.btn_gray);

        textView.setTextColor(ContextCompat.getColor(this, R.color.black));

        textView.setPadding(8, 10, 8, 10);

        textView.setTag(false);  //用于记录当前View是否点击

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordsViewClick(textView);
            }
        });

        return textView;

    }


    /**
     * 助记词点击
     *
     * @param textView
     */
    private void wordsViewClick(TextView textView) {

        boolean isClick = (boolean) textView.getTag();

        if (!isClick) {  //如果没有点击
            textView.setBackgroundResource(R.drawable.btn_blue);
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
            mBackupWords.add(textView.getText().toString());
        } else {
            textView.setBackgroundResource(R.drawable.btn_gray);
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
            mBackupWords.remove(textView.getText().toString());
        }
        textView.setTag(!isClick);

        mBinding.tvWords.setText(StringUtils.listToString(mBackupWords, "    ")); //用户点击的字符显示
    }


}
