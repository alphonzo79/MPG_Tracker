package com.androidmpgtracker.adapter;

import android.content.Context;

import com.androidmpgtracker.data.entities.Vehicle;

import java.util.List;

public class VehicleAdapter extends MpgBaseSpinnerAdapter<Vehicle> {
    public VehicleAdapter(Context context, List<Vehicle> dataList) {
        super(context, dataList);
    }

    @Override
    protected String getDisplayString(int position) {
        if(dataList != null && dataList.size() > position && dataList.get(position) != null) {
            return dataList.get(position).getDisplayString();
        } else {
            return "";
        }
    }

    @Override
    public int indexOf(String displayString) {
        int index = 0;

        if(dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i) != null && dataList.get(i).getDisplayString() != null && dataList.get(i).getDisplayString().equals(displayString)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    @Override
    public long getItemId(int i) {
        if(dataList != null && dataList.size() > i && dataList.get(i) != null) {
            return dataList.get(i).getId() != null ? dataList.get(i).getId() : 0;
        } else {
            return 0;
        }
    }
}
