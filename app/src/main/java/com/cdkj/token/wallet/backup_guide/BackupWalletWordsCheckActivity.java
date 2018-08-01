package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.HelpWordsGridCheckAdapter;
import com.cdkj.token.databinding.ActivityBackupWalletWordsCheckBinding;
import com.cdkj.token.model.HelpWordsCheckModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.recycler.GridDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

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
    private boolean isFromWalletToolBackup;

    /**
     * @param context
     * @param isFromWalletToolBackup 是否来自备份界面
     */
    public static void open(Context context, boolean isFromWalletToolBackup) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletWordsCheckActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromWalletToolBackup);
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

        isFromWalletToolBackup = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);
        mChooseWordList = new ArrayList<>();
        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mBinding.recyclerView.addItemDecoration(new GridDivider(this, DisplayHelper.dp2px(this, 1), ContextCompat.getColor(this, R.color.gray_dee0e5)));

        ((DefaultItemAnimator) mBinding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        initAdapter();

    }

    void initAdapter() {
        List<String> wordsList = getWordsisFromBackup();

        if (wordsList != null && wordsList.size() > 0) {

            Collections.shuffle(wordsList);//打乱数组顺序

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
                        checkInputWords();
                    }
                    checkModel.setChoose(true);
                    helpWordsGridAdapter.notifyItemChanged(position);
                    addChooseViewByWords(checkModel, position);
                }
            });
        }
    }

    /**
     * 不同界面获取不同数据
     *
     * @return
     */
    public List<String> getWordsisFromBackup() {
        if (isFromWalletToolBackup) {
            return WalletHelper.getHelpWordsListByUserId(SPUtilHelper.getUserId());
        }

        WalletDBModel walletDBModel = JSON.parseObject(SPUtilHelper.getWalletCache(), WalletDBModel.class);

        if (walletDBModel == null) return null;

        return StringUtils.splitAsList(walletDBModel.getHelpWordsEn(), WalletHelper.HELPWORD_SPACE_SYMBOL);
    }


    /**
     * 判断用户选择的单词是否正确
     */
    void checkInputWords() {
        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> WalletHelper.checkMnenonic(mChooseWordList))
                .map(isPass -> {
                    if (isPass && !isFromWalletToolBackup) {              //验证通过 保存创建界面的缓存数据
                        WalletDBModel walletDBModel = JSON.parseObject(SPUtilHelper.getWalletCache(), WalletDBModel.class);
                        return walletDBModel.save();
                    }
                    return isPass;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isPass -> {
                    if (isPass) {
                        SPUtilHelper.createWalletCache("");  //清除缓存
                        showSureDialog(getString(R.string.wallet_backup_success), new CommonDialog.OnPositiveListener() {
                            @Override
                            public void onPositive(View view) {
                                backupStateCheck();
                            }
                        });

                    } else {
                        showSureDialog(getString(R.string.check_words_fail), null);
                    }

                }, throwable -> {

                }));
    }

    /**
     * 备份通过状态
     */
    void backupStateCheck() {
        showDoubleWarnListen(getString(R.string.backup_introtation_dialog_title), getString(R.string.backup_introtation_dialog_content), view1 -> {

            if (!isFromWalletToolBackup) {
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                MainActivity.open(BackupWalletWordsCheckActivity.this);
            }
            finish();
        });
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

    @Override
    public void onBackPressed() {

    }

}
