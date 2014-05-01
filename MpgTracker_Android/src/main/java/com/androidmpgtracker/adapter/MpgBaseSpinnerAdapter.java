package com.androidmpgtracker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.androidmpgtracker.R;

import java.util.List;

public abstract class MpgBaseSpinnerAdapter<T> extends BaseAdapter {
    protected List<T> dataList;
    protected Context context;

    public MpgBaseSpinnerAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if(dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public T getItem(int i) {
        if(dataList != null && dataList.size() > i && dataList.get(i) != null) {
            return dataList.get(i);
        } else {
            return null;
        }
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_cell, null);
        }

        ((TextView)convertView.findViewById(R.id.spinner_text_view)).setText(getDisplayString(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_cell, null);
        }

        ((TextView)convertView.findViewById(R.id.spinner_text_view)).setText(getDisplayString(position));

        return convertView;
    }

    protected abstract String getDisplayString(int position);
}
