package com.androidmpgtracker.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmpgtracker.R;

public class VehicleView extends LinearLayout {

    public VehicleView(Context context, String text) {
        super(context);

        inflate(context, R.layout.view_vehicle_view, null);
        ((TextView)findViewById(R.id.text_view)).setText(text);
    }

    public void setText(String text) {
        ((TextView)findViewById(R.id.text_view)).setText(text);
    }
}
