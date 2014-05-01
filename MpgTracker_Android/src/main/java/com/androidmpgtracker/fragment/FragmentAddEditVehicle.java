package com.androidmpgtracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.androidmpgtracker.R;
import com.androidmpgtracker.adapter.MakeAdapter;
import com.androidmpgtracker.adapter.ModelAdapter;
import com.androidmpgtracker.adapter.TrimAdapter;
import com.androidmpgtracker.adapter.YearAdapter;
import com.androidmpgtracker.data.entities.EdmundsMake;
import com.androidmpgtracker.data.entities.EdmundsModel;
import com.androidmpgtracker.data.entities.EdmundsModelYear;
import com.androidmpgtracker.data.entities.EdmundsStyle;
import com.androidmpgtracker.data.entities.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
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

        return root;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        //todo more
    }

    private void setupYearAdapter() {
        List<String> yearList = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = 1990; i <= currentYear; i++) {
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

    @Override
    public void onClick(View view) {
        //todo
    }
}
