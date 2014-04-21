package com.androidmpgtracker.data.entities;

import java.util.List;

public class EdmundsYear extends MpgApiEntity {
    static final long serialVersionUID = -82044478749542659L;

    private List<EdmundsMake> makes;

    public List<EdmundsMake> getMakes() {
        return makes;
    }

    public void setMakes(List<EdmundsMake> makes) {
        this.makes = makes;
    }
}
