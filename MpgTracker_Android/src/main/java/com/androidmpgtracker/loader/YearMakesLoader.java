package com.androidmpgtracker.loader;

import android.content.Context;
import android.text.TextUtils;

import com.androidmpgtracker.data.Method;
import com.androidmpgtracker.data.MpgEdmundsRequest;
import com.androidmpgtracker.data.NetworkCallExecutor;
import com.androidmpgtracker.data.entities.EdmundsYear;
import com.google.gson.Gson;

public class YearMakesLoader extends MpgBaseLoader<EdmundsYear> {
    private Integer year;

    public YearMakesLoader(Context context) {
        super(context);
    }

    @Override
    public EdmundsYear loadInBackground() {
        if(year != null) {
            MpgEdmundsRequest request = new MpgEdmundsRequest(context, Method.GET_MAKES_FOR_YEAR);
            request.addParam("year", String.valueOf(year));
            request.addParam("fmt", "json");
            request.addParam("view", "basic");

            request = new NetworkCallExecutor().sendEdmundsRequestAndWait(request);
            if(request.getResponse() != null) {
                result = new Gson().fromJson(request.getResponse(), EdmundsYear.class);
            }
        }

        return result;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
