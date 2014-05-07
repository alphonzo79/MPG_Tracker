package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.MakeAdapter;
import com.androidmpgtracker.adapter.ModelAdapter;
import com.androidmpgtracker.adapter.TrimAdapter;
import com.androidmpgtracker.adapter.YearAdapter;
import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.Vehicle;

public class CustomVehicleFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private Vehicle vehicle;
    private View root;

    private EditText yearInput;
    private EditText makeInput;
    private EditText modelInput;
    private EditText trimInput;

    private Activity activity;
    private MpgFragmentListener listener;

    private Button saveButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        if(activity instanceof MpgFragmentListener) {
            listener = (MpgFragmentListener)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_custom_vehicle, null);

        yearInput = (EditText)root.findViewById(R.id.year_input);
        yearInput.addTextChangedListener(this);

        makeInput = (EditText)root.findViewById(R.id.make_input);
        makeInput.addTextChangedListener(this);

        modelInput = (EditText)root.findViewById(R.id.model_input);
        modelInput.addTextChangedListener(this);

        trimInput = (EditText)root.findViewById(R.id.trim_input);
        trimInput.addTextChangedListener(this);

        saveButton = (Button)root.findViewById(R.id.save_car_button);
        saveButton.setEnabled(false);

        setupFields();

        if(vehicle == null) {
            vehicle = new Vehicle();
        } else {
            ((TextView) root.findViewById(R.id.header)).setText(R.string.edit_custom_vehicle_header);
        }

        return root;
    }

    private void setupFields() {
        if(saveButton != null && vehicle != null) {
            boolean enableButton = false;

            if(vehicle.getYear() != null) {
                yearInput.setText(String.valueOf(vehicle.getYear()));
                enableButton = true;
            }
            if(vehicle.getMake() != null) {
                makeInput.setText(vehicle.getMake());
                enableButton = true;
            }
            if(vehicle.getModel() != null) {
                modelInput.setText(vehicle.getModel());
                enableButton = true;
            }
            if(vehicle.getTrim() != null) {
                trimInput.setText(vehicle.getTrim());
                enableButton = true;
            }

            saveButton.setEnabled(enableButton);
        }
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        if(vehicle == null) {
            this.vehicle = new Vehicle();
        } else if(root != null) {
            ((TextView) root.findViewById(R.id.header)).setText(R.string.edit_custom_vehicle_header);
        }

        setupFields();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.save_car_button:
                vehicle.setYear(Integer.valueOf(yearInput.getText().toString()));
                vehicle.setMake(makeInput.getText().toString());
                vehicle.setModel(modelInput.getText().toString());
                vehicle.setTrim(trimInput.getText().toString());
                vehicle.setIsCustom(true);

                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        VehiclesDao dao = new VehiclesDao(activity);
                        return dao.saveVehicle(vehicle);
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            if(listener != null) {
                                listener.killFragment();
                            }
                        } else {
                            Toast.makeText(activity, R.string.save_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        //do nothing
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length() > 0 && !saveButton.isEnabled()) {
            saveButton.setEnabled(true);
        }
    }
}
