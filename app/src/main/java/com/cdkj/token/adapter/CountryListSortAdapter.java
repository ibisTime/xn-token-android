package com.cdkj.token.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdkj.token.R;

/**
 * Created by lei on 2017/8/26.
 */

public class CountryListSortAdapter extends BaseAdapter {

    private Context context;
    private String[] list;

    private ViewHolder holder;

    public CountryListSortAdapter(Context context, String[] list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_country_list_sort, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvSort.setText(list[i]);

        return view;
    }

    static class ViewHolder {
        TextView tvSort;

        ViewHolder(View view) {
            tvSort = view.findViewById(R.id.tv_sort);
        }
    }
}
