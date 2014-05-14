package com.androidmpgtracker.data.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.androidmpgtracker.data.MpgDatabaseHelper;
import com.androidmpgtracker.data.entities.FillUp;
import com.flurry.android.FlurryAgent;

import java.util.List;

public class FillUpsDao extends MpgDatabaseHelper {
    //database constants
    public static final String TABLE_NAME = "fill_ups";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAR_ID = "car_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MILES = "miles";
    public static final String COLUMN_GALLONS = "gallons";
    public static final String COLUMN_PRICE_PER_GALLON = "price_per_gallon";
    public static final String COLUMN_FULL_COST = "full_cost";

    public FillUpsDao(Context context) {
        super(context);
    }

    public boolean saveFillUp(long carId, float miles, float gallons, Float pricePerGallon) {
        boolean success = false;

        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stmt = db.compileStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?", TABLE_NAME, COLUMN_CAR_ID, COLUMN_DATE, COLUMN_MILES, COLUMN_GALLONS, COLUMN_PRICE_PER_GALLON, COLUMN_FULL_COST));
        stmt.bindLong(1, carId);
        stmt.bindLong(2, System.currentTimeMillis());
        stmt.bindDouble(3, miles);
        stmt.bindDouble(4, gallons);
        if(pricePerGallon != null) {
            stmt.bindDouble(5, pricePerGallon);
            float totalCost = gallons * pricePerGallon;
            stmt.bindDouble(6, totalCost);
        }

        db.beginTransaction();
        try
        {
            stmt.execute();
            db.setTransactionSuccessful();
            success = true;
            if(MpgApplication.isUsageSharingAllowed()) {
                FlurryAgent.logEvent(FlurryEvents.FILL_UP_LOGGED);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            db.endTransaction();
            stmt.close();
            db.close();
        }

        return success;
    }

    public FillUp getMostRecentFillUp() {
        //todo
        return null;
    }

    public FillUp getMostRecentFillUp(long carId) {
        //todo
        return null;
    }

    /**
     * Return a list of fillups for a given vehicle. Optionally, specify the maximum number of records to return
     * @param carId
     * @param count OPTIONAL. If you want all records pass in -1
     * @param mostRecentFirst
     * @return
     */
    public List<FillUp> getRecentFillUps(long carId, int count, boolean mostRecentFirst) {
        //todo
        return null;
    }
}
