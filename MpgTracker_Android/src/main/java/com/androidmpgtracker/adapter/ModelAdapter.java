package com.androidmpgtracker.adapter;

import android.content.Context;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.entities.EdmundsModel;

import java.util.List;

public class ModelAdapter extends MpgBaseSpinnerAdapter<EdmundsModel> {
    public ModelAdapter(Context context, List<EdmundsModel> dataList) {
        super(context, dataList);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    protected String getDisplayString(int position) {
        if(dataList != null && dataList.size() > position && dataList.get(position) != null) {
            return dataList.get(position).getName();
        } else {
            return context.getString(R.string.select);
        }
    }
}
