package com.androidmpgtracker.test.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidmpgtracker.data.MpgDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpgDatabaseHelperTests extends SimpleDashboardInstrumentedBase {
    private MpgDatabaseHelper mDbHelper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mDbHelper = new MpgDatabaseHelper(mActivity);
    }

    public void testOnCreate() {
        //First drop all tables so we can start fresh
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        List<String> names = new ArrayList<String>(Arrays.asList(new String[]{"settings", "my_cars", "fill_ups"}));

        List<String> foundNames = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                foundNames.add(cursor.getString(0));
            }
        }

        for(String table : names) {
            assertTrue("The table " + table + " was not present before setup", foundNames.contains(table));
        }

        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS my_cars");
        db.execSQL("DROP TABLE IF EXISTS fill_ups");

        foundNames = new ArrayList<String>();
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                foundNames.add(cursor.getString(0));
            }
        }

        for(String table : names) {
            assertFalse("The table " + table + " was not deleted during setup", foundNames.contains(table));
        }

        //setup is complete. Create and make sure we have all tables, including those installed during updates
        mDbHelper.onCreate(db);

        Cursor dbCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'settings'", null);
        assertTrue("The settings table was not present", dbCursor.getCount() > 0);
        dbCursor.close();

        dbCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'my_cars'", null);
        assertTrue("The my_cars table was not present", dbCursor.getCount() > 0);
        dbCursor.close();

        dbCursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'fill_ups'", null);
        assertTrue("The fill_ups table was not present", dbCursor.getCount() > 0);
        dbCursor.close();
    }
}
