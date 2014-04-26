package com.androidmpgtracker.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import com.androidmpgtracker.data.entities.MpgDatabaseHelper;

public class SettingsDao extends MpgDatabaseHelper {
    //database constants
    public static final String TABLE_NAME = "settings";
    public static final String SETTING_VALUE = "setting_value";
    public static final String SETTING_NAME = "setting_name";

    //setting name constants
    public static final String ALLOW_USAGE_SHARING = "allow_usage_sharing";
    public static final String ALLOW_DATA_SHARING = "allow_data_sharing";

    public SettingsDao(Context context) {
        super(context);
    }

    public boolean getAllowUsageSharing() {
        Boolean found = getAllowUsageSharing_boxed();
        if(found == null) {
            found = true;
        }

        return found;
    }

    private Boolean getAllowUsageSharing_boxed() {
        Boolean result = null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{SETTING_VALUE}, SETTING_NAME + "=?", new String[]{ALLOW_USAGE_SHARING}, null, null, null);
        if(cursor != null) {
            try {
                cursor.moveToFirst();
                String val = cursor.getString(0);
                result = Boolean.valueOf(val);
            } catch (SQLiteException e) {
                e.printStackTrace();
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }

    public boolean setAllowUsageSharing(boolean allowUsageSharing) {
        boolean success = false;

        if(getAllowUsageSharing_boxed() == null) {
            success = performInsert(ALLOW_USAGE_SHARING, Boolean.toString(allowUsageSharing));
        } else {
            success = performUpdate(ALLOW_USAGE_SHARING, Boolean.toString(allowUsageSharing));
        }

        return success;
    }

    public boolean getAllowDataSharing() {
        Boolean found = getAllowDataSharing_boxed();
        if(found == null) {
            found = true;
        }

        return found;
    }

    private Boolean getAllowDataSharing_boxed() {
        Boolean result = null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{SETTING_VALUE}, SETTING_NAME + "=?", new String[]{ALLOW_DATA_SHARING}, null, null, null);
        if(cursor != null) {
            try {
                cursor.moveToFirst();
                String val = cursor.getString(0);
                result = Boolean.valueOf(val);
            } catch (SQLiteException e) {
                e.printStackTrace();
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }

    public boolean setAllowDataSharing(boolean allowDataSharing) {
        boolean success = false;

        if(getAllowDataSharing_boxed() == null) {
            success = performInsert(ALLOW_DATA_SHARING, Boolean.toString(allowDataSharing));
        } else {
            success = performUpdate(ALLOW_DATA_SHARING, Boolean.toString(allowDataSharing));
        }

        return success;
    }

    private boolean performInsert(String settingName, String settingValue) {
        boolean success = false;

        SQLiteDatabase db = getReadableDatabase();

        SQLiteStatement sqlStatement = db.compileStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", TABLE_NAME, SETTING_NAME, SETTING_VALUE));
        sqlStatement.bindString(1, settingName);
        sqlStatement.bindString(2, settingValue);

        db.beginTransaction();
        try
        {
            sqlStatement.execute();
            db.setTransactionSuccessful();
            success = true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            db.endTransaction();
            sqlStatement.close();
            db.close();
        }

        return success;
    }

    private boolean performUpdate(String settingName, String settingValue) {
        boolean success = false;

        SQLiteDatabase db = getReadableDatabase();

        SQLiteStatement sqlStatement = db.compileStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", TABLE_NAME, SETTING_VALUE, SETTING_NAME));
        sqlStatement.bindString(1, settingValue);
        sqlStatement.bindString(2, settingName);

        db.beginTransaction();
        try
        {
            sqlStatement.execute();
            db.setTransactionSuccessful();
            success = true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            db.endTransaction();
            sqlStatement.close();
            db.close();
        }

        return success;
    }
}
