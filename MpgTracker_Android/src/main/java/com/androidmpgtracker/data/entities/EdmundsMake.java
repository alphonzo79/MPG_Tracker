package com.androidmpgtracker.data.entities;

import java.util.List;

public class EdmundsMake extends MpgApiEntity {
    static final long serialVersionUID = 5510057652548604271L;

    private Integer id;
    private String name;
    private String niceName;
    private List<EdmundsModel> models;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public List<EdmundsModel> getModels() {
        return models;
    }

    public void setModels(List<EdmundsModel> models) {
        this.models = models;
    }

    public static EdmundsMake getBareEntity() {
        EdmundsMake retVal = new EdmundsMake();

        retVal.setId(-1);
        retVal.setName("");

        return retVal;
    }
}
