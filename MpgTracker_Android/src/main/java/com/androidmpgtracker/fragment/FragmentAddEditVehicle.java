package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.MakeAdapter;
import com.androidmpgtracker.adapter.ModelAdapter;
import com.androidmpgtracker.adapter.TrimAdapter;
import com.androidmpgtracker.adapter.YearAdapter;
import com.androidmpgtracker.data.dao.VehiclesDao;
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
    private Vehicle inputVehicle;
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
    private AddEditVehicleListener listener;
    private MpgFragmentListener fragListener;

    private Button notListedButton;
    private Button saveButton;

    private LoaderCallbacks<EdmundsYear> yearMakeLoaderCallbacks;
    private final int YEAR_MAKE_LOADER_ID = 45;

    private LoaderCallbacks<EdmundsMake> modelLoaderCallbacks;
    private final int MODEL_LOADER_ID = 55;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        if(activity instanceof AddEditVehicleListener) {
            listener = (AddEditVehicleListener)activity;
        }

        if(activity instanceof MpgFragmentListener) {
            fragListener = (MpgFragmentListener)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_edit_vehicle, null);

        setupYearSpinner();
        setupMakeSpinner();
        setupModelSpinner();
        setupTrimSpinner();

        notListedButton = (Button)root.findViewById(R.id.not_listed_button);
        notListedButton.setOnClickListener(this);

        saveButton = (Button)root.findViewById(R.id.save_car_button);
        saveButton.setOnClickListener(this);
        saveButton.setEnabled(false);

        if(vehicle == null) {
            vehicle = new Vehicle();
        } else {
            ((TextView) root.findViewById(R.id.header)).setText(R.string.edit_vehicle_header);
        }

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
                vehicle.setMake(null);

                setupModelAdapter(null);
                modelSpinner.setEnabled(false);
                vehicle.setModel(null);

                setupTrimAdapter(null);
                trimSpinner.setEnabled(false);
                vehicle.setTrim(null);
                vehicle.setTrimId(null);

                saveButton.setEnabled(false);

                if(i > 0) {
                    vehicle.setYear(yearAdapter.getItem(i));
                    getMakesForYear(vehicle.getYear());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        if(inputVehicle != null && inputVehicle.getYear() != null) {
            yearSpinner.setSelection(yearAdapter.indexOf(String.valueOf(inputVehicle.getYear())), true);
        }
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
                vehicle.setModel(null);

                setupTrimAdapter(null);
                trimSpinner.setEnabled(false);
                vehicle.setTrim(null);
                vehicle.setTrimId(null);

                if(i > 0) {
                    saveButton.setEnabled(true);
                    vehicle.setMake(makeAdapter.getItem(i).getName());
                    getModelsForYearAndMake(vehicle.getYear(), makeAdapter.getItem(i).getNiceName());
                    if(inputVehicle != null) {
                        //clear out the input vehicle so it will not reset the selection if we change the year
                        inputVehicle.setMake(null);
                    }
                }
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
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //reset and disable the lower spinners
                setupTrimAdapter(null);
                trimSpinner.setEnabled(false);
                vehicle.setTrim(null);
                vehicle.setTrimId(null);

                if(i > 0) {
                    vehicle.setModel(modelAdapter.getItem(i).getName());
                    getTrimsForModel(modelAdapter.getItem(i));
                    if(inputVehicle != null) {
                        //Clear out the model for the input vehicle so it doesn't reset things if we change the year or make
                        inputVehicle.setModel(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupTrimSpinner() {
        trimSpinner = (Spinner)root.findViewById(R.id.trim_spinner);
        setupTrimAdapter(null);
        trimSpinner.setAdapter(trimAdapter);
        trimSpinner.setEnabled(false);
        trimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    vehicle.setTrim(trimAdapter.getItem(i).getName());
                    vehicle.setTrimId(trimAdapter.getItem(i).getId());
                    if(inputVehicle != null) {
                        //Reset the trim for the inputVehicle so it doesn't reset the selection if we chand the year, make or model
                        inputVehicle.setTrim(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setVehicle(Vehicle vehicle) {
        this.inputVehicle = vehicle;
        this.vehicle = vehicle.getClone();
        if(vehicle == null) {
            this.vehicle = new Vehicle();
        } else if(root != null) {
            ((TextView)root.findViewById(R.id.header)).setText(R.string.edit_vehicle_header);
            setupYearSpinner();
        }
    }

    private void setupYearAdapter() {
        List<Integer> yearList = new ArrayList<Integer>();
        yearList.add(null);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = currentYear; i >= 1990; i--) {
            yearList.add(i);
        }
        yearAdapter = new YearAdapter(activity, yearList);
    }

    private void setupMakeAdapter(List<EdmundsMake> dataList) {
        List<EdmundsMake> makeList = new ArrayList<EdmundsMake>();
        if(dataList == null) {
            makeList.add(EdmundsMake.getBareEntity());
        } else {
            makeList.add(null);
            makeList.addAll(dataList);
        }

        if(makeAdapter == null) {
            makeAdapter = new MakeAdapter(activity, dataList);
        }

        makeAdapter.setDataList(makeList);
        makeAdapter.notifyDataSetChanged();

        if(inputVehicle != null && inputVehicle.getMake() != null && makeSpinner != null) {
            makeSpinner.setSelection(makeAdapter.indexOf(inputVehicle.getMake()), true);
        }
    }

    private void setupModelAdapter(List<EdmundsModel> dataList) {
        List<EdmundsModel> modelList = new ArrayList<EdmundsModel>();
        if(dataList == null) {
            modelList.add(EdmundsModel.getBareEntity());
        } else {
            modelList.add(null);
            modelList.addAll(dataList);
        }

        if(modelAdapter == null) {
            modelAdapter = new ModelAdapter(activity, dataList);
        }

        modelAdapter.setDataList(modelList);
        modelAdapter.notifyDataSetChanged();

        if(inputVehicle != null && inputVehicle.getModel() != null && modelSpinner != null) {
            modelSpinner.setSelection(modelAdapter.indexOf(inputVehicle.getModel()), true);
        }
    }

    private void setupTrimAdapter(List<EdmundsStyle> dataList) {
        List<EdmundsStyle> trimList = new ArrayList<EdmundsStyle>();
        if(dataList == null) {
            trimList.add(EdmundsStyle.getBareEntity());
        } else {
            trimList.add(null);
            trimList.addAll(dataList);
        }

        if(trimAdapter == null) {
            trimAdapter = new TrimAdapter(activity, dataList);
        }

        trimAdapter.setDataList(trimList);
        trimAdapter.notifyDataSetChanged();

        if(inputVehicle != null && inputVehicle.getTrim() != null && trimSpinner != null) {
            trimSpinner.setSelection(trimAdapter.indexOf(inputVehicle.getTrim()), true);
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

    private void getTrimsForModel(EdmundsModel selectedModel) {
        if(selectedModel != null && selectedModel.getYears() != null && selectedModel.getYears().size() > 0) {
            if(selectedModel.getYears().get(0) != null && selectedModel.getYears().get(0).getStyles() != null && selectedModel.getYears().get(0).getStyles().size() > 0) {
                setupTrimAdapter(selectedModel.getYears().get(0).getStyles());
                trimSpinner.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.save_car_button:
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        VehiclesDao dao = new VehiclesDao(activity);
                        return dao.saveVehicle(vehicle);
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            if(fragListener != null) {
                                fragListener.killFragment();
                            }
                        } else {
                            Toast.makeText(activity, R.string.save_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
                break;
            case R.id.not_listed_button:
                if(listener != null) {
                    listener.goToCustomVehicleFragment(vehicle);
                }
                break;
        }
    }

    public interface AddEditVehicleListener {
        public void goToCustomVehicleFragment(Vehicle vehicle);
    }
}
