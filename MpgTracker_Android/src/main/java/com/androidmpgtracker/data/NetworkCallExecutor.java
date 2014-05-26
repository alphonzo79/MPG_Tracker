package com.androidmpgtracker.data;

import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Log;

import com.androidmpgtracker.MpgApplication;
import com.androidmpgtracker.Utils.FlurryEvents;
import com.flurry.android.FlurryAgent;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NetworkCallExecutor {

    public MpgApiRequest sendMpgPostAndWait(MpgApiRequest request) {
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            reqEntity.addPart("method", new StringBody(request.getPostMethod()));
            for (Map.Entry<String, String> entry : request.getParams().entrySet())
            {
                reqEntity.addPart(entry.getKey(), new StringBody(entry.getValue()) );
            }

            HttpPost post = new HttpPost(request.getRequestPath());
            post.setEntity(reqEntity);
            post.addHeader("client", request.getApiKey());
            post.addHeader("secret", request.getApiSecret());

            HttpClient client = AndroidHttpClient.newInstance("");

            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode()==200) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String responseString = buffer.readLine();
                JsonParser parser = new JsonParser();
                Log.d("MPG", responseString);
                request.setResponse(parser.parse(responseString).getAsJsonObject());

                responseString = null;
                buffer = null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }

    public MpgApiRequest sendMpgGetAndWait(MpgApiRequest request) {
        try {
            StringBuilder pathBuilder = new StringBuilder(request.getRequestPath());
            for (Map.Entry<String, String> entry : request.getParams().entrySet())
            {
                pathBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }

            HttpGet get = new HttpGet(pathBuilder.toString());
            get.addHeader("client", request.getApiKey());
            get.addHeader("secret", request.getApiSecret());

            HttpClient client = AndroidHttpClient.newInstance("");

            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode()==200) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String responseString = buffer.readLine();
                JsonParser parser = new JsonParser();
                Log.d("MPG", responseString);
                request.setResponse(parser.parse(responseString).getAsJsonObject());

                responseString = null;
                buffer = null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }

    public MpgEdmundsRequest sendEdmundsRequestAndWait(MpgEdmundsRequest request) {
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
