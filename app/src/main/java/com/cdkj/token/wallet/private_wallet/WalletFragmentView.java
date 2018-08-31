package com.cdkj.token.wallet.private_wallet;

import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.WalletFragmentAllData;

import java.util.List;

/**
 * Created by cdkj on 2018/8/31.
 */

public interface WalletFragmentView {

    void onAllDataRefresh(WalletFragmentAllData walletFragmentAllData);//界面数据刷新

    void onCoinBalanceRefresh(List<WalletBalanceModel> walletBalanceModels);//资产界面刷新

    void onCardChangeCoinBalaceRefresh(List<WalletBalanceModel> walletBalanceModels);//卡片切换资产界面刷新

    void onAllAssetsString(String allAssts);

    void onWalletAssetsString(String walletAssets);

    void onPrivateWalletAssetsString(String walletAssets);

    void onBulletRefresh(String bullet);//公告刷新刷新


}
