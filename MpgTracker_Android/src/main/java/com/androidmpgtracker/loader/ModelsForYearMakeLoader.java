package com.androidmpgtracker.loader;

import android.content.Context;
import android.text.TextUtils;

import com.androidmpgtracker.data.Method;
import com.androidmpgtracker.data.MpgEdmundsRequest;
import com.androidmpgtracker.data.NetworkCallExecutor;
import com.androidmpgtracker.data.entities.EdmundsMake;
import com.google.gson.Gson;

public class ModelsForYearMakeLoader extends MpgBaseLoader<EdmundsMake> {
    private String makeNiceName;
    private Integer year;


    public ModelsForYearMakeLoader(Context context) {
        super(context);
    }

    @Override
    public EdmundsMake loadInBackground() {
        if(year != null && !TextUtils.isEmpty(makeNiceName)) {
            StringBuilder methodBuilder = new StringBuilder("/").append(makeNiceName).append(Method.GET_MODEL_FOR_YEAR_AND_MAKE);
            MpgEdmundsRequest request = new MpgEdmundsRequest(context, methodBuilder.toString());
            request.addParam("year", String.valueOf(year));
            request.addParam("view", "basic");
            request.addParam("fmt", "json");

            request = new NetworkCallExecutor().sendEdmundsRequestAndWait(request);
            if(request.getResponse() != null) {
                result = new Gson().fromJson(request.getResponse(), EdmundsMake.class);
            }
        }

        return result;
    }

    public void setMakeNiceName(String makeNiceName) {
        this.makeNiceName = makeNiceName;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
