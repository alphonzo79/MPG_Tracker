package com.androidmpgtracker.test.data;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.SystemClock;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.entities.FillUp;
import com.androidmpgtracker.utils.FlurryEvents;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.Calendar;
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
        toCleanUp.add(input);

        dao.saveFillUp(input.getCarId(), input.getMiles(), input.getGallons(), input.getPricePerGallon());
        
        SQLiteDatabase db = dao.getReadableDatabase();
        Cursor cursor = db.query(dao.TABLE_NAME, new String[]{dao.COLUMN_PRICE_PER_GALLON, dao.COLUMN_GALLONS, dao.COLUMN_MILES, dao.COLUMN_FULL_COST, dao.COLUMN_DATE}, dao.COLUMN_CAR_ID + "=?", new String[]{String.valueOf(input.getCarId())}, null, null, null);
        assertNotNull("The cursor returned from our query was null", cursor);
        FillUp found = null;
        float fullCost = 0;
        try{
            cursor.moveToFirst();
            found = new FillUp();
            found.setPricePerGallon(cursor.getFloat(0));
            found.setGallons(cursor.getFloat(1));
            found.setMiles(cursor.getFloat(2));
            fullCost = cursor.getFloat(3);
            found.setDate(cursor.getLong(4));
        } catch(SQLiteException e) {
            e.printStackTrace();
        }
        
        assertNotNull("We were not able to build up a found entity", found);
        assertEquals("The price per gallon did not match", input.getPricePerGallon(), found.getPricePerGallon());
        assertEquals("The gallons did not match", input.getGallons(), found.getGallons());
        assertEquals("The miles did not match", input.getMiles(), found.getMiles());
        
        float expected = input.getGallons() * input.getPricePerGallon();
        float diff = expected - fullCost;
        assertTrue("The full cost was not close enough to assume it is the same. Diff was" + diff, diff < 0.01f);
        
        Calendar today = Calendar.getInstance();
        Calendar foundDate = Calendar.getInstance();
        foundDate.setTimeInMillis(found.getDate());
        assertEquals("the year was not what we expected", today.get(Calendar.YEAR), foundDate.get(Calendar.YEAR));
        assertEquals("the month was not what we expected", today.get(Calendar.MONTH), foundDate.get(Calendar.MONTH));
        assertEquals("the day was not what we expected", today.get(Calendar.DAY_OF_MONTH), foundDate.get(Calendar.DAY_OF_MONTH));
    }

    public void testGetMostRecentFillUp() {
        FillUp input1 = new FillUp();
        input1.setCarId(System.currentTimeMillis());
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        dao.saveFillUp(input1.getCarId(), input1.getMiles(), input1.getGallons(), input1.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input2 = new FillUp();
        input2.setCarId(System.currentTimeMillis());
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        dao.saveFillUp(input2.getCarId(), input2.getMiles(), input2.getGallons(), input2.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input3 = new FillUp();
        input3.setCarId(System.currentTimeMillis());
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        dao.saveFillUp(input3.getCarId(), input3.getMiles(), input3.getGallons(), input3.getPricePerGallon());
        
        FillUp found = dao.getMostRecentFillUp();

        assertNotNull("We were not able to build up a found entity", found);
        assertEquals("The price per gallon did not match", input3.getPricePerGallon(), found.getPricePerGallon());
        assertEquals("The gallons did not match", input3.getGallons(), found.getGallons());
        assertEquals("The miles did not match", input3.getMiles(), found.getMiles());

        float expected = input3.getGallons() * input3.getPricePerGallon();
        float diff = expected - found.getTotalCost();
        assertTrue("The full cost was not close enough to assume it is the same. Diff was" + diff, diff < 0.01f);

        Calendar today = Calendar.getInstance();
        Calendar foundDate = Calendar.getInstance();
        foundDate.setTimeInMillis(found.getDate());
        assertEquals("the year was not what we expected", today.get(Calendar.YEAR), foundDate.get(Calendar.YEAR));
        assertEquals("the month was not what we expected", today.get(Calendar.MONTH), foundDate.get(Calendar.MONTH));
        assertEquals("the day was not what we expected", today.get(Calendar.DAY_OF_MONTH), foundDate.get(Calendar.DAY_OF_MONTH));
        assertEquals("The car id did not match", input3.getCarId(), found.getCarId());
    }

    public void testGetMostRecentFillUp_SpecificVehicle() {
        FillUp input1 = new FillUp();
        input1.setCarId(System.currentTimeMillis());
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        dao.saveFillUp(input1.getCarId(), input1.getMiles(), input1.getGallons(), input1.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input2 = new FillUp();
        input2.setCarId(System.currentTimeMillis());
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        dao.saveFillUp(input2.getCarId(), input2.getMiles(), input2.getGallons(), input2.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input3 = new FillUp();
        input3.setCarId(System.currentTimeMillis());
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        dao.saveFillUp(input3.getCarId(), input3.getMiles(), input3.getGallons(), input3.getPricePerGallon());

        FillUp found = dao.getMostRecentFillUp(input2.getCarId());

        assertNotNull("We were not able to build up a found entity", found);
        assertEquals("The price per gallon did not match", input2.getPricePerGallon(), found.getPricePerGallon());
        assertEquals("The gallons did not match", input2.getGallons(), found.getGallons());
        assertEquals("The miles did not match", input2.getMiles(), found.getMiles());

        float expected = input2.getGallons() * input2.getPricePerGallon();
        float diff = expected - found.getTotalCost();
        assertTrue("The full cost was not close enough to assume it is the same. Diff was" + diff, diff < 0.01f);

        Calendar today = Calendar.getInstance();
        Calendar foundDate = Calendar.getInstance();
        foundDate.setTimeInMillis(found.getDate());
        assertEquals("the year was not what we expected", today.get(Calendar.YEAR), foundDate.get(Calendar.YEAR));
        assertEquals("the month was not what we expected", today.get(Calendar.MONTH), foundDate.get(Calendar.MONTH));
        assertEquals("the day was not what we expected", today.get(Calendar.DAY_OF_MONTH), foundDate.get(Calendar.DAY_OF_MONTH));
        assertEquals("The car id did not match", input2.getCarId(), found.getCarId());
    }

    public void testGetRecentFillUps_All() {
        long carId = System.currentTimeMillis();

        FillUp input1 = new FillUp();
        input1.setCarId(carId);
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        dao.saveFillUp(input1.getCarId(), input1.getMiles(), input1.getGallons(), input1.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input2 = new FillUp();
        input2.setCarId(carId);
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        dao.saveFillUp(input2.getCarId(), input2.getMiles(), input2.getGallons(), input2.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input3 = new FillUp();
        input3.setCarId(carId);
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        dao.saveFillUp(input3.getCarId(), input3.getMiles(), input3.getGallons(), input3.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input4 = new FillUp();
        input4.setCarId(System.currentTimeMillis());
        input4.setPricePerGallon(3.53f);
        input4.setGallons(6.74f);
        input4.setMiles(156.4f);
        toCleanUp.add(input4);

        dao.saveFillUp(input4.getCarId(), input4.getMiles(), input4.getGallons(), input4.getPricePerGallon());

        List<FillUp> found = dao.getRecentFillUps(input1.getCarId(), -1, true);
        assertNotNull("The returned list was null", found);
        assertTrue("There were not three entries in the list", found.size() == 3);
        assertEquals("The first Entry was not the most recent as expected", input3.getGallons(), found.get(0).getGallons());
        assertEquals("The second Entry was not the one we expected", input2.getGallons(), found.get(1).getGallons());
        assertEquals("The third Entry was not the one we expected", input1.getGallons(), found.get(2).getGallons());
    }

    public void testGetRecentFillUps_Limits() {
        long carId = System.currentTimeMillis();

        FillUp input1 = new FillUp();
        input1.setCarId(carId);
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        dao.saveFillUp(input1.getCarId(), input1.getMiles(), input1.getGallons(), input1.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input2 = new FillUp();
        input2.setCarId(carId);
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        dao.saveFillUp(input2.getCarId(), input2.getMiles(), input2.getGallons(), input2.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input3 = new FillUp();
        input3.setCarId(carId);
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        dao.saveFillUp(input3.getCarId(), input3.getMiles(), input3.getGallons(), input3.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input4 = new FillUp();
        input4.setCarId(carId);
        input4.setPricePerGallon(1.43f);
        input4.setGallons(9.54f);
        input4.setMiles(176.45f);
        toCleanUp.add(input4);

        dao.saveFillUp(input4.getCarId(), input4.getMiles(), input4.getGallons(), input4.getPricePerGallon());

        List<FillUp> found = dao.getRecentFillUps(input1.getCarId(), 2, true);
        assertNotNull("The returned list was null", found);
        assertTrue("There were not two entries in the list", found.size() == 2);
        assertEquals("The first Entry was not the most recent as expected", input4.getGallons(), found.get(0).getGallons());
        assertEquals("The second Entry was not the one we expected", input3.getGallons(), found.get(1).getGallons());
    }

    public void testGetRecentFillUps_Ordering() {
        long carId = System.currentTimeMillis();

        FillUp input1 = new FillUp();
        input1.setCarId(carId);
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        dao.saveFillUp(input1.getCarId(), input1.getMiles(), input1.getGallons(), input1.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input2 = new FillUp();
        input2.setCarId(carId);
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        dao.saveFillUp(input2.getCarId(), input2.getMiles(), input2.getGallons(), input2.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input3 = new FillUp();
        input3.setCarId(carId);
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        dao.saveFillUp(input3.getCarId(), input3.getMiles(), input3.getGallons(), input3.getPricePerGallon());

        SystemClock.sleep(10);

        FillUp input4 = new FillUp();
        input4.setCarId(System.currentTimeMillis());
        input4.setPricePerGallon(3.53f);
        input4.setGallons(6.74f);
        input4.setMiles(156.4f);
        toCleanUp.add(input4);

        dao.saveFillUp(input4.getCarId(), input4.getMiles(), input4.getGallons(), input4.getPricePerGallon());

        List<FillUp> found = dao.getRecentFillUps(input1.getCarId(), -1, false);
        assertNotNull("The returned list was null", found);
        assertTrue("There were not three entries in the list", found.size() == 3);
        assertEquals("The first Entry was not the oldest as expected", input1.getGallons(), found.get(0).getGallons());
        assertEquals("The second Entry was not the one we expected", input2.getGallons(), found.get(1).getGallons());
        assertEquals("The third Entry was not the one we expected", input3.getGallons(), found.get(2).getGallons());
    }

    public void testGetFillupsYtd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 3);
        FillUp input1 = new FillUp();
        input1.setCarId(-234l);
        input1.setDate(cal.getTimeInMillis());
        input1.setPricePerGallon(3.45f);
        input1.setGallons(12.45f);
        input1.setMiles(256.4f);
        toCleanUp.add(input1);

        cal.add(Calendar.YEAR, -1);
        FillUp input2 = new FillUp();
        input2.setCarId(-234l);
        input2.setDate(cal.getTimeInMillis());
        input2.setPricePerGallon(2.35f);
        input2.setGallons(11.36f);
        input2.setMiles(226.6f);
        toCleanUp.add(input2);

        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 5);
        FillUp input3 = new FillUp();
        input3.setCarId(-234l);
        input3.setDate(cal.getTimeInMillis());
        input3.setPricePerGallon(3.53f);
        input3.setGallons(6.74f);
        input3.setMiles(156.4f);
        toCleanUp.add(input3);

        cal.set(Calendar.MONTH, 6);
        FillUp input4 = new FillUp();
        input4.setCarId(-234543l);
        input4.setDate(cal.getTimeInMillis());
        input4.setPricePerGallon(3.53f);
        input4.setGallons(6.74f);
        input4.setMiles(156.4f);
        toCleanUp.add(input4);

        SQLiteDatabase db = dao.getWritableDatabase();

        boolean success = false;
        try {
            db.beginTransaction();
            for(FillUp fillUp : toCleanUp) {
                SQLiteStatement stmt = db.compileStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?);", dao.TABLE_NAME, dao.COLUMN_CAR_ID, dao.COLUMN_DATE, dao.COLUMN_MILES, dao.COLUMN_GALLONS, dao.COLUMN_PRICE_PER_GALLON, dao.COLUMN_FULL_COST));
                stmt.bindLong(1, fillUp.getCarId());
                stmt.bindLong(2, fillUp.getDate());
                stmt.bindDouble(3, fillUp.getMiles());
                stmt.bindDouble(4, fillUp.getGallons());
                stmt.bindDouble(5, fillUp.getPricePerGallon());
                float totalCost = fillUp.getGallons() * fillUp.getPricePerGallon();
                stmt.bindDouble(6, totalCost);

                stmt.execute();
                stmt.close();
            }
            db.setTransactionSuccessful();
            success = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        assertTrue("The inputs failed, yo.", success);

        List<FillUp> found = dao.getFillUpsYtd(input1.getCarId(), true);

        assertNotNull("The returned list was null", found);
        assertTrue("There were not three entries in the list", found.size() == 2);
        assertEquals("The first Entry was not the most recent as expected", input3.getGallons(), found.get(0).getGallons());
        assertEquals("The second Entry was not the one we expected", input1.getGallons(), found.get(1).getGallons());
    }
}
