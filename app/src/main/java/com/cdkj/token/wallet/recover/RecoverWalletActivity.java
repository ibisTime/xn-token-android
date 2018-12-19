package com.cdkj.token.wallet.recover;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.adapter.RecoverWalletMomonicAdapter;
import com.cdkj.token.databinding.ActivityRecoverWalletBinding;
import com.cdkj.token.model.RecoverWalletMemonicModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.backup_guide.BackupWalletSuccessActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 恢复钱包
 * Created by cdkj on 2018/11/27.
 */

public class RecoverWalletActivity extends AbsStatusBarTranslucentActivity {

    private ActivityRecoverWalletBinding mBinding;

    private List<RecoverWalletMemonicModel> mList;

    private ArrayList<String> mWordList;

    private String mPassword;

    public static void open(Context context, String mPassword) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RecoverWalletActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, mPassword);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_recover_wallet,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initListener();
        initRecyclerView();
    }

    private void init() {
        mPassword = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        mList = new ArrayList<>();
        while (mList.size()<12){
            mList.add(new RecoverWalletMemonicModel());
        }
    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            if (check()){
                checkWordsAndSave(mList);
            }
        });
    }

    private void initRecyclerView() {

        mBinding.rvMemonic.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        RecoverWalletMomonicAdapter mAdapter = new RecoverWalletMomonicAdapter(mList);
        mAdapter.bindToRecyclerView(mBinding.rvMemonic);
        mBinding.rvMemonic.setAdapter(mAdapter);
    }

    private boolean check(){

        for (RecoverWalletMemonicModel model : mList){

            if(TextUtils.isEmpty(model.getMemonic())){
                Toast.makeText(this, "请将助记词填写完整", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return true;
    }

    /**
     * 验证组记词
     */
    private void checkWordsAndSave(List<RecoverWalletMemonicModel> mnenonic) {
        showLoadingDialog();
        mSubscription.add(
                Observable.just(mnenonic)
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> {
                            mWordList = formatMemonicList(s);
                            return mWordList;
                        })
                        .map(wds -> WalletHelper.checkMnenonic(wds))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .filter(isPass -> {
                            if (!isPass) {
                                UITipDialog.showFail(this, getString(R.string.wallet_import_fail));
                            }
                            return isPass == true;
                        })
                        .subscribeOn(Schedulers.newThread())
                        .map(isPass -> {
                            WalletDBModel dbModel = WalletHelper.createAllCoinPrivateKeybyMnenonic(mWordList); //TODO 导入缺少BTC
                            dbModel.setWalletPassWord(mPassword);
                            dbModel.setUserId(WalletHelper.WALLET_USER);
                            dbModel.setWalletName(WalletHelper.DEFAULT_PRI_WALLET);
                            return dbModel.save();
                        })

                        .observeOn(AndroidSchedulers.mainThread())

                        .doFinally(() -> disMissLoadingDialog())

                        .subscribe(isSave -> {

                            if (isSave) {
                                BackupWalletSuccessActivity.open(this, BackupWalletSuccessActivity.RECOVER);
                                finish();
                            } else {
                                UITipDialog.showFail(this, getString(R.string.wallet_import_fail));
                            }

                        }, throwable -> {
                            ToastUtil.show(RecoverWalletActivity.this, getString(R.string.check_words_fail));
                        })


        );
    }

    private ArrayList<String> formatMemonicList(List<RecoverWalletMemonicModel> mnenonic){

        ArrayList<String> arrayList = new ArrayList<>();

        for(RecoverWalletMemonicModel memonicModel : mnenonic){

            if (!TextUtils.isEmpty(memonicModel.getMemonic()))
                arrayList.add(memonicModel.getMemonic());
        }

        return arrayList;

    }

}
