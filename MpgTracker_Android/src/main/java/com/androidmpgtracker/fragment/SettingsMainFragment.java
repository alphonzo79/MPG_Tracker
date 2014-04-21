package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidmpgtracker.R;
import com.androidmpgtracker.activity.SettingsActivity;

public class SettingsMainFragment extends Fragment implements View.OnClickListener {
    private LinearLayout vehicleLayout;
    private LinearLayout sharingLayout;
    private SettingsActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof SettingsActivity) {
            this.activity = (SettingsActivity)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, null);

        root.findViewById(R.id.my_vehicles).setOnClickListener(this);
        root.findViewById(R.id.sharing_subhead).setOnClickListener(this);
        root.findViewById(R.id.about_subhead).setOnClickListener(this);

        vehicleLayout = (LinearLayout)root.findViewById(R.id.vehicle_container);
        sharingLayout = (LinearLayout)root.findViewById(R.id.sharing_container);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.my_vehicles:
                if(vehicleLayout.getVisibility() != View.VISIBLE) {
                    vehicleLayout.setVisibility(View.VISIBLE);
                } else {
                    vehicleLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.sharing_subhead:
                if(sharingLayout.getVisibility() != View.VISIBLE) {
                    sharingLayout.setVisibility(View.VISIBLE);
                } else {
                    sharingLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.about_subhead:
                activity.replaceContentFragment(new AboutFragment());
                break;
        }
    }
}
