package com.cdkj.token.model;/*
 * Copyright 2012 Matt Corallo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.script.*;

import com.google.common.base.Objects;

import java.io.*;
import java.math.*;
import java.util.Locale;

/**
 * 用于比特币交易签名
 */
public class BtcSignUTXO {

    private Script script;
    private Sha256Hash hash;
    private long index;

    /**
     * Creates a stored transaction output.
     *
     * @param hash  The hash of the containing transaction.
     * @param index The outpoint.
     */
    public BtcSignUTXO(Sha256Hash hash,
                       long index,
                       Script script) {
        this.hash = hash;
        this.index = index;
        this.script = script;
    }


    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Sha256Hash getHash() {
        return hash;
    }

    public void setHash(Sha256Hash hash) {
        this.hash = hash;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

}
