package com.androidmpgtracker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.VehicleAdapter;
import com.androidmpgtracker.data.dao.FillUpsDao;
import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.AverageMpg;
import com.androidmpgtracker.data.entities.FillUp;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.loader.GetCommunityMpgLoader;
import com.androidmpgtracker.view.MpgHistoryGraph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public abstract class ReportingActivityRoot extends FragmentActivity {
    private Spinner vehicleSpinner;
    private TextView dateView, currentMpgView, currentMilesView, currentGallonsView, currentCostView,
                        currentCostPerMileView, gallonsYtdView, costYtdView, myAvgView, myAvgRecordsView,
                        communityAvgView, communityAvgRecordsView;
    private LinearLayout communityAverageLayout;
    private MpgHistoryGraph historyGraph;

    private VehicleAdapter vehicleAdapter;
    private Vehicle selectedVehicle;
    boolean dialogShown = false;

    String currencySymbol = null;

    private LoaderManager.LoaderCallbacks<AverageMpg> communityMpgLoaderCallbacks;
    private final int COMMUNITY_MPG_LOADER_ID = 107;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reports);

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
                    selectedVehicle = vehicleAdapter.getItem(i);
                    if(selectedVehicle != null && selectedVehicle.getId() != null) {
                        new GetLastFillupAsync().execute(selectedVehicle.getId());

                        if(selectedVehicle.getTrimId() != null && selectedVehicle.getTrimId() > 0) {
                            getCommunityData(selectedVehicle.getTrimId());
                        } else {
                            communityAverageLayout.setVisibility(View.GONE);
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
                VehiclesDao dao = new VehiclesDao(ReportingActivityRoot.this);
                return dao.getAllVehicles();
            }

            @Override
            protected void onPostExecute(List<Vehicle> vehicleList) {
                if(vehicleList != null && vehicleList.size() > 0) {
                    vehicleAdapter.setDataList(vehicleList);
                    vehicleAdapter.notifyDataSetChanged();

                    if(getIntent() != null && getIntent().getSerializableExtra("loggedVehicle") != null) {
                        Vehicle vehicle = (Vehicle)getIntent().getSerializableExtra("loggedVehicle");

                        vehicleSpinner.setSelection(vehicleAdapter.indexOf(vehicle.getDisplayString()));
                    } else {
                        vehicleSpinner.setSelection(0);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportingActivityRoot.this);
                    builder.setMessage(R.string.must_have_a_vehilce);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent saveCar = new Intent(ReportingActivityRoot.this, SettingsActivity.class);
                            saveCar.putExtra("com.androidmpgtracker.setupvehicle", true);
                            startActivity(saveCar);
                            dialogInterface.dismiss();
                            ReportingActivityRoot.this.finish();
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
            FillUpsDao dao = new FillUpsDao(ReportingActivityRoot.this);
            return dao.getMostRecentFillUp(longs[0]);
        }

        @Override
        protected void onPostExecute(FillUp fillup) {
            if(fillup != null) {
                new GetYtdData().execute(selectedVehicle.getId());
                new GetMpgHistory().execute(selectedVehicle.getId());
                new GetAvgMpg().execute(selectedVehicle.getId());

                if(fillup.getDate() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(fillup.getDate());
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                    dateView.setText(sdf.format(cal.getTime()));
                }

                if(fillup.getGallons() != null && fillup.getGallons() != 0 && fillup.getMiles() != null && fillup.getMiles() != 0) {
                    float lastMpg = fillup.getMiles() / fillup.getGallons();
                    currentMpgView.setText(String.format("%01.2f", lastMpg));
                    currentMilesView.setText(String.format("%01.1f", fillup.getMiles()));
                    currentGallonsView.setText(String.format("%01.1f", fillup.getGallons()));

                    float costPerGallon = 0f;
                    float costPerMile = 0f;
                    if(fillup.getPricePerGallon() != null) {
                        costPerGallon = fillup.getPricePerGallon();
                    }
                    float totalCost = costPerGallon * fillup.getGallons();
                    costPerMile = totalCost / fillup.getMiles();
                    currentCostView.setText(String.format("%s%01.2f", currencySymbol, costPerGallon));
                    currentCostPerMileView.setText(String.format("%s%01.2f", currencySymbol, costPerMile));
                }
            } else {
                if(!dialogShown) {
                    dialogShown = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReportingActivityRoot.this);
                    builder.setMessage(R.string.no_logs_for_vehicle);
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent saveCar = new Intent(ReportingActivityRoot.this, LogFillUpActivity.class);
                            startActivity(saveCar);
                            dialogInterface.dismiss();
                            ReportingActivityRoot.this.finish();
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialogShown = false;
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dialogShown = false;
                        }
                    });
                    builder.show();
                }
            }
        }
    }

    private class GetAvgMpg extends AsyncTask<Long, Void, AverageMpg> {

        @Override
        protected AverageMpg doInBackground(Long... longs) {
            FillUpsDao dao = new FillUpsDao(ReportingActivityRoot.this);
            List<FillUp> fillups = dao.getRecentFillUps(longs[0], -1, true);

            AverageMpg result = new AverageMpg();
            result.setCount(0);
            result.setMpg(0f);

            if(fillups != null && fillups.size() > 0) {
                result.setCount(fillups.size());
                float runningMiles = 0f;
                float runningGallons = 0f;
                for(FillUp fillup : fillups) {
                    if(fillup.getMiles() != null) {
                        runningMiles += fillup.getMiles();
                    }
                    if(fillup.getGallons() != null) {
                        runningGallons += fillup.getGallons();
                    }
                }
                result.setMpg(runningMiles / runningGallons);
            }

            return result;
        }

        @Override
        protected void onPostExecute(AverageMpg result) {
            if(result.getCount() != null) {
                myAvgRecordsView.setText(String.format("(%d %s)", result.getCount(), getString(R.string.records)));
            }
            if(result.getMpg() != null) {
                myAvgView.setText(String.format("%01.2f %s", result.getMpg(), getString(R.string.mpg)));
            }
        }
    }

    private class GetYtdData extends AsyncTask<Long, Void, float[]> {

        @Override
        protected float[] doInBackground(Long... longs) {
            FillUpsDao dao = new FillUpsDao(ReportingActivityRoot.this);
            List<FillUp> fillups = dao.getFillUpsYtd(longs[0], true);

            float gallons = 0;
            float dollars = 0;
            if(fillups != null && fillups.size() > 0) {
                for(FillUp fillup : fillups) {
                    if(fillup.getGallons() != null) {
                        gallons += fillup.getGallons();
                    }
                    if(fillup.getTotalCost() != null) {
                        dollars += fillup.getTotalCost();
                    }
                }
            }

            return new float[]{gallons, dollars};
        }

        @Override
        protected void onPostExecute(float[] result) {
            gallonsYtdView.setText(String.format("%d", (int)result[0]));
            costYtdView.setText(String.format("%s%,d", currencySymbol, (int)result[1]));
        }
    }

    private class GetMpgHistory extends AsyncTask<Long, Void, List<FillUp>> {

        @Override
        protected List<FillUp> doInBackground(Long... longs) {
            FillUpsDao dao = new FillUpsDao(ReportingActivityRoot.this);
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
            historyGraph.setFillUps(fillups);
        }
    }

    private void getCommunityData(Long vehicleTrimId) {
        if(communityMpgLoaderCallbacks == null) {
            communityMpgLoaderCallbacks = new LoaderManager.LoaderCallbacks<AverageMpg>() {
                @Override
                public Loader<AverageMpg> onCreateLoader(int id, Bundle args) {
                    GetCommunityMpgLoader loader = new GetCommunityMpgLoader(ReportingActivityRoot.this);
                    loader.setVehicleTrimId(args.getLong("trimId"));

                    return loader;
                }

                @Override
                public void onLoadFinished(Loader<AverageMpg> loader, AverageMpg data) {
                    if(loader.getId() == COMMUNITY_MPG_LOADER_ID){
                        if(data != null) {
                            if (data.getCount() != null) {
                                communityAvgRecordsView.setText(String.format("(%d %s)", data.getCount(), getString(R.string.records)));
                            }
                            if (data.getMpg() != null) {
                                communityAvgView.setText(String.format("%01.2f %s", data.getMpg(), getString(R.string.mpg)));
                            }
                            communityAverageLayout.setVisibility(View.VISIBLE);
                        } else {
                            communityAverageLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onLoaderReset(Loader<AverageMpg> loader) {

                }
            };
        }

        Bundle args = new Bundle();
        args.putLong("trimId", vehicleTrimId);
        if(getSupportLoaderManager().getLoader(COMMUNITY_MPG_LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(COMMUNITY_MPG_LOADER_ID, args, communityMpgLoaderCallbacks);
        } else {
            getSupportLoaderManager().restartLoader(COMMUNITY_MPG_LOADER_ID, args, communityMpgLoaderCallbacks);
        }
    }
}
