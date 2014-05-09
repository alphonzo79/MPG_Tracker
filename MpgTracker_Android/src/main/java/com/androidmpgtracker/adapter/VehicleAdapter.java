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
            StringBuilder displayBuilder = new StringBuilder();
            if(dataList.get(position).getYear() != null) {
                displayBuilder.append(String.valueOf(dataList.get(position).getYear())).append(" ");
            }
            if(dataList.get(position).getMake() != null) {
                displayBuilder.append(dataList.get(position).getYear()).append(" ");
            }
            if(dataList.get(position).getModel() != null) {
                displayBuilder.append(dataList.get(position).getModel()).append(" ");
            }
            return displayBuilder.toString().trim();
        } else {
            return "";
        }
    }

    @Override
    public int indexOf(String displayString) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        if(dataList != null && dataList.size() > i && dataList.get(i) != null) {
            return dataList.get(i).getId();
        } else {
            return 0;
        }
    }
}
