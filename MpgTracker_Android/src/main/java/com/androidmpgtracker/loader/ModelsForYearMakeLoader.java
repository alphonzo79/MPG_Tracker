package com.androidmpgtracker.loader;

import android.content.Context;

import com.androidmpgtracker.data.entities.EdmundsMake;

public class ModelsForYearMakeLoader extends MpgBaseLoader<EdmundsMake> {
    private String makeNiceName;
    private Integer year;


    public ModelsForYearMakeLoader(Context context) {
        super(context);
    }

    @Override
    public EdmundsMake loadInBackground() {


        ///vehicle/v2/bmw/models?year=2013&category=Sedan&view=basic&fmt=json&api_key=3usewnhfjum378f5snyg8295

        return null;
    }

    public void setMakeNiceName(String makeNiceName) {
        this.makeNiceName = makeNiceName;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
