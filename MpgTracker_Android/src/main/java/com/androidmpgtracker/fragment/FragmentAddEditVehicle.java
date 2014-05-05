package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.app.LoaderManager;
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
import com.androidmpgtracker.loader.ModelsForYearMakeLoader;
import com.androidmpgtracker.loader.YearMakesLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentAddEditVehicle extends Fragment implements View.OnClickListener {
    private Vehicle vehicle;
    private View root;

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

    private LoaderCallbacks<EdmundsMake> modelLoaderCallbacks;
    private final int MODEL_LOADER_ID = 55;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_edit_vehicle, null);

        setupYearSpinner();
        setupMakeSpinner();
        setupModelSpinner();
        setupTrimSpinner();

        saveButton = (Button)root.findViewById(R.id.save_car_button);
        saveButton.setEnabled(false);

        return root;
    }

    private void setupYearSpinner() {
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
    }

    private void setupMakeSpinner() {
        makeSpinner = (Spinner)root.findViewById(R.id.make_spinner);
        setupMakeAdapter(null);
        makeSpinner.setAdapter(makeAdapter);
        makeSpinner.setEnabled(false);
        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //reset and disable the lower spinners
                setupModelAdapter(null);
                modelSpinner.setEnabled(false);

                setupTrimAdapter(null);
                trimSpinner.setEnabled(false);

                saveButton.setEnabled(true);

                getModelsForYearAndMake(yearAdapter.getItem(yearSpinner.getSelectedItemPosition()), makeAdapter.getItem(i).getNiceName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupModelSpinner() {
        modelSpinner = (Spinner)root.findViewById(R.id.model_spinner);
        setupModelAdapter(null);
        modelSpinner.setAdapter(modelAdapter);
        modelSpinner.setEnabled(false);
    }

    private void setupTrimSpinner() {
        trimSpinner = (Spinner)root.findViewById(R.id.trim_spinner);
        setupTrimAdapter(null);
        trimSpinner.setAdapter(trimAdapter);
        trimSpinner.setEnabled(false);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        //todo more
    }

    private void setupYearAdapter() {
        List<Integer> yearList = new ArrayList<Integer>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = currentYear; i >= 1990; i--) {
            yearList.add(i);
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

    private void getMakesForYear(int year) {
        if (activity != null) {
            if(yearMakeLoaderCallbacks == null) {
                yearMakeLoaderCallbacks = new LoaderCallbacks<EdmundsYear>() {
                    @Override
                    public Loader<EdmundsYear> onCreateLoader(int i, Bundle bundle) {
                        YearMakesLoader yearLoader = new YearMakesLoader(activity);
                        yearLoader.setYear(bundle.getInt("year"));
                        return yearLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<EdmundsYear> edmundsMakeLoader, EdmundsYear yearMakeList) {
                        if (edmundsMakeLoader.getId() == YEAR_MAKE_LOADER_ID) {
                            if(yearMakeList != null && yearMakeList.getMakes() != null && yearMakeList.getMakes().size() > 0) {
                                setupMakeAdapter(yearMakeList.getMakes());
                                makeSpinner.setEnabled(true);
                            } else {
                                Toast.makeText(activity, R.string.year_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<EdmundsYear> edmundsMakeLoader) {

                    }
                };
            }

            Bundle args = new Bundle();
            args.putInt("year", year);
            if(getLoaderManager().getLoader(YEAR_MAKE_LOADER_ID) == null) {
                getLoaderManager().initLoader(YEAR_MAKE_LOADER_ID, args, yearMakeLoaderCallbacks);
            } else {
                getLoaderManager().restartLoader(YEAR_MAKE_LOADER_ID, args, yearMakeLoaderCallbacks);
            }
        }
    }

    private void getModelsForYearAndMake(int year, String modelNiceName) {
        if(!TextUtils.isEmpty(modelNiceName) && activity != null) {
            if(modelLoaderCallbacks == null) {
                modelLoaderCallbacks = new LoaderCallbacks<EdmundsMake>() {
                    @Override
                    public Loader<EdmundsMake> onCreateLoader(int id, Bundle args) {
                        ModelsForYearMakeLoader modelLoader = new ModelsForYearMakeLoader(activity);
                        modelLoader.setMakeNiceName(args.getString("model"));
                        modelLoader.setYear(args.getInt("year"));
                        return modelLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<EdmundsMake> loader, EdmundsMake data) {
                        if (loader.getId() == MODEL_LOADER_ID) {
                            if(data!= null && data.getModels() != null && data.getModels().size() > 0) {
                                setupModelAdapter(data.getModels());
                                modelSpinner.setEnabled(true);
                            } else {
                                Toast.makeText(activity, R.string.make_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<EdmundsMake> loader) {

                    }
                };
            }

            Bundle args = new Bundle();
            args.putInt("year", year);
            args.putString("model", modelNiceName);
            if(getLoaderManager().getLoader(MODEL_LOADER_ID) == null) {
                getLoaderManager().initLoader(MODEL_LOADER_ID, args, modelLoaderCallbacks);
            } else {
                getLoaderManager().restartLoader(MODEL_LOADER_ID, args, modelLoaderCallbacks);
            }
        }
    }

    @Override
    public void onClick(View view) {
        //todo
    }
}
