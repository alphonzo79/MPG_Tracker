package com.androidmpgtracker.data.entities;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidmpgtracker.R;

public class MpgDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AndroidMpgTrackerDB";
    private static final int VERSION = 1;
    Context mContext;

    public MpgDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();

        try {
            sqLiteDatabase.execSQL(mContext.getString(R.string.create_settings_table));
            sqLiteDatabase.execSQL(mContext.getString(R.string.create_cars_table));
            sqLiteDatabase.execSQL(mContext.getString(R.string.create_fill_ups_table));

            sqLiteDatabase.setTransactionSuccessful();
        } catch(SQLiteException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //todo later
    }
}
