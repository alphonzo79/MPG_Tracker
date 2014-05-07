package com.androidmpgtracker.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.entities.Vehicle;

public class VehicleView extends LinearLayout {
    private Vehicle vehicle;

    public VehicleView(Context context, Vehicle vehicle) {
        super(context);

        this.vehicle = vehicle;

        inflate(context, R.layout.view_vehicle_view, null);

        if(vehicle != null) {
            TextView textView = (TextView)findViewById(R.id.year);
            if(vehicle.getYear() != null) {
                textView.setText(String.valueOf(vehicle.getYear()));
            } else {
                textView.setVisibility(GONE);
            }

            textView = (TextView)findViewById(R.id.make);
            if(vehicle.getMake() != null) {
                textView.setText(vehicle.getMake());
            } else {
                textView.setVisibility(GONE);
            }

            textView = (TextView)findViewById(R.id.model);
            if(vehicle.getModel() != null) {
                textView.setText(vehicle.getModel());
            } else {
                textView.setVisibility(GONE);
            }

            textView = (TextView)findViewById(R.id.trim);
            if(vehicle.getTrim() != null) {
                textView.setText(vehicle.getTrim());
            } else {
                textView.setVisibility(GONE);
            }
        }
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
