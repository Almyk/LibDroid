package com.example.almyk.libdroid.utilities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    final static String LIBGEN_BASE_URL = "http://libgen.io/search.php?";

    final static String PARAM_REQUEST = "req";
    final static String PARAM_SORT = "sort";
    final static String PARAM_SORT_MODE = "sortmode";

    final static String SORT_BY_YEAR = "year";
    final static String SORT_BY_DESC = "desc";

    private static final int REQUEST_CODE_SHOW_RESPONSE_TEXT = 1;
    private static final String KEY_RESPONSE_TEXT = "KEY_RESPONSE_TEXT";
    private static Handler uiUpdater;

    public static URL buildUrl(String query){
        Uri uri = Uri.parse(LIBGEN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_REQUEST, query)
                .appendQueryParameter(PARAM_SORT, SORT_BY_YEAR)
                .appendQueryParameter(PARAM_SORT_MODE, SORT_BY_DESC)
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

                try {
                    Document doc = Jsoup.connect(url.toString()).get();
                    String title = doc.title();

                    Message message = new Message();
                    message.what = REQUEST_CODE_SHOW_RESPONSE_TEXT;
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_RESPONSE_TEXT, title);
                    message.setData(bundle);
                    uiUpdater.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        sendHttpRequestThread.start();
    }

}
