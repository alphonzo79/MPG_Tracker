package com.androidmpgtracker.test.data;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.androidmpgtracker.activity.DashboardActivity;
import com.androidmpgtracker.data.entities.MpgDatabaseHelper;

public class MpgDatabaseHelperTests extends ActivityInstrumentationTestCase2<DashboardActivity> {
    private MpgDatabaseHelper mDbHelper;
    private DashboardActivity mActivity;

    public MpgDatabaseHelperTests(Class<DashboardActivity> activityClass) {
        super(activityClass);
    }

    public MpgDatabaseHelperTests() {
        this(DashboardActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getInstrumentation();
        mActivity = getActivity();
        mDbHelper = new MpgDatabaseHelper(mActivity);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnCreate() {
        assertTrue("This should fail", false);
    }
}
