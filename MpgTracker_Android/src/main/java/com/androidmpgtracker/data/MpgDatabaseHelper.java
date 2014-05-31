package com.androidmpgtracker.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.androidmpgtracker.R;
import com.androidmpgtracker.data.dao.SettingsDao;

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

        populateSettingTableDefulats(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //todo later
    }

    private void populateSettingTableDefulats(SQLiteDatabase db) {
        SQLiteStatement sqlStatementUsage = null;
        SQLiteStatement sqlStatementData = null;

        db.beginTransaction();
        try{
            sqlStatementUsage = db.compileStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", SettingsDao.TABLE_NAME, SettingsDao.SETTING_NAME, SettingsDao.SETTING_VALUE));
            sqlStatementUsage.bindString(1, SettingsDao.ALLOW_USAGE_SHARING);
            sqlStatementUsage.bindString(2, Boolean.toString(true));

            sqlStatementData = db.compileStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", SettingsDao.TABLE_NAME, SettingsDao.SETTING_NAME, SettingsDao.SETTING_VALUE));
            sqlStatementUsage.bindString(1, SettingsDao.ALLOW_DATA_SHARING);
            sqlStatementUsage.bindString(2, Boolean.toString(true));

            sqlStatementUsage.execute();
            sqlStatementData.execute();
            db.setTransactionSuccessful();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            if(sqlStatementData != null) {
                sqlStatementData.close();
            }
            if(sqlStatementUsage != null) {
                sqlStatementUsage.close();
            }
        }
    }
}
