package com.androidmpgtracker.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.VehicleAdapter;
import com.androidmpgtracker.data.dao.VehiclesDao;
import com.androidmpgtracker.data.entities.Vehicle;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFillUpActivity extends Activity implements View.OnClickListener {
    private Button saveButton;
    private Spinner vehicleSpinner;
    private EditText milesInput, gallonsInput, priceInput;
    private TextView mpgValue, costValue;

    private VehicleAdapter vehicleAdapter;

    String currencySymbol = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_fill_up);

        vehicleSpinner = (Spinner)findViewById(R.id.car_spinner);
        milesInput = (EditText)findViewById(R.id.miles_input);
        milesInput.addTextChangedListener(milesTextWatcher);
        gallonsInput = (EditText)findViewById(R.id.gallons_input);
        gallonsInput.addTextChangedListener(gallonsTextWatcher);
        priceInput = (EditText)findViewById(R.id.price_input);
        priceInput.addTextChangedListener(priceTextWatcher);
        mpgValue = (TextView)findViewById(R.id.mpg_value);
        costValue = (TextView)findViewById(R.id.cost_value);
        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList.add(new Vehicle());
        vehicleAdapter = new VehicleAdapter(this, vehicleList);
        vehicleSpinner.setAdapter(vehicleAdapter);

        new AsyncTask<Void, Void, List<Vehicle>>() {

            @Override
            protected List<Vehicle> doInBackground(Void... voids) {
                VehiclesDao dao = new VehiclesDao(LogFillUpActivity.this);
                return dao.getAllVehicles();
            }

            @Override
            protected void onPostExecute(List<Vehicle> vehicleList) {
                vehicleAdapter.setDataList(vehicleList);
                vehicleAdapter.notifyDataSetChanged();
            }
        }.execute();

        try {
            currencySymbol = Currency.getInstance(Locale.getDefault()).getSymbol();
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            currencySymbol = "$";
        }
    }

    private TextWatcher milesTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editableString = editable.toString();

            //Keep it to one digit after the decimal
            if(editableString.contains(".")) {
                int decimalIndex = editableString.indexOf('.');
                if(editableString.length() > decimalIndex + 2) {
                    //enforce one digit after the decimal
                    editable.delete(decimalIndex + 2, editableString.length());
                } else if(decimalIndex == 0) {
                    //Make sure we have at least one leading 0
                    editable.insert(0, "0");
                }
            }

            figureResults(editable.toString(), gallonsInput.getText().toString(), priceInput.getText().toString());
        }
    };

    private TextWatcher gallonsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editableString = editable.toString();

            //Keep it to two digit after the decimal
            if(editableString.contains(".")) {
                int decimalIndex = editableString.indexOf('.');
                if(editableString.length() > decimalIndex + 3) {
                    //enforce one digit after the decimal
                    editable.delete(decimalIndex + 3, editableString.length());
                } else if(decimalIndex == 0) {
                    //Make sure we have at least one leading 0
                    editable.insert(0, "0");
                }
            }

            figureResults(milesInput.getText().toString(), editable.toString(), priceInput.getText().toString());
        }
    };

    private TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editableString = editable.toString();

            //Keep a $ at the beginning
            if(editableString.length() < currencySymbol.length() || !editableString.substring(0, currencySymbol.length()).equals(currencySymbol)) {
                Pattern charsToKeep = Pattern.compile("[0-9,\\,\\.]");
                Matcher matcher = charsToKeep.matcher(editableString);
                if(matcher.matches()) {
                    editableString = currencySymbol + editableString.substring(matcher.start());
                } else {
                    editableString = currencySymbol;
                }
                priceInput.setText(editableString);
                priceInput.setSelection(priceInput.getText().length());
                return;
            }

            //Keep it to two digit after the decimal
            if(editableString.contains(".")) {
                int decimalIndex = editableString.indexOf('.');
                if(editableString.length() > decimalIndex + 3) {
                    //enforce one digit after the decimal
                    editable.delete(decimalIndex + 3, editableString.length());
                } else if(decimalIndex == currencySymbol.length()) {
                    //Make sure we have at least one leading 0
                    editable.insert(currencySymbol.length(), "0");
                }
            }

            figureResults(milesInput.getText().toString(), gallonsInput.getText().toString(), editable.toString());
        }
    };

    private void figureResults(String milesString, String gallonsString, String priceString) {
        //Figure miles per gallon first
        float mpg = -1l;
        if(!TextUtils.isEmpty(milesString) &&!TextUtils.isEmpty(gallonsString)) {
            try{
                mpg = NumberFormat.getNumberInstance().parse(milesString).floatValue() / NumberFormat.getNumberInstance().parse(gallonsString).floatValue();
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }

        //Set the MPG text if we need
        if(mpg > -1) {
            String mpgString = String.format("%01.2f", mpg);
            if(mpgValue.getText() != mpgString) {
                mpgValue.setText(mpgString);
            }

            //And now figure cost per mile if possible
            if(!TextUtils.isEmpty(priceString) && mpg > -1) {
                float costPerMile = -1l;
                try{
                    costPerMile = NumberFormat.getNumberInstance().parse(priceString.substring(currencySymbol.length())).floatValue() / mpg;
                } catch(ParseException e) {
                    e.printStackTrace();
                }

                if(costPerMile > -1) {
                    String costString = String.format("%s%01.2f", currencySymbol, costPerMile);
                    if(!costValue.getText().equals(costString)) {
                        costValue.setText(costString);
                    }
                } else if(!costValue.getText().equals(getString(R.string.double_dashes))) {
                    costValue.setText(getString(R.string.double_dashes));
                }
            }
        } else if(!mpgValue.getText().equals(getString(R.string.double_dashes))) {
            mpgValue.setText(getString(R.string.double_dashes));
        }
    }

    @Override
    public void onClick(View view) {
        //todo
    }
}
