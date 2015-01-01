package com.androidmpgtracker.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.dao.VehiclesDao;

/**
 * Created by joe on 12/28/14.
 */
public class DataTransferProvider extends ContentProvider {
    public static final String AUTHORITY_LITE = "com.androidmpgtracker.lite.DATA_TRANSFER";
    public static final String AUTHORITY_PAID = "com.androidmpgtracker.DATA_TRANSFER";
    public static final String CARS_LITE_URI = AUTHORITY_LITE + VehiclesDao.TABLE_NAME;
    public static final String CARS_PAID_URI = AUTHORITY_PAID + VehiclesDao.TABLE_NAME;
    public static final String LOGS_LITE_URI = AUTHORITY_LITE + FillUpsDao.TABLE_NAME;
    public static final String LOGS_PAID_URI = AUTHORITY_PAID + FillUpsDao.TABLE_NAME;

    public static final String DATA_TRANSFER_PERMISSION = "com.androidmpgtracker.DATA_TRANSFER";

    public static final String[] CARS_PROJECTION = new String[]{VehiclesDao.COLUMN_ID, VehiclesDao.COLUMN_YEAR, VehiclesDao.COLUMN_MAKE, VehiclesDao.COLUMN_MODEL, VehiclesDao.COLUMN_TRIM, VehiclesDao.COLUMN_TRIM_ID, VehiclesDao.COLUMN_IS_CUSTOM};
    public static final String[] LOGS_PROJECTION = new String[]{FillUpsDao.COLUMN_ID, FillUpsDao.COLUMN_CAR_ID, FillUpsDao.COLUMN_DATE, FillUpsDao.COLUMN_MILES, FillUpsDao.COLUMN_GALLONS, FillUpsDao.COLUMN_PRICE_PER_GALLON, FillUpsDao.COLUMN_FULL_COST};

    private UriMatcher matcher;

    @Override
    public boolean onCreate() {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY_LITE, VehiclesDao.TABLE_NAME, 1);
        matcher.addURI(AUTHORITY_PAID, VehiclesDao.TABLE_NAME, 2);
        matcher.addURI(AUTHORITY_LITE, FillUpsDao.TABLE_NAME, 3);
        matcher.addURI(AUTHORITY_PAID, FillUpsDao.TABLE_NAME, 4);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        MpgDatabaseHelper dbHelper = new MpgDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (matcher.match(uri)) {
            case 1:
            case 2:
                result = db.query(VehiclesDao.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case 3:
            case 4:
                result = db.query(FillUpsDao.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }

        return result;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
