package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.HelpWordsGridAdapter;
import com.cdkj.token.adapter.HelpWordsGridCheckAdapter;
import com.cdkj.token.databinding.ActivityBackupWalletWordsCheckBinding;
import com.cdkj.token.model.HelpWordsCheckModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.recycler.GridDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包备份单词验证
 * Created by cdkj on 2018/6/7.
 */

public class BackupWalletWordsCheckActivity extends AbsLoadActivity {

    private ActivityBackupWalletWordsCheckBinding mBinding;

    private List<String> mChooseWordList;//用户选择的单词列表
    private HelpWordsGridCheckAdapter helpWordsGridAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletWordsCheckActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_backup_wallet_words_check, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        mChooseWordList = new ArrayList<>();
        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mBinding.recyclerView.addItemDecoration(new GridDivider(this, DisplayHelper.dp2px(this, 1), ContextCompat.getColor(this, R.color.red)));
        initAdapter();

    }

    void initAdapter() {
        List<String> wordsList = WalletHelper.getHelpWordsListByUserId(SPUtilHelper.getUserId());
        Collections.shuffle(wordsList);//打乱数组顺序
        if (wordsList != null && wordsList.size() > 0) {

            helpWordsGridAdapter = new HelpWordsGridCheckAdapter(getWordsModeList(wordsList));
            mBinding.recyclerView.setAdapter(helpWordsGridAdapter);

            helpWordsGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    HelpWordsCheckModel checkModel = helpWordsGridAdapter.getItem(position);

                    if (checkModel == null || checkModel.isChoose()) {  //已经选择过的不再选择
                        return;
                    }
                    mChooseWordList.add(checkModel.getWords());
                    if (mChooseWordList.size() >= 12) {
                        mSubscription.add(Observable.just("")
                                .subscribeOn(Schedulers.newThread())
                                .map(s -> WalletHelper.checkMnenonic(mChooseWordList))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(isPass -> {
                                    showSureListen(getString(R.string.wallet_backup_success), new CommonDialog.OnPositiveListener() {
                                        @Override
                                        public void onPositive(View view) {
                                            showDoubleWarnListen("免责声明", "请确保已备份钱包至安全的地方，\n" +
                                                    "THA不承担任何钱包丢失、被盗、\n" +
                                                    "忘记密码等产生的资产损失！", view1 -> finish());
                                        }
                                    });
                                }, throwable -> {

                                }));
                    }

                    checkModel.setChoose(true);
                    helpWordsGridAdapter.notifyItemChanged(position);

                    addChooseViewByWords(checkModel, position);


                }
            });
        }
    }

    /**
     * 根据用户创建View 显示单词
     *
     * @param checkModel
     */
    void addChooseViewByWords(HelpWordsCheckModel checkModel, int postion) {
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 15;
        layoutParams.bottomMargin = 10;
        TextView textView = createText(checkModel.getWords(), postion);
        mBinding.flexLayout.addView(textView, layoutParams);
    }

    /**
     * 获取适配器shuju
     *
     * @param wordsList
     * @return
     */
    private List<HelpWordsCheckModel> getWordsModeList(List<String> wordsList) {

        List<HelpWordsCheckModel> helpWordsCheckModels = new ArrayList<>();

        for (String s : wordsList) {
            HelpWordsCheckModel helpWordsCheckModel = new HelpWordsCheckModel();
            helpWordsCheckModel.setWords(s);
            helpWordsCheckModels.add(helpWordsCheckModel);
        }
        return helpWordsCheckModels;
    }

    /**
     * 根据文本创建TextView用于显示助记词
     *
     * @param word
     */
    public TextView createText(String word, int position) {

        TextView textView = new TextView(this);

        textView.setText(word);

        textView.setTextColor(Color.parseColor("#407EF9"));

        textView.setBackgroundColor(Color.parseColor("#EDF3FF"));

        textView.setPadding(8, 10, 8, 10);

        textView.setTag(position);

        textView.setOnClickListener(new View.OnClickListener() {      //用户点击已经选择的View
            @Override
            public void onClick(View view) {
                mChooseWordList.remove(textView.getText());
                mBinding.flexLayout.removeView(textView);
                HelpWordsCheckModel helpWordsCheckModel = helpWordsGridAdapter.getItem(position);
                helpWordsCheckModel.setChoose(false);
                helpWordsGridAdapter.notifyItemChanged((int) textView.getTag());
            }
        });

        return textView;

    }

}
