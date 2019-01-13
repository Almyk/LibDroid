package com.example.almyk.libdroid.utilities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    final static String LIBGEN_BASE_URL = "http://libgen.io/search.php?";

    final static String PARAM_REQUEST = "req";

    private static final int REQUEST_CODE_SHOW_RESPONSE_TEXT = 1;
    private static final String KEY_RESPONSE_TEXT = "KEY_RESPONSE_TEXT";
    private static Handler uiUpdater;

    public static URL buildUrl(String query){
        Uri uri = Uri.parse(LIBGEN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_REQUEST, query)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static void getRequestFromHttpUrl(final URL url, final TextView view){

        uiUpdater = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == REQUEST_CODE_SHOW_RESPONSE_TEXT)
                {
                    Bundle bundle = msg.getData();
                    if(bundle != null)
                    {
                        String responseText = bundle.getString(KEY_RESPONSE_TEXT);
                        view.setText(responseText);
                    }
                }
            }
        };

        Thread sendHttpRequestThread = new Thread(){
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;

                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    Message message = new Message();
                    message.what = REQUEST_CODE_SHOW_RESPONSE_TEXT;
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_RESPONSE_TEXT, line);
                    message.setData(bundle);
                    uiUpdater.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        };
        sendHttpRequestThread.start();
    }

}
