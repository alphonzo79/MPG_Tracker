package com.androidmpgtracker.activity;

import android.os.Bundle;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.flurry.android.FlurryAgent;
import com.crashlytics.android.Crashlytics;

/**
 * Created by joe on 12/26/14.
 */
public class DashboardActivity extends DashboardActivityRoot {

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
        Crashlytics.start(this);
        super.onCreate(savedInstanceState);
    }
}
