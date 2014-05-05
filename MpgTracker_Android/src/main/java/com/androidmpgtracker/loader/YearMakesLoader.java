package com.androidmpgtracker.loader;

import android.content.Context;
import android.text.TextUtils;

import com.androidmpgtracker.data.Method;
import com.androidmpgtracker.data.MpgEdmundsRequest;
import com.androidmpgtracker.data.entities.EdmundsYear;
import com.google.gson.Gson;

public class YearMakesLoader extends MpgBaseLoader<EdmundsYear> {
    private String year;

    public YearMakesLoader(Context context) {
        super(context);
    }

    @Override
    public EdmundsYear loadInBackground() {
        if(!TextUtils.isEmpty(year)) {
            MpgEdmundsRequest request = new MpgEdmundsRequest(context, Method.GET_MAKES_FOR_YEAR);
            request.addParam("year", year);
            request.addParam("fmt", "json");
            request.addParam("view", "basic");

            request = sendEdmundsRequestAndWait(request);
            if(request.getResponse() != null) {
                result = new Gson().fromJson(request.getResponse(), EdmundsYear.class);
            }
        }

        return result;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
