package com.androidmpgtracker;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.androidmpgtracker.data.dao.SettingsDao;

public class MpgApplication extends Application {
    private static boolean usageSharingAllowed = false;

    @Override
    public void onCreate() {
        super.onCreate();

        SettingsDao dao = new SettingsDao(this);
        usageSharingAllowed = dao.getAllowUsageSharing();
    }

    public static void setUsageSharingAllowed(boolean usageSharingAllowed) {
        MpgApplication.usageSharingAllowed = usageSharingAllowed;
    }

    public static boolean isUsageSharingAllowed() {
        return usageSharingAllowed;
    }
}
