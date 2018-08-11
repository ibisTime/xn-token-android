package com.cdkj.token.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加相册图片适配器
 * Created by cdkj on 2018/5/25.
 */

public class AddPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private final String addPhotoFlag = "photoadd";

    public static final int PHOTOCOUNT = 9;//最多选择数量

    public AddPhotoAdapter() {
        super(R.layout.item_photo_add);
        init();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ViewGroup.LayoutParams params = helper.getView(R.id.img_center).getLayoutParams();
        params.height = DisplayHelper.getScreenWidth(mContext) / 3;  //设置item宽高
        params.width = params.height - DisplayHelper.dp2px(mContext, 15);
        helper.getView(R.id.img_center).setLayoutParams(params);


        if (isAddPhoto(helper.getLayoutPosition())) {
            helper.setGone(R.id.fra_delete, false);

            helper.setImageResource(R.id.img, R.drawable.photo_add);

        } else {
            helper.setGone(R.id.fra_delete, true);
            ImgUtils.loadLocalImage(mContext, item, helper.getView(R.id.img));
        }

        helper.addOnClickListener(R.id.fra_delete);
        helper.addOnClickListener(R.id.img);

    }


    /**
     * 当前是否是添加图片状态
     *
     * @param position
     * @return
     */
    public boolean isAddPhoto(int position) {
        return TextUtils.equals(addPhotoFlag, mData.get(position));
    }

    private void init() {
        if (!mData.isEmpty()) {
            return;
        }
        mData.add(addPhotoFlag);
    }

    /**
     * 选择的图片更新
     *
     * @param urls
     */
    public void updateUrlList(List<String> urls) {
        if (urls == null) return;
        mData.clear();
        mData.addAll(urls);
        if (mData.size() < PHOTOCOUNT) {
            mData.add(addPhotoFlag);
        }

        notifyDataSetChanged();
    }

    /**
     * 删除
     *
     * @param position
     */
    public void removeItem(int position) {
        if (position < 0 || position == mData.size()) return;

        if (TextUtils.equals(mData.get(position), addPhotoFlag)) {
            return;
        }
        remove(position);

        if (!isAddPhoto(mData.size() - 1)) {
            addData(addPhotoFlag);
        }
    }

    /**
     * 获取用户选择的图片路径
     *
     * @return
     */
    public ArrayList<String> getSelectUrlList() {

        ArrayList<String> mUrls = new ArrayList<>();

        for (String mDatum : mData) {

            if (TextUtils.equals(addPhotoFlag, mDatum)) {
                continue;
            }
            mUrls.add(mDatum);
        }

        return mUrls;
    }


}
