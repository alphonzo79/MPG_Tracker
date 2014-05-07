package com.androidmpgtracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import android.support.v4.app.FragmentManager;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.fragment.CustomVehicleFragment;
import com.androidmpgtracker.fragment.FragmentAddEditVehicle;
import com.androidmpgtracker.fragment.MpgFragmentListener;
import com.androidmpgtracker.fragment.SettingsMainFragment;
import com.flurry.android.FlurryAgent;

public class SettingsActivity extends FragmentActivity implements FragmentAddEditVehicle.AddEditVehicleListener, MpgFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        if(savedInstanceState == null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.content_container, new SettingsMainFragment(), null);
            trans.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MpgApplication.isUsageSharingAllowed()) {
            FlurryAgent.onStartSession(this, getString(R.string.flurry_key));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(MpgApplication.isUsageSharingAllowed()) {
            FlurryAgent.onEndSession(this);
        }
    }

    public void replaceContentFragment(Fragment fragment) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.content_container, fragment, null);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void goToCustomVehicleFragment(Vehicle vehicle) {
        getSupportFragmentManager().popBackStack();
        CustomVehicleFragment fragment = new CustomVehicleFragment();
        fragment.setVehicle(vehicle);
        replaceContentFragment(fragment);
    }

    @Override
    public void killFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
