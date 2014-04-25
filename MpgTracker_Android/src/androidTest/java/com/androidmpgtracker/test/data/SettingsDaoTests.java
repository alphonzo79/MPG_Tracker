package com.androidmpgtracker.test.data;

import android.database.sqlite.SQLiteDatabase;

import com.androidmpgtracker.data.dao.SettingsDao;

public class SettingsDaoTests extends SimpleDashboardInstrumentedBase {
    private SettingsDao mSettingsDao;
    private boolean startingPointUsage;
    private boolean startingPointData;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mSettingsDao = new SettingsDao(mActivity);

        startingPointUsage = mSettingsDao.getAllowUsageSharing();
        startingPointData = mSettingsDao.getAllowDataSharing();
    }

    @Override
    public void tearDown() throws Exception {
        mSettingsDao.setAllowUsageSharing(startingPointUsage);
        mSettingsDao.setAllowDataSharing(startingPointData);
        super.tearDown();
    }

    public void testGetSetAllowUsageSharing() {
        //Empty returns true
        SQLiteDatabase dao = mSettingsDao.getWritableDatabase();
        dao.execSQL(String.format("DELETE from %s WHERE %s = '%s'", mSettingsDao.TABLE_NAME, mSettingsDao.SETTING_NAME, mSettingsDao.ALLOW_USAGE_SHARING));
        boolean allowSharing = mSettingsDao.getAllowUsageSharing();
        assertTrue("Null did not return true as expected", allowSharing);

        //Set and get
        boolean success = mSettingsDao.setAllowUsageSharing(false);
        assertTrue("The set to false did not succeed", success);
        allowSharing = mSettingsDao.getAllowUsageSharing();
        assertFalse("Did not return false after setting as expected", allowSharing);

        success = mSettingsDao.setAllowUsageSharing(true);
        assertTrue("The set to true did not succeed", success);
        allowSharing = mSettingsDao.getAllowUsageSharing();
        assertTrue("Did not return true after setting as expected", allowSharing);
    }

    public void testGetSetAllowDataSharing() {
        //Empty returns true
        SQLiteDatabase dao = mSettingsDao.getWritableDatabase();
        dao.execSQL(String.format("DELETE from %s WHERE %s = '%s'", mSettingsDao.TABLE_NAME, mSettingsDao.SETTING_NAME, mSettingsDao.ALLOW_DATA_SHARING));
        boolean allowSharing = mSettingsDao.getAllowDataSharing();
        assertTrue("Null did not return true as expected", allowSharing);

        //Set and get
        boolean success = mSettingsDao.setAllowDataSharing(false);
        assertTrue("The set to false did not succeed", success);
        allowSharing = mSettingsDao.getAllowDataSharing();
        assertFalse("Did not return false after setting as expected", allowSharing);

        success = mSettingsDao.setAllowDataSharing(true);
        assertTrue("The set to true did not succeed", success);
        allowSharing = mSettingsDao.getAllowDataSharing();
        assertTrue("Did not return true after setting as expected", allowSharing);
    }
}
