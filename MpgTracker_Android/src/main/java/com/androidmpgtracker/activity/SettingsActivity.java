package com.androidmpgtracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import android.support.v4.app.FragmentManager;

import com.androidmpgtracker.R;
import com.androidmpgtracker.fragment.SettingsMainFragment;

public class SettingsActivity extends FragmentActivity {

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

    public void replaceContentFragment(Fragment fragment) {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.content_container, fragment, null);
        trans.addToBackStack(null);
        trans.commit();
    }
}
