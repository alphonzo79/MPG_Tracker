package com.androidmpgtracker.test.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.entities.FillUp;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.List;

public class FillUpsDaoTests extends SimpleDashboardInstrumentedBase {
    private FillUpsDao dao;
    private List<FillUp> toCleanUp;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        dao = new FillUpsDao(mActivity);

        toCleanUp = new ArrayList<FillUp>();
    }

    @Override
    public void tearDown() throws Exception {
        SQLiteDatabase db = dao.getWritableDatabase();
        db.beginTransaction();
        try {
            for(FillUp fillUp : toCleanUp) {
                db.delete(dao.TABLE_NAME, dao.COLUMN_CAR_ID + "=?", new String[]{String.valueOf(fillUp.getCarId())});
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        super.tearDown();
    }

    public void testSaveFillUp() {
        FillUp input = new FillUp();
        input.setCarId(System.currentTimeMillis());
        input.setPricePerGallon(3.45f);
        input.setGallons(12.45f);
        input.setMiles(256.4f);

        dao.saveFillUp(input.getCarId(), input.getMiles(), input.getGallons(), input.getPricePerGallon());
        //todo
    }

    public void testGetMostRecentFillUp() {
        //todo
    }

    public void testGetMostRecentFillUp_SpecificVehicle() {
        //todo
    }

    public void testGetRecentFillUps_All() {
        //todo
    }

    public void testGetRecentFillUps_Limits() {
        //todo
    }

    public void testGetRecentFillUps_Ordering() {
        //todo
    }
}
