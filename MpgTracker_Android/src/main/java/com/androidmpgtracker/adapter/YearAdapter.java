package com.androidmpgtracker.adapter;

import android.content.Context;

import java.util.List;

public class YearAdapter extends MpgBaseSpinnerAdapter<Integer> {
    public YearAdapter(Context context, List<Integer> dataList) {
        super(context, dataList);
    }

    @Override
    protected String getDisplayString(int position) {
        if(dataList != null && dataList.size() > position && dataList.get(position) != null) {
            return String.valueOf(dataList.get(position));
        } else {
            return "";
        }
    }

    @Override
    public long getItemId(int position) {
        if(dataList != null && dataList.size() > position && dataList.get(position) != null) {
            return position;
        } else {
            return 0;
        }
    }
}
