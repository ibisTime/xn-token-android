package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cdkj.token.R;
import com.cdkj.token.model.RecoverWalletMemonicModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/11/27.
 */

public class RecoverWalletMomonicAdapter extends BaseQuickAdapter<RecoverWalletMemonicModel, BaseViewHolder> {

    EditText edtMemonic;
    EditText edtMemonicNext;

    List<RecoverWalletMemonicModel> mData;

    public RecoverWalletMomonicAdapter(@Nullable List<RecoverWalletMemonicModel> data) {
        super(R.layout.item_recover_wallet_memonic, data);
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecoverWalletMemonicModel item) {

        // 有EditText 禁用复用
        helper.setIsRecyclable(false);

        edtMemonic = helper.getView(R.id.edt_memonic);

        if (helper.getLayoutPosition() == 0){
            edtMemonic.setFocusable(true);
            edtMemonic.setFocusableInTouchMode(true);
            edtMemonic.requestFocus();
        }else {
            edtMemonic.setFocusable(false);
            edtMemonic.setFocusableInTouchMode(false);
            // 如果之前没设置过点击事件，该处可省略
            edtMemonic.setOnClickListener(null);
        }

        setEditTextInhibitInputSpace(helper, item, edtMemonic);

        edtMemonic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                item.setMemonic(editable.toString().trim());
            }
        });
    }

    /**
     * EditText输入空格监听
     * @param editText
     */
    private void setEditTextInhibitInputSpace(BaseViewHolder helper, RecoverWalletMemonicModel item, EditText editText){
        InputFilter filter= (source, start, end, dest, dstart, dend) -> {
            if(source.equals(" ")){
                skipToNext(helper, item);
                return "";
            } else {
                return null;
            }

        };
        editText.setFilters(new InputFilter[]{filter});
    }

    private void skipToNext(BaseViewHolder helper, RecoverWalletMemonicModel item){

        // 跳转到下一个EditText(获得焦点)
        if (helper.getLayoutPosition() != 11){ // 最后一个不可继续跳转

            BaseViewHolder viewHolder = (BaseViewHolder) getRecyclerView().findViewHolderForAdapterPosition(helper.getLayoutPosition()+1);
            if (null != viewHolder){
                edtMemonicNext = viewHolder.getView(R.id.edt_memonic);

                if(null != edtMemonicNext){
                    edtMemonic.clearFocus();
                    edtMemonic.requestFocus();

                    edtMemonicNext.setFocusable(true);
                    edtMemonicNext.setFocusableInTouchMode(true);
                    edtMemonicNext.requestFocus();
                }
            }
        }
    }

}
