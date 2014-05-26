package com.androidmpgtracker.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.androidmpgtracker.data.MpgApiRequest;
import com.androidmpgtracker.data.MpgBaseRequest;
import com.androidmpgtracker.data.MpgEdmundsRequest;
import com.flurry.android.FlurryAgent;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
}
