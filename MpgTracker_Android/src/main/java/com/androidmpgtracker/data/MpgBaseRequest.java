package com.androidmpgtracker.data;

import android.content.Context;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Properties;
import java.util.TreeMap;

public abstract class MpgBaseRequest implements Serializable {
    protected String requestMethod;
    protected TreeMap<String, String> params = new TreeMap<String, String >();
    protected JsonObject response;

    protected Properties apiProperties;

    public MpgBaseRequest(Context context, String methodName) {
        requestMethod = methodName;
        params = new TreeMap<String, String>();
        setUpApiProps(context);
    }

    public TreeMap<String, String> getParams()
    {
        return params;
    }

    public void setParams(TreeMap<String, String> params)
    {
        this.params = params;
    }

    public void addParam(String key, String value)
    {
        if(params==null)params = new TreeMap<String, String>();
        params.put(key, value);
    }

    public Object getParam(String key)
    {
        if(params.containsKey(key))
            return params.get(key);
        else
            return null;
    }

    public String getParamsAsString()
    {
        StringBuilder paramString = new StringBuilder();

        for(String key : params.keySet())
        {
            paramString.append(key).append("=").append(params.get(key)).append("&");
        }

        return paramString.substring(0, paramString.length()-1);
    }

    public JsonObject getResponse()
    {
        return response;
    }

    public void setResponse(JsonObject response)
    {
        this.response = response;
    }

    public String getRequestPath() {
        return apiProperties.getProperty("api_url") + requestMethod;
    }

    public String getApiKey() {
        return apiProperties.getProperty("key");
    }

    public String getApiSecret() {
        return apiProperties.getProperty("secret");
    }

    protected abstract void setUpApiProps(Context context);
}
