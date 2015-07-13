package com.androidmpgtracker.activity;

import android.os.Bundle;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.flurry.android.FlurryAgent;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by joe on 12/26/14.
 */
public abstract class DashboardActivityBuildVar extends DashboardActivityRoot {

    @Override
    public void onStart() {
        super.onStart();
        if(MpgApplication.isUsageSharingAllowed()) {
            FlurryAgent.setCaptureUncaughtExceptions(true);
            FlurryAgent.onStartSession(this, getString(R.string.flurry_key));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
    }
}
