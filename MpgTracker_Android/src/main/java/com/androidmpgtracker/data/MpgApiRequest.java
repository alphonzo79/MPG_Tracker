package com.androidmpgtracker.data;

import android.content.Context;

import com.androidmpgtracker.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MpgApiRequest extends MpgBaseRequest {
    private String postMethod;

    public MpgApiRequest(Context context, String methodName) {
        super(context, methodName);
    }

    @Override
    protected void setUpApiProps(Context context) {
        apiProperties = new Properties();
        InputStream fis = null;
        try {
            fis = context.getResources().openRawResource(R.raw.mpg_tracker_api_settings);
            apiProperties.loadFromXML(fis);
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                if(fis!=null) fis.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getRequestPath() {
        return apiProperties.getProperty("api_url") + requestMethod;
    }

    public String getPostMethod() {
        return postMethod;
    }

    public void setPostMethod(String postMethod) {
        this.postMethod = postMethod;
    }
}
