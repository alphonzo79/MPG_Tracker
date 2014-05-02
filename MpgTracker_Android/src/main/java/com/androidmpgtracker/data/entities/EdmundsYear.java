package com.androidmpgtracker.data.entities;

import java.util.List;

public class EdmundsYear extends MpgApiEntity {
    static final long serialVersionUID = -82044478749542659L;

    private List<EdmundsMake> makes;
    private Integer makesCount;

    public List<EdmundsMake> getMakes() {
        return makes;
    }

    public void setMakes(List<EdmundsMake> makes) {
        this.makes = makes;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMakesCount() {
        return makesCount;
    }

    public void setMakesCount(Integer makesCount) {
        this.makesCount = makesCount;
    }
}
