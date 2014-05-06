package com.androidmpgtracker.test.data;

import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehiclesDaoTest extends SimpleDashboardInstrumentedBase {
    private VehiclesDao vehiclesDao;
    private List<Vehicle> toCleanup;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        vehiclesDao = new VehiclesDao(mActivity);

        toCleanup = new ArrayList<Vehicle>();
    }

    @Override
    public void tearDown() throws Exception {
        for(Vehicle vehicle : toCleanup) {
            vehiclesDao.removeVehicle(vehicle);
        }

        super.tearDown();
    }

    public void testAddNewVehicle_bareBones() {
        Vehicle bareVehicle = new Vehicle();
        bareVehicle.setYear(2005);
        bareVehicle.setMake("Fake Make" + System.currentTimeMillis());

        boolean success = vehiclesDao.saveVehicle(bareVehicle);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(bareVehicle.getYear()) && vehicle.getMake().equals(bareVehicle.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", foundVehicle);
        assertFalse("The vehicle did not default to not custom", foundVehicle.getIsCustom());
        assertNotNull("The returned vehicle did not have an _id", foundVehicle.getId());
    }

    public void testAddNewVehicle_fullData() {
        Vehicle input = new Vehicle();
        input.setYear(2005);
        input.setMake("Fake Make" + System.currentTimeMillis());
        input.setModel("Fake Model" + System.currentTimeMillis());
        input.setTrim("Fake Trim" + System.currentTimeMillis());
        input.setTrimId(System.currentTimeMillis());
        input.setIsCustom(false);

        boolean success = vehiclesDao.saveVehicle(input);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", foundVehicle);
        assertNotNull("The returned vehicle did not have an _id", foundVehicle.getId());
        assertEquals("The returned vehicle did not have the same model", input.getModel(), foundVehicle.getModel());
        assertEquals("The returned vehicle did not have the same trim", input.getTrim(), foundVehicle.getTrim());
        assertEquals("The returned vehicle did not have the same trim_id", input.getTrimId(), foundVehicle.getTrimId());
        assertFalse("The returned vehicle was set to custom", foundVehicle.getIsCustom());
    }

    public void testAddNewVehicle_custom() {
        Vehicle input = new Vehicle();
        input.setYear(2005);
        input.setMake("Fake Make" + System.currentTimeMillis());
        input.setModel("Fake Model" + System.currentTimeMillis());
        input.setTrim("Fake Trim" + System.currentTimeMillis());
        input.setTrimId( + System.currentTimeMillis());
        input.setIsCustom(true);

        boolean success = vehiclesDao.saveVehicle(input);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", foundVehicle);
        assertNotNull("The returned vehicle did not have an _id", foundVehicle.getId());
        assertEquals("The returned vehicle did not have the same model", input.getModel(), foundVehicle.getModel());
        assertEquals("The returned vehicle did not have the same trim", input.getTrim(), foundVehicle.getTrim());
        assertEquals("The returned vehicle did not have the same trim_id", input.getTrimId(), foundVehicle.getTrimId());
        assertTrue("The returned vehicle was not set to custom", foundVehicle.getIsCustom());
    }

    public void testUpdateVehicle() {
        Vehicle input = new Vehicle();
        input.setYear(2005);
        input.setMake("Fake Make" + System.currentTimeMillis());
        input.setModel("Fake Model" + System.currentTimeMillis());
        input.setTrim("Fake Trim" + System.currentTimeMillis());
        input.setTrimId( + System.currentTimeMillis());
        input.setIsCustom(false);

        boolean success = vehiclesDao.saveVehicle(input);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", foundVehicle);
        assertNotNull("The returned vehicle did not have an _id", foundVehicle.getId());
        assertEquals("The returned vehicle did not have the same model", input.getModel(), foundVehicle.getModel());
        assertEquals("The returned vehicle did not have the same trim", input.getTrim(), foundVehicle.getTrim());
        assertEquals("The returned vehicle did not have the same trim_id", input.getTrimId(), foundVehicle.getTrimId());
        assertFalse("The returned vehicle was set to custom", foundVehicle.getIsCustom());

        foundVehicle.setModel("NewFakeModel" + System.currentTimeMillis());
        foundVehicle.setTrimId(694022l);

        success = vehiclesDao.saveVehicle(foundVehicle);
        assertTrue("The vehicle did not save", success);

        foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle updated = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                updated = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", updated);
        assertNotNull("The returned vehicle did not have an _id", updated.getId());
        assertEquals("The returned vehicle did not have the same model", foundVehicle.getModel(), updated.getModel());
        assertEquals("The returned vehicle did not have the same trim", foundVehicle.getTrim(), updated.getTrim());
        assertEquals("The returned vehicle did not have the same trim_id", foundVehicle.getTrimId(), updated.getTrimId());
        assertFalse("The returned vehicle was set to custom", updated.getIsCustom());
    }

    public void testDeleteVehicle() {
        Vehicle input = new Vehicle();
        input.setYear(2005);
        input.setMake("Fake Make" + System.currentTimeMillis());
        input.setModel("Fake Model" + System.currentTimeMillis());
        input.setTrim("Fake Trim" + System.currentTimeMillis());
        input.setTrimId( + System.currentTimeMillis());
        input.setIsCustom(false);

        boolean success = vehiclesDao.saveVehicle(input);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        Vehicle foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNotNull("We did not find our vehicle in the list", foundVehicle);
        assertNotNull("The returned vehicle did not have an _id", foundVehicle.getId());
        assertEquals("The returned vehicle did not have the same model", input.getModel(), foundVehicle.getModel());
        assertEquals("The returned vehicle did not have the same trim", input.getTrim(), foundVehicle.getTrim());
        assertEquals("The returned vehicle did not have the same trim_id", input.getTrimId(), foundVehicle.getTrimId());
        assertFalse("The returned vehicle was set to custom", foundVehicle.getIsCustom());

        success = vehiclesDao.removeVehicle(foundVehicle);
        assertTrue("The vehicle did not delete", success);

        foundVehicles = vehiclesDao.getAllVehicles();
        foundVehicle = null;
        for(Vehicle vehicle : foundVehicles) {
            if(vehicle.getYear().equals(input.getYear()) && vehicle.getMake().equals(input.getMake())) {
                foundVehicle = vehicle;
                toCleanup.add(vehicle);
                break;
            }
        }
        assertNull("The removed vehicle was still in the database", foundVehicle);
    }

    public void testGetAllVehicles() {
        Vehicle bareVehicle = new Vehicle();
        bareVehicle.setYear(2005);
        bareVehicle.setMake("Fake Make" + System.currentTimeMillis());

        boolean success = vehiclesDao.saveVehicle(bareVehicle);
        assertTrue("The vehicle did not save", success);

        Vehicle bareVehicleTwo = new Vehicle();
        bareVehicleTwo.setYear(2001);
        bareVehicleTwo.setMake("Fake Make Two" + System.currentTimeMillis());

        success = vehiclesDao.saveVehicle(bareVehicleTwo);
        assertTrue("The vehicle did not save", success);

        Vehicle bareVehicleThree = new Vehicle();
        bareVehicleThree.setYear(1995);
        bareVehicleThree.setMake("Fake Make Three" + System.currentTimeMillis());

        success = vehiclesDao.saveVehicle(bareVehicleThree);
        assertTrue("The vehicle did not save", success);

        List<Vehicle> foundVehicles = vehiclesDao.getAllVehicles();
        int collectedVehicles = 0;
        assertTrue("We did not get any vehicles", foundVehicles.size() > 0);
        for(Vehicle vehicle : foundVehicles) {
            if((vehicle.getYear().equals(bareVehicle.getYear()) && vehicle.getMake().equals(bareVehicle.getMake())) ||
                    (vehicle.getYear().equals(bareVehicleTwo.getYear()) && vehicle.getMake().equals(bareVehicleTwo.getMake())) ||
                    (vehicle.getYear().equals(bareVehicleThree.getYear()) && vehicle.getMake().equals(bareVehicleThree.getMake()))) {
                collectedVehicles++;
                toCleanup.add(vehicle);
            }
        }

        assertEquals("There were not three vehicles as expected", 3, collectedVehicles);
    }
}
