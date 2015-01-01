package com.androidmpgtracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.R;
import com.androidmpgtracker.data.DataTransferProvider;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.FillUp;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.utils.FlurryEvents;
import com.flurry.android.FlurryAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public abstract class DashboardActivityRoot extends Activity implements View.OnClickListener {
    int reportClicks = 0;

    protected final String MPG_TRACKER_SHARED_PREFS = "com.androidmpgtracker.prefs";
    protected final String DATA_TRANSFER_DONE_SHARED_PREF = "data_transfer_done";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.view_reports_button).setOnClickListener(this);
        findViewById(R.id.log_fill_up_button).setOnClickListener(this);
        findViewById(R.id.settings_button).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(MPG_TRACKER_SHARED_PREFS, MODE_PRIVATE);
        boolean dataTransferDone = prefs.getBoolean(DATA_TRANSFER_DONE_SHARED_PREF, false);
        if(!dataTransferDone) {
            AlertDialog progressDialog = new AlertDialog.Builder(this).setCancelable(false).
                    setMessage(R.string.checking_for_existing_data).show();

            new AsyncTask<Object, Void, Void>() {
                AlertDialog dialog;
                SharedPreferences sharedPrefs;

                @Override
                protected Void doInBackground(Object... params) {
                    dialog = (AlertDialog)params[0];
                    sharedPrefs = (SharedPreferences)params[1];

                    ContentResolver resolver = getContentResolver();
                    Cursor vehicles = resolver.query(Uri.parse(getCarsUri()), DataTransferProvider.CARS_PROJECTION,
                            null, null, VehiclesDao.COLUMN_ID);

                    if(vehicles != null &&vehicles.getCount() > 0) {
                        publishProgress(null);

                        VehiclesDao vehiclesDao = new VehiclesDao(DashboardActivityRoot.this);
                        while(vehicles.moveToNext()) {
                            long id = vehicles.getLong(0);
                            int year = vehicles.getInt(1);
                            String make = vehicles.getString(2);
                            String model = vehicles.getString(3);
                            String trim = vehicles.getString(4);
                            int trimId = vehicles.getInt(5);
                            String isCustom = vehicles.getString(6);

                            vehiclesDao.insertVehicleFromTransfer(id, year, make, model, trim, trimId, isCustom);
                        }

                        vehicles.close();

                        Cursor fillups = resolver.query(Uri.parse(getFillupsUri()), DataTransferProvider.LOGS_PROJECTION,
                                null, null, FillUpsDao.COLUMN_ID);

                        if(fillups != null && fillups.getCount() > 0) {
                            FillUpsDao fillUpsDao = new FillUpsDao(DashboardActivityRoot.this);
                            while(fillups.moveToNext()) {
                                long carId = fillups.getLong(1);
                                long date = fillups.getLong(2);
                                float miles = fillups.getFloat(3);
                                float gallons = fillups.getFloat(4);
                                float pricePerGallon = fillups.getFloat(5);
                                float totalPrice = fillups.getFloat(6);

                                fillUpsDao.saveFillupFromTransfer(carId, date, miles, gallons, pricePerGallon, totalPrice);
                            }

                            fillups.close();
                        }
                    }

                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    dialog.setMessage(getString(R.string.transfering_data));
                }

                @Override
                protected void onPostExecute(Void result) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean(DATA_TRANSFER_DONE_SHARED_PREF, true);
                    editor.commit();

                    dialog.dismiss();
                }
            }.execute(progressDialog, prefs);
        }

        FillUpsDao fillupsDao = new FillUpsDao(this);
        VehiclesDao vehiclesDao = new VehiclesDao(this);
        FillUp mostRecent = fillupsDao.getMostRecentFillUp();
        if(mostRecent != null) {
            findViewById(R.id.we_got_data_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.no_data_layout).setVisibility(View.GONE);
            Vehicle car = vehiclesDao.getVehicle(mostRecent.getCarId());
            if(car != null) {
                ((TextView)findViewById(R.id.latest_data_car)).setText(car.getDisplayString());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(mostRecent.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
            ((TextView)findViewById(R.id.latest_data_date)).setText(sdf.format(cal.getTime()));

            float lastMpg = 0;
            if(mostRecent.getGallons() != null && mostRecent.getGallons() != 0 && mostRecent.getMiles() != null && mostRecent.getMiles() != 0) {
                lastMpg = mostRecent.getMiles() / mostRecent.getGallons();
            }
            ((TextView)findViewById(R.id.latest_data_mpg)).setText(String.format("%01.2f", lastMpg));

            findViewById(R.id.we_got_data_layout).setOnClickListener(this);
        } else {
            findViewById(R.id.we_got_data_layout).setVisibility(View.GONE);
            findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);

            List<Vehicle> cars = vehiclesDao.getAllVehicles();
            if(cars != null && cars.size() > 0) {
                ((TextView)findViewById(R.id.no_data_header)).setText(R.string.no_data_dashboard_header);
                ((TextView)findViewById(R.id.no_data_text)).setText(R.string.no_data_dashboard_text);
            } else {
                ((TextView)findViewById(R.id.no_data_header)).setText(R.string.no_cars_dashboard_header);
                ((TextView)findViewById(R.id.no_data_text)).setText(R.string.no_cars_dashboard_text);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(MpgApplication.isUsageSharingAllowed()) {
            FlurryAgent.onEndSession(this);
        }
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.log_fill_up_button:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.LOG_FILL_UP_VISITED);
                }
                startActivity(new Intent(this, LogFillUpActivity.class));
                break;
            case R.id.view_reports_button:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.VISIT_REPORTS_FROM_DASH);
                }
                startActivity(new Intent(this, ReportingActivity.class));
                break;
            case R.id.settings_button:
                if(MpgApplication.isUsageSharingAllowed()) {
                    FlurryAgent.logEvent(FlurryEvents.SETTINGS_VISITED);
                }
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.we_got_data_layout:
                reportClicks++;
                if(reportClicks == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.did_you_know);
                    builder.setMessage(R.string.furlongs_did_you_know);
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.lets_do_it, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            ((TextView)findViewById(R.id.latest_data_mpg_label)).setText(R.string.furlongs_per_pint);
                        }
                    });
                    builder.show();
                }
                break;
        }
    }

    protected abstract String getCarsUri();
    protected abstract String getFillupsUri();
}
