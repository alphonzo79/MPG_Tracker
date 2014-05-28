package com.androidmpgtracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.VehicleAdapter;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.CommunityMpg;
import com.androidmpgtracker.data.entities.FillUp;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.loader.GetCommunityMpgLoader;
import com.androidmpgtracker.view.MpgHistoryGraph;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ReportingActivity extends Activity {
    private Spinner vehicleSpinner;
    private TextView dateView, currentMpgView, currentMilesView, currentGallonsView, currentCostView,
                        currentCostPerMileView, gallonsYtdView, costYtdView, myAvgView, myAvgRecordsView,
                        communityAvgView, communityAvgRecordsView;
    private LinearLayout communityAverageLayout;
    private MpgHistoryGraph historyGraph;

    private VehicleAdapter vehicleAdapter;

    String currencySymbol = null;

    private LoaderManager.LoaderCallbacks<CommunityMpg> communityMpgLoaderCallbacks;
    private final int COMMUNITY_MPG_LOADER_ID = 107;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_fill_up);

        vehicleSpinner = (Spinner)findViewById(R.id.car_spinner);
        dateView = (TextView)findViewById(R.id.date);
        currentMpgView = (TextView)findViewById(R.id.last_mpg);
        currentMilesView = (TextView)findViewById(R.id.last_miles);
        currentGallonsView = (TextView)findViewById(R.id.last_gallons);
        currentCostView = (TextView)findViewById(R.id.last_cost_per_gallon);
        currentCostPerMileView = (TextView)findViewById(R.id.last_cost_per_mile);
        gallonsYtdView = (TextView)findViewById(R.id.gallons_ytd);
        costYtdView = (TextView)findViewById(R.id.cost_ytd);
        myAvgView = (TextView)findViewById(R.id.my_average);
        myAvgRecordsView = (TextView)findViewById(R.id.my_record_count);
        communityAvgView = (TextView)findViewById(R.id.community_average);
        communityAvgRecordsView = (TextView)findViewById(R.id.community_record_count);
        communityAverageLayout = (LinearLayout)findViewById(R.id.community_average_layout);
        historyGraph = (MpgHistoryGraph)findViewById(R.id.history_graph);

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList.add(new Vehicle());
        vehicleAdapter = new VehicleAdapter(this, vehicleList);
        vehicleSpinner.setAdapter(vehicleAdapter);
        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(vehicleAdapter != null) {
                    Vehicle vehicle = vehicleAdapter.getItem(i);
                    if(vehicle != null && vehicle.getId() != null) {
                        new GetLastFillupAsync().execute(vehicle.getId());
                        new GetYtdData().execute(vehicle.getId());
                        new GetMpgHistory().execute(vehicle.getId());

                        if(vehicle.getTrimId() != null && vehicle.getTrimId() > 0) {
                            getCommunityData(vehicle.getTrimId());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            currencySymbol = Currency.getInstance(Locale.getDefault()).getSymbol();
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            currencySymbol = "$";
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, List<Vehicle>>() {

            @Override
            protected List<Vehicle> doInBackground(Void... voids) {
                VehiclesDao dao = new VehiclesDao(ReportingActivity.this);
                return dao.getAllVehicles();
            }

            @Override
            protected void onPostExecute(List<Vehicle> vehicleList) {
                if(vehicleList != null && vehicleList.size() > 0) {
                    vehicleAdapter.setDataList(vehicleList);
                    vehicleAdapter.notifyDataSetChanged();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportingActivity.this);
                    builder.setMessage(R.string.must_have_a_vehilce);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent saveCar = new Intent(ReportingActivity.this, SettingsActivity.class);
                            saveCar.putExtra("com.androidmpgtracker.setupvehicle", true);
                            startActivity(saveCar);
                            dialogInterface.dismiss();
                            ReportingActivity.this.finish();
                        }
                    });
                    builder.show();
                }
            }
        }.execute();
    }

    private class GetLastFillupAsync extends AsyncTask<Long, Void, FillUp> {

        @Override
        protected FillUp doInBackground(Long... longs) {
            FillUpsDao dao = new FillUpsDao(ReportingActivity.this);
            return dao.getMostRecentFillUp(longs[0]);
        }

        @Override
        protected void onPostExecute(FillUp fillup) {
            //todo
        }
    }

    private class GetYtdData extends AsyncTask<Long, Void, List<FillUp>> {

        @Override
        protected List<FillUp> doInBackground(Long... longs) {
            //todo
            return null;
        }

        @Override
        protected void onPostExecute(List<FillUp> fillups) {
            //todo
        }
    }

    private class GetMpgHistory extends AsyncTask<Long, Void, List<FillUp>> {

        @Override
        protected List<FillUp> doInBackground(Long... longs) {
            FillUpsDao dao = new FillUpsDao(ReportingActivity.this);
            List<FillUp> fillups = dao.getRecentFillUps(longs[0], 20, true);

            //we got the most recent first so it would return the most recent 20.
            //however, we want them in chronological order. Now that we have the list
            //let's reverse it.
            List<FillUp> reversed = new ArrayList<FillUp>(fillups.size());
            for(int i = fillups.size() - 1; i >= 0; i--) {
                reversed.add(fillups.get(i));
            }

            return reversed;
        }

        @Override
        protected void onPostExecute(List<FillUp> fillups) {
            //todo
        }
    }

    private void getCommunityData(Long vehicleTrimId) {
        if(communityMpgLoaderCallbacks == null) {
            communityMpgLoaderCallbacks = new LoaderManager.LoaderCallbacks<CommunityMpg>() {
                @Override
                public Loader<CommunityMpg> onCreateLoader(int id, Bundle args) {
                    GetCommunityMpgLoader loader = new GetCommunityMpgLoader(ReportingActivity.this);
                    loader.setVehicleTrimId(args.getLong("trimId"));

                    return loader;
                }

                @Override
                public void onLoadFinished(Loader<CommunityMpg> loader, CommunityMpg data) {
                    if(loader.getId() == COMMUNITY_MPG_LOADER_ID && data != null) {
                        //todo
                    }
                }

                @Override
                public void onLoaderReset(Loader<CommunityMpg> loader) {

                }
            };
        }

        Bundle args = new Bundle();
        args.putLong("trimId", vehicleTrimId);
        if(getLoaderManager().getLoader(COMMUNITY_MPG_LOADER_ID) == null) {
//            getLoaderManager().initLoader(COMMUNITY_MPG_LOADER_ID, args, communityMpgLoaderCallbacks);
        } else {
//            getLoaderManager().restartLoader(COMMUNITY_MPG_LOADER_ID, args, communityMpgLoaderCallbacks);
        }
        //todo
    }
}
