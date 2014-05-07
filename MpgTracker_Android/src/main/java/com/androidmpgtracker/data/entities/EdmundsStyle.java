package com.androidmpgtracker.data.entities;

public class EdmundsStyle extends MpgApiEntity {
    static final long serialVersionUID = -8223122031161500965L;

    private Long id;
    private String name;
    private EdmundsSubmodel submodel;
    private String trim;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdmundsSubmodel getSubmodel() {
        return submodel;
    }

    public void setSubmodel(EdmundsSubmodel submodel) {
        this.submodel = submodel;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public static EdmundsStyle getBareEntity() {
        EdmundsStyle retVal = new EdmundsStyle();

        retVal.setId(-1l);
        retVal.setName("");

        return retVal;
    }
}
