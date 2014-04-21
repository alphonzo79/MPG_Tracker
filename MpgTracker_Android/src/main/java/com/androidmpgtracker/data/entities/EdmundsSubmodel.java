package com.androidmpgtracker.data.entities;

public class EdmundsSubmodel extends MpgApiEntity {
    static final long serialVersionUID = 7436522543432085713L;

    private String body;
    private String modelName;
    private String niceName;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }
}
