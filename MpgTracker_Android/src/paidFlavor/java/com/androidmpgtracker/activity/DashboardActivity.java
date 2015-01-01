package com.androidmpgtracker.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.DataTransferProvider;
import com.androidmpgtracker.data.MpgDatabaseHelper;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.dao.VehiclesDao;

/**
 * Created by joe on 12/31/14.
 */
public class DashboardActivity extends DashboardActivityBuildVar {
    protected String getCarsUri() {
        return DataTransferProvider.CARS_LITE_URI;
    }

    protected String getFillupsUri() {
        return DataTransferProvider.LOGS_LITE_URI;
    }
}
