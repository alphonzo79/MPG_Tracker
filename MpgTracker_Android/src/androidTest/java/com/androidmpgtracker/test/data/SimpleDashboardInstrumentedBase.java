package com.androidmpgtracker.test.data;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import com.androidmpgtracker.activity.DashboardActivity;
import com.androidmpgtracker.data.entities.MpgDatabaseHelper;

public abstract class SimpleDashboardInstrumentedBase extends ActivityInstrumentationTestCase2<DashboardActivity> {
    protected DashboardActivity mActivity;
    protected Instrumentation mInstrumentation;

    public SimpleDashboardInstrumentedBase(Class<DashboardActivity> activityClass) {
        super(activityClass);
    }

    public SimpleDashboardInstrumentedBase() {
        this(DashboardActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mActivity = getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
