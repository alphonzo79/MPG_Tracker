package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.androidmpgtracker.activity.SettingsActivity;
import com.androidmpgtracker.data.dao.SettingsDao;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.view.MpgSwitchView;
import com.flurry.android.FlurryAgent;

public class SettingsMainFragment extends Fragment implements View.OnClickListener {
    private View root;
    private LinearLayout vehicleLayout;
    private LinearLayout sharingLayout;
    SettingsActivity activity;

    private MpgSwitchView usageSwitch;
    private MpgSwitchView dataSwitch;

    boolean shareUsage = true;
    boolean shareData = true;

    //Since pausing and restoring a fragment (like in navigate away and back)
    //triggers another run through onCreateView, keep track of what was expanded
    private boolean vehiclesExpanded = false;
    private boolean sharingExpanded = false;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if(activity instanceof SettingsActivity) {
            this.activity = (SettingsActivity)activity;
        }

        new AsyncTask<Void, Void, Boolean[]>() {
            @Override
            protected Boolean[] doInBackground(Void... voids) {
                Boolean[] result = new Boolean[2];
                SettingsDao dao = new SettingsDao(activity);
                result[0] = dao.getAllowUsageSharing();
                result[1] = dao.getAllowDataSharing();

                return result;
            }

            @Override
            protected void onPostExecute(Boolean[] result) {
                shareUsage = result[0];
                shareData = result[1];
                Log.d("JAR", "onPostExecute");
                initializeSwitches();
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, null);

        root.findViewById(R.id.my_vehicles).setOnClickListener(this);
        root.findViewById(R.id.sharing_subhead).setOnClickListener(this);
        root.findViewById(R.id.about_subhead).setOnClickListener(this);

        vehicleLayout = (LinearLayout) root.findViewById(R.id.vehicle_container);
        if(vehiclesExpanded) {
            vehicleLayout.setVisibility(View.VISIBLE);
        }
        root.findViewById(R.id.add_vehicle).setOnClickListener(this);

        //todo vehicle setup stuff here

        sharingLayout = (LinearLayout) root.findViewById(R.id.sharing_container);
        if(sharingExpanded) {
            sharingLayout.setVisibility(View.VISIBLE);
        }
        usageSwitch = (MpgSwitchView) root.findViewById(R.id.usage_sharing_switch);
        usageSwitch.setOnClickListener(this);
        dataSwitch = (MpgSwitchView) root.findViewById(R.id.data_sharing_switch);
        dataSwitch.setOnClickListener(this);
        initializeSwitches();

        return root;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.my_vehicles:
                if(vehicleLayout.getVisibility() != View.VISIBLE) {
                    vehiclesExpanded = true;
                    vehicleLayout.setVisibility(View.VISIBLE);
                } else {
                    vehiclesExpanded = false;
                    vehicleLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.add_vehicle:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.ADD_VEHICLE_SELECTED);
                }
                activity.replaceContentFragment(new FragmentAddEditVehicle());
                break;
            case R.id.sharing_subhead:
                if(sharingLayout.getVisibility() != View.VISIBLE) {
                    sharingExpanded = true;
                    sharingLayout.setVisibility(View.VISIBLE);
                } else {
                    sharingExpanded = false;
                    sharingLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.data_sharing_switch:
                shareData = !shareData;
                dataSwitch.setSelected(shareData);
                if(MpgApplication.isUsageSharingAllowed()) {
                    if(shareData) {
                        FlurryAgent.logEvent(FlurryEvents.DATA_SHARING_ENABLED);
                    } else {
                        FlurryAgent.logEvent(FlurryEvents.DATA_SHARING_DISABLED);
                    }
                }
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(activity != null) {
                            new SettingsDao(activity).setAllowDataSharing(shareData);
                        }
                        return null;
                    }
                }.execute();
                break;
            case R.id.usage_sharing_switch:
                shareUsage = !shareUsage;
                usageSwitch.setSelected(shareUsage);
                if(MpgApplication.isUsageSharingAllowed() && !shareData) {
                    FlurryAgent.logEvent(FlurryEvents.USAGE_SHARING_DISABLED);
                } else if(shareData) {
                    FlurryAgent.logEvent(FlurryEvents.USAGE_SHARING_ENABLED);
                }
                MpgApplication.setUsageSharingAllowed(shareUsage);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(activity != null) {
                            new SettingsDao(activity).setAllowUsageSharing(shareUsage);
                        }
                        return null;
                    }
                }.execute();
                break;
            case R.id.about_subhead:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.ABOUT_FRAGMENT_VISITED);
                }
                activity.replaceContentFragment(new AboutFragment());
                break;
        }
    }

    void initializeSwitches() {
        if(dataSwitch != null && usageSwitch != null) {
            dataSwitch.setSelectedNoAnimate(shareData);
            usageSwitch.setSelectedNoAnimate(shareUsage);
        }
    }
}
