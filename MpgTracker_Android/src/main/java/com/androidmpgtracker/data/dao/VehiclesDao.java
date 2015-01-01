package com.androidmpgtracker.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.utils.FlurryEvents;
import com.androidmpgtracker.data.MpgDatabaseHelper;
import com.androidmpgtracker.data.entities.Vehicle;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.List;

public class VehiclesDao extends MpgDatabaseHelper {

    //database constants
    public static final String TABLE_NAME = "my_cars";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MAKE = "make";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_TRIM = "trim";
    public static final String COLUMN_TRIM_ID = "trim_id";
    public static final String COLUMN_IS_CUSTOM = "is_custom";

    public VehiclesDao(Context context) {
        super(context);
    }

    public boolean saveVehicle(Vehicle vehicle) {
        if(vehicle.getId() != null) {
            return updateVehicle(vehicle);
        } else {
            return insertVehicle(vehicle);
        }
    }

    private boolean insertVehicle(Vehicle vehicle) {
        boolean success = false;

        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement sqlStatement = db.compileStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)", TABLE_NAME, COLUMN_YEAR, COLUMN_MAKE, COLUMN_MODEL, COLUMN_TRIM, COLUMN_TRIM_ID, COLUMN_IS_CUSTOM));
        if(vehicle.getYear() != null) {
            sqlStatement.bindLong(1, vehicle.getYear());
        }
        if(vehicle.getMake() != null) {
            sqlStatement.bindString(2, vehicle.getMake());
        }
        if(vehicle.getModel() != null) {
            sqlStatement.bindString(3, vehicle.getModel());
        }
        if(vehicle.getTrim() != null) {
            sqlStatement.bindString(4, vehicle.getTrim());
        }
        if(vehicle.getTrimId() != null) {
            sqlStatement.bindLong(5, vehicle.getTrimId());
        }
        String isCustomString = Boolean.toString(false);
        if(vehicle.getIsCustom() != null) {
            isCustomString = String.valueOf(vehicle.getIsCustom());
        }
        sqlStatement.bindString(6, isCustomString);

        db.beginTransaction();
        try
        {
            sqlStatement.execute();
            db.setTransactionSuccessful();
            success = true;
            if(MpgApplication.isUsageSharingAllowed()) {
                FlurryAgent.logEvent(FlurryEvents.VEHICLE_SAVED);
            }
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

    public boolean insertVehicleFromTransfer(long id, int year, String make, String model, String trim, int trimId, String isCustom) {
        boolean success = false;

        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement sqlStatement = db.compileStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)", TABLE_NAME, COLUMN_ID, COLUMN_YEAR, COLUMN_MAKE, COLUMN_MODEL, COLUMN_TRIM, COLUMN_TRIM_ID, COLUMN_IS_CUSTOM));
        sqlStatement.bindLong(1, id);
        sqlStatement.bindLong(2, year);
        sqlStatement.bindString(3, make);
        sqlStatement.bindString(4, model);
        sqlStatement.bindString(5, trim);
        sqlStatement.bindLong(6, trimId);
        sqlStatement.bindString(7, isCustom);

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

    private boolean updateVehicle(Vehicle vehicle) {
        boolean success = false;

        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement sqlStatement = db.compileStatement(String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?", TABLE_NAME, COLUMN_YEAR, COLUMN_MAKE, COLUMN_MODEL, COLUMN_TRIM, COLUMN_TRIM_ID, COLUMN_IS_CUSTOM, COLUMN_ID));
        if(vehicle.getYear() != null) {
            sqlStatement.bindLong(1, vehicle.getYear());
        }
        if(vehicle.getMake() != null) {
            sqlStatement.bindString(2, vehicle.getMake());
        }
        if(vehicle.getModel() != null) {
            sqlStatement.bindString(3, vehicle.getModel());
        }
        if(vehicle.getTrim() != null) {
            sqlStatement.bindString(4, vehicle.getTrim());
        }
        if(vehicle.getTrimId() != null) {
            sqlStatement.bindLong(5, vehicle.getTrimId());
        }
        String isCustomString = Boolean.toString(false);
        if(vehicle.getIsCustom() != null) {
            isCustomString = String.valueOf(vehicle.getIsCustom());
        }
        sqlStatement.bindString(6, isCustomString);
        sqlStatement.bindLong(7, vehicle.getId());

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

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> result = new ArrayList<Vehicle>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{COLUMN_ID, COLUMN_YEAR, COLUMN_MAKE, COLUMN_MODEL, COLUMN_TRIM, COLUMN_TRIM_ID, COLUMN_IS_CUSTOM};
        Cursor cursor = db.query(TABLE_NAME, columns, COLUMN_ID + " IS NOT NULL", null, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            try {
                while(cursor.moveToNext()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(cursor.getLong(0));
                    vehicle.setYear(((int)cursor.getLong(1)));
                    vehicle.setMake(cursor.getString(2));
                    vehicle.setModel(cursor.getString(3));
                    vehicle.setTrim(cursor.getString(4));
                    vehicle.setTrimId(cursor.getLong(5));
                    vehicle.setIsCustom(Boolean.valueOf(cursor.getString(6)));

                    result.add(vehicle);
                }
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

    public Vehicle getVehicle(long carId) {
        Vehicle result = null;

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{COLUMN_ID, COLUMN_YEAR, COLUMN_MAKE, COLUMN_MODEL, COLUMN_TRIM, COLUMN_TRIM_ID, COLUMN_IS_CUSTOM};
        Cursor cursor = db.query(TABLE_NAME, columns, COLUMN_ID + "=?", new String[]{String.valueOf(carId)}, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                result = new Vehicle();
                result.setId(cursor.getLong(0));
                result.setYear(((int)cursor.getLong(1)));
                result.setMake(cursor.getString(2));
                result.setModel(cursor.getString(3));
                result.setTrim(cursor.getString(4));
                result.setTrimId(cursor.getLong(5));
                result.setIsCustom(Boolean.valueOf(cursor.getString(6)));
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

    public boolean removeVehicle(Vehicle vehicle) {
        boolean success = false;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(vehicle.getId())});
            if(result > 0) {
                success = true;
            }
            db.setTransactionSuccessful();
            if(MpgApplication.isUsageSharingAllowed()) {
                FlurryAgent.logEvent(FlurryEvents.VEHICLE_DELETED);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return success;
    }
}
