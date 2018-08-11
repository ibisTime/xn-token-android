package com.cdkj.token.model;

import java.util.List;

/**
 * Created by cdkj on 2018/8/11.
 */

public class UTXOListModel {


    /**
     * balance : 2039749196
     * utxoList : [{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"},{"txid":"15cd385f35faf4c4fe075de058d046cb6c2769a7f1e83aec8f4b1144ebd680cc","vout":0,"count":708480540,"scriptPubKey":"76a9146a2e1d4acf975ccf1bfcc0dbd38bb818f37ad32788ac","address":"mqCPAUTqUWLu1zy3Q5thWcAm4iPKhtsK4p"}]
     */

    private String balance;
    private List<UTXOModel> utxoList;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<UTXOModel> getUtxoList() {
        return utxoList;
    }

    public void setUtxoList(List<UTXOModel> utxoList) {
        this.utxoList = utxoList;
    }

}
