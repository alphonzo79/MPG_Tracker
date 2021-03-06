package com.androidmpgtracker.data.entities;

import android.text.TextUtils;

public class Vehicle extends MpgApiEntity {
    static final long serialVersionUID = 8161058626817994594L;

    private Long id;
    private Integer year;
    private String make;
    private String model;
    private String trim;
    private Long trimId;
    private Boolean isCustom;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public Long getTrimId() {
        return trimId;
    }

    public void setTrimId(Long trimId) {
        this.trimId = trimId;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public Vehicle getClone() {
        Vehicle result = new Vehicle();

        result.setId(id);
        result.setYear(year);
        result.setMake(make);
        result.setModel(model);
        result.setTrim(trim);
        result.setTrimId(trimId);
        result.setIsCustom(isCustom);

        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(year != null) {
            builder.append(year).append(" ");
        }
        if(!TextUtils.isEmpty(make)) {
            builder.append(make).append(" ");
        }
        if(!TextUtils.isEmpty(model)) {
            builder.append(model).append(" ");
        }
        if(!TextUtils.isEmpty(trim)) {
            builder.append(trim);
        }

        return builder.toString().trim();
    }

    public String getDisplayString() {
        StringBuilder builder = new StringBuilder();
        if(year != null) {
            builder.append(year).append(" ");
        }
        if(!TextUtils.isEmpty(make)) {
            builder.append(make).append(" ");
        }
        if(!TextUtils.isEmpty(model)) {
            builder.append(model);
        }

        return builder.toString().trim();
    }
}
