package com.androidmpgtracker.adapter;

import android.content.Context;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.entities.EdmundsStyle;
import com.androidmpgtracker.data.entities.EdmundsSubmodel;

import java.util.List;

public class TrimAdapter extends MpgBaseSpinnerAdapter<EdmundsStyle> {
    public TrimAdapter(Context context, List<EdmundsStyle> dataList) {
        super(context, dataList);
    }

    @Override
    public long getItemId(int i) {
        if(dataList != null && dataList.size() > i && dataList.get(i) != null) {
            return dataList.get(i).getId();
        } else {
            return 0;
        }
    }

    @Override
    protected String getDisplayString(int position) {
        if(dataList != null && dataList.size() > position && dataList.get(position) != null) {
            return dataList.get(position).getName();
        } else {
            return context.getString(R.string.select);
        }
    }

    @Override
    public int indexOf(String displayString) {
        int index = 0;

        if(dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i) != null && dataList.get(i).getName() != null && dataList.get(i).getName().equals(displayString)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }
}
