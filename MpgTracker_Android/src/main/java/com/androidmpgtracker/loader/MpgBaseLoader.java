package com.androidmpgtracker.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.androidmpgtracker.data.MpgBaseRequest;
import com.androidmpgtracker.data.MpgEdmundsRequest;
import com.flurry.android.FlurryAgent;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class MpgBaseLoader<D> extends AsyncTaskLoader<D> {
    protected D result;
    protected Context context;

    public MpgBaseLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        if(result != null)
            deliverResult(result);

        if(takeContentChanged() || result == null)
            forceLoad();
    }

    @Override
    protected void onReset() {
        stopLoading();

        if (result != null)
        {
            result=null;
        }
    }

    @Override
    public void deliverResult(D data) {
        result = data;
        if(isReset()){
            if(result != null){
                result = null;
                return;
            }
        }
        if(isStarted())
            super.deliverResult(result);
    }

    protected MpgEdmundsRequest sendEdmundsRequestAndWait(MpgEdmundsRequest request) {
        StringBuilder urlBuilder = new StringBuilder(request.getRequestPath()).append("?");
        for(String paramKey : request.getParams().keySet()) {
            urlBuilder.append(paramKey).append("=").append(request.getParam(paramKey)).append("&");
        }
        urlBuilder.append("api_key=").append(request.getApiKey());

        URL url = null;

        try {
           url = new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(url != null) {
            OkHttpClient client = new OkHttpClient();
            HttpURLConnection connection = client.open(url);
            InputStream in = null;
            BufferedReader reader = null;

            try {
                if(connection.getResponseCode() == 200) {
                    in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String responseString = reader.readLine();
                    Log.d("MPG", responseString);

                    JsonParser parser = new JsonParser();
                    request.setResponse(parser.parse(responseString).getAsJsonObject());
                } else {
                    in = connection.getErrorStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String errorString = reader.readLine();
                    Log.d("MPG", errorString);
                    if(MpgApplication.isUsageSharingAllowed()) {
                        Map<String, String> errorMap = new HashMap<String, String>();
                        errorMap.put("edmundsError", errorString);
                        FlurryAgent.logEvent(FlurryEvents.EDMUNDS_API_ERROR, errorMap);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        
        return request;
    }
}
