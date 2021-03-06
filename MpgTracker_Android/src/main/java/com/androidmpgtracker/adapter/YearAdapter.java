package com.androidmpgtracker.adapter;

import android.content.Context;

import com.androidmpgtracker.R;

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
            return context.getString(R.string.select);
        }
    }

    @Override
    public int indexOf(String displayString) {
        if(dataList != null) {
            return dataList.indexOf(Integer.valueOf(displayString));
        } else {
            return 0;
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
