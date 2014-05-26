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
//            if(response.getStatusLine().getStatusCode()==200)
//            {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String responseString = buffer.readLine();
            Log.d("MPG", responseString);
//                JsonParser parser = new JsonParser();
//                request.setResponse(parser.parse(responseString).getAsJsonObject());
//                if(request.getResponseCode() == 200)
//                {
//                    if(request.getTtl() > 0){
//                        cacheResponse(request.getCacheKey(), responseString);
//                    }
//                } else {
//                    TreeMap<String, String> paramsMap = request.getParams();
//                    if (BBcomApplication.isAllowPerformanceSharing()) {
//                        StringBuilder builder = new StringBuilder();
//                        builder.append("Method:");
//                        builder.append(request.getMethodName());
//                        builder.append(", ResponseCode:");
//                        builder.append(request.getResponse().getResponseCode());
//                        builder.append(", ErrorMessage:");
//                        builder.append(request.getResponse().getErrorMessage());
//                        builder.append(", Params:");
//
//                        StringBuilder paramBuilder = new StringBuilder();
//                        for(String key : paramsMap.keySet()) {
//                            if(key.equals("signature") || key.equals("agent") || key.equals("consumer") || key.equals("nonce") || key.equals("timestamp")) {
//                                continue;
//                            }
//                            paramBuilder.append(key);
//                            paramBuilder.append("=");
//                            paramBuilder.append(paramsMap.get(key));
//                            paramBuilder.append("&");
//                        }
//
//                        builder.append(paramBuilder.toString());
//                        FlurryAgent.onError("ApiCallFailure", builder.toString(), new Throwable());
//                    }
//
//                    String token = paramsMap.get("token");
//                    if(bbcomresp.getResponseCode() == 504 || TextUtils.isEmpty(token) || token.length() < 5) {
//                        //The token is bad. Let's try to refresh the session
//                        RefreshSessionUtil refreshSessionUtil = new RefreshSessionUtil(this);
//                        refreshSessionUtil.refreshSession(false);
//                    }
//                }
                responseString = null;
                buffer = null;
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if(url != null && postBody != null) {
//            Request.Builder builder = new Request.Builder();
//            builder.addHeader("client", request.getApiKey());
//            builder.addHeader("secret", request.getApiSecret());
//            builder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postBody));
//            builder.url(url);
//            Request okRequest = builder.build();
//            Log.d("MPG", "request string: " + okRequest.toString());
//
//            OkHttpClient client = new OkHttpClient();
//            Call call = client.newCall(okRequest);
//            try {
//                Response okResponse = call.execute();
//                String responseString = okResponse.toString();
//                Log.d("MPG", responseString);
//
//                String code = okResponse.header("code");
//                String message = okResponse.header("message");
//                String body = okResponse.body().string();
//                Log.d("MPG", "code: " + code + " message: " + message + " body: " + body);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            //todo headers
//            HttpURLConnection connection = client.open(url);
//            OutputStream out = null;
//            InputStream in = null;
//            BufferedReader reader = null;
//            try {
//                // Write the request.
//                connection.setRequestMethod("POST");
//
//                out = connection.getOutputStream();
//                out.write(postBody);
//                out.close();
//
//                // Read the response.
//                if(connection.getResponseCode() == 200) {
//                    in = connection.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String responseString = reader.readLine();
//                    Log.d("MPG", responseString);
//
//                    JsonParser parser = new JsonParser();
//                    request.setResponse(parser.parse(responseString).getAsJsonObject());
//                } else {
//                    in = connection.getErrorStream();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String errorString = reader.readLine();
//                    Log.d("MPG", errorString);
//                    if(MpgApplication.isUsageSharingAllowed()) {
//                        Map<String, String> errorMap = new HashMap<String, String>();
//                        errorMap.put("mpgApiError", errorString);
//                        FlurryAgent.logEvent(FlurryEvents.MPG_API_ERROR, errorMap);
//                    }
//                }
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                // Clean up.
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        return request;
    }

    public MpgApiRequest sendMpgGetAndWait(MpgApiRequest request) {
//        StringBuilder urlBuilder = new StringBuilder(request.getRequestPath()).append("?method=");
//        urlBuilder.append(request.getRequestMethod());
//        for(String paramKey : request.getParams().keySet()) {
//            urlBuilder.append(paramKey).append("=").append(request.getParam(paramKey)).append("&");
//        }
//
//        URL url = null;
//
//        try {
//            url = new URL(urlBuilder.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        if(url != null) {
//            OkHttpClient client = new OkHttpClient();
//            //todo headers
//            HttpURLConnection connection = client.open(url);
//            InputStream in = null;
//            BufferedReader reader = null;
//
//            try {
//                if(connection.getResponseCode() == 200) {
//                    in = connection.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String responseString = reader.readLine();
//                    Log.d("MPG", responseString);
//
//                    JsonParser parser = new JsonParser();
//                    request.setResponse(parser.parse(responseString).getAsJsonObject());
//                } else {
//                    in = connection.getErrorStream();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String errorString = reader.readLine();
//                    Log.d("MPG", errorString);
//                    if(MpgApplication.isUsageSharingAllowed()) {
//                        Map<String, String> errorMap = new HashMap<String, String>();
//                        errorMap.put("mpgApiError", errorString);
//                        FlurryAgent.logEvent(FlurryEvents.MPG_API_ERROR, errorMap);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }

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
