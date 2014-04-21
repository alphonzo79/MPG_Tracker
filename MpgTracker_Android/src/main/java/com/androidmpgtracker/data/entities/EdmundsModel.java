package com.androidmpgtracker.data.entities;

import java.util.List;

public class EdmundsModel extends MpgApiEntity {
    static final long serialVersionUID = 2855183380518305335L;

    private String id;
    private String name;
    private String niceName;
    private List<EdmundsModelYear> years;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<EdmundsModelYear> getYears() {
        return years;
    }

    public void setYears(List<EdmundsModelYear> years) {
        this.years = years;
    }
}
