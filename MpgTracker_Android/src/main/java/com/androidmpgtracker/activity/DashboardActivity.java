package com.androidmpgtracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.androidmpgtracker.utils.FlurryEvents;
import com.flurry.android.FlurryAgent;

public class DashboardActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.view_reports_button).setOnClickListener(this);
        findViewById(R.id.log_fill_up_button).setOnClickListener(this);
        findViewById(R.id.settings_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MpgApplication.isUsageSharingAllowed()) {
            FlurryAgent.setCaptureUncaughtExceptions(true);
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

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.log_fill_up_button:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.LOG_FILL_UP_VISITED);
                }
                startActivity(new Intent(this, LogFillUpActivity.class));
                break;
            case R.id.view_reports_button:
                //todo
                break;
            case R.id.settings_button:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.SETTINGS_VISITED);
                }
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }
}
