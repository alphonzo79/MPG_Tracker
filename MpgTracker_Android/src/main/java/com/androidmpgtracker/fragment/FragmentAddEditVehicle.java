package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.MakeAdapter;
import com.androidmpgtracker.adapter.ModelAdapter;
import com.androidmpgtracker.adapter.TrimAdapter;
import com.androidmpgtracker.adapter.YearAdapter;
import com.androidmpgtracker.data.entities.EdmundsMake;
import com.androidmpgtracker.data.entities.EdmundsModel;
import com.androidmpgtracker.data.entities.EdmundsStyle;
import com.androidmpgtracker.data.entities.EdmundsYear;
import com.androidmpgtracker.data.entities.Vehicle;
import com.androidmpgtracker.loader.YearMakesLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentAddEditVehicle extends Fragment implements View.OnClickListener {
    private Vehicle vehicle;

    private Spinner yearSpinner;
    private YearAdapter yearAdapter;

    private Spinner makeSpinner;
    private MakeAdapter makeAdapter;

    private Spinner modelSpinner;
    private ModelAdapter modelAdapter;

    private Spinner trimSpinner;
    private TrimAdapter trimAdapter;

    private Activity activity;

    private Button saveButton;

    private LoaderCallbacks<EdmundsYear> yearMakeLoaderCallbacks;
    private final int YEAR_MAKE_LOADER_ID = 45;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_vehicle, null);

        yearSpinner = (Spinner)root.findViewById(R.id.year_spinner);
        setupYearAdapter();
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //reset and disable the lower spinners
                setupMakeAdapter(null);
                makeSpinner.setEnabled(false);

                setupModelAdapter(null);
                modelSpinner.setEnabled(false);

                setupTrimAdapter(null);
                trimSpinner.setEnabled(false);

                saveButton.setEnabled(false);

                getMakesForYear(yearAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        makeSpinner = (Spinner)root.findViewById(R.id.make_spinner);
        setupMakeAdapter(null);
        makeSpinner.setAdapter(makeAdapter);
        makeSpinner.setEnabled(false);

        modelSpinner = (Spinner)root.findViewById(R.id.model_spinner);
        setupModelAdapter(null);
        modelSpinner.setAdapter(modelAdapter);
        modelSpinner.setEnabled(false);

        trimSpinner = (Spinner)root.findViewById(R.id.trim_spinner);
        setupTrimAdapter(null);
        trimSpinner.setAdapter(trimAdapter);
        trimSpinner.setEnabled(false);

        saveButton = (Button)root.findViewById(R.id.save_car_button);
        saveButton.setEnabled(false);

        return root;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        //todo more
    }

    private void setupYearAdapter() {
        List<String> yearList = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = currentYear; i >= 1990; i--) {
            yearList.add(String.valueOf(i));
        }
        yearAdapter = new YearAdapter(activity, yearList);
    }

    private void setupMakeAdapter(List<EdmundsMake> dataList) {
        if(dataList == null) {
            dataList = new ArrayList<EdmundsMake>();
            dataList.add(EdmundsMake.getBareEntity());
        }

        if(makeAdapter == null) {
            makeAdapter = new MakeAdapter(activity, dataList);
        } else {
            makeAdapter.setDataList(dataList);
            makeAdapter.notifyDataSetChanged();
        }
    }

    private void setupModelAdapter(List<EdmundsModel> dataList) {
        if(dataList == null) {
            dataList = new ArrayList<EdmundsModel>();
            dataList.add(EdmundsModel.getBareEntity());
        }

        if(modelAdapter == null) {
            modelAdapter = new ModelAdapter(activity, dataList);
        } else {
            modelAdapter.setDataList(dataList);
            modelAdapter.notifyDataSetChanged();
        }
    }

    private void setupTrimAdapter(List<EdmundsStyle> dataList) {
        if(dataList == null) {
            dataList = new ArrayList<EdmundsStyle>();
            dataList.add(EdmundsStyle.getBareEntity());
        }

        if(trimAdapter == null) {
            trimAdapter = new TrimAdapter(activity, dataList);
        } else {
            trimAdapter.setDataList(dataList);
            trimAdapter.notifyDataSetChanged();
        }
    }

    private void getMakesForYear(String year) {
        if(!TextUtils.isEmpty(year)) {
            if(yearMakeLoaderCallbacks == null) {
                yearMakeLoaderCallbacks = new LoaderCallbacks<EdmundsYear>() {
                    @Override
                    public Loader<EdmundsYear> onCreateLoader(int i, Bundle bundle) {
                        YearMakesLoader yearLoader = new YearMakesLoader(activity);
                        yearLoader.setYear(bundle.getString("year"));
                        return yearLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<EdmundsYear> edmundsMakeLoader, EdmundsYear yearMakeList) {
                        if(yearMakeList != null && yearMakeList.getMakes() != null && yearMakeList.getMakes().size() > 0) {
                            setupMakeAdapter(yearMakeList.getMakes());
                            makeSpinner.setEnabled(true);
                        } else {
                            Toast.makeText(activity, R.string.year_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<EdmundsYear> edmundsMakeLoader) {

                    }
                };
            }

            Bundle args = new Bundle();
            args.putString("year", year);
            if(getLoaderManager().getLoader(YEAR_MAKE_LOADER_ID) == null) {
                getLoaderManager().initLoader(YEAR_MAKE_LOADER_ID, args, yearMakeLoaderCallbacks);
            } else {
                getLoaderManager().restartLoader(YEAR_MAKE_LOADER_ID, args, yearMakeLoaderCallbacks);
            }
        }
    }

    @Override
    public void onClick(View view) {
        //todo
    }
}
