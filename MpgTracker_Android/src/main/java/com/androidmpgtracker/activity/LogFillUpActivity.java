package com.androidmpgtracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.VehicleAdapter;

public class LogFillUpActivity extends Activity implements View.OnClickListener {
    private Button saveButton;
    private Spinner vehicleSpinner;
    private EditText milesInput, gallonsInput, priceInput;
    private TextView mpgValue, costValue;

    private View root;

    private VehicleAdapter vehicleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_fill_up);

        //todo
    }

    @Override
    public void onClick(View view) {
        //todo
    }
}
