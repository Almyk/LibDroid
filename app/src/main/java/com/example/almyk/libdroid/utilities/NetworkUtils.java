package com.example.almyk.libdroid.utilities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.almyk.libdroid.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    // Strings for libgen.io
    private final static String LIBGEN_BASE_URL = "http://libgen.io/search.php?";
    private final static String L_PARAM_REQUEST = "req";
    private final static String L_PARAM_SORT = "sort";
    private final static String L_PARAM_SORT_MODE = "sortmode";
    private final static String L_PARAM_RES_COUNT = "res";
    private final static String L_SORT_BY_YEAR = "year";
    private final static String L_SORT_BY_DESC = "DESC";
    private final static String L_RES_COUNT = "50";

    // Strings for b-ok.cc
    private final static String BOK_BASE_URL = "https://b-ok.cc/s/?";
    private final static String B_PARAM_REQUEST = "q";
    private final static String B_PARAM_LANGUAGE = "language";
    private final static String B_PARAM_EXTENSION = "extension";
    private final static String B_LANGUAGE = "english";
    private final static String B_EXTENSION = "epub";


    private static final int REQUEST_CODE_SHOW_RESPONSE_TEXT = 1;
    private static final String KEY_RESPONSE_TEXT = "KEY_RESPONSE_TEXT";
    private static Handler uiUpdater;

    private static Boolean mOnlyEpub;
    private static String mLanguage;

    public static void setFileType(Boolean onlyEpub){
        mOnlyEpub = onlyEpub;
    }
    public static void setLanguage(String language) { mLanguage = language.toLowerCase(); }

    public static URL buildUrl(String query){
        Uri.Builder builder = Uri.parse(BOK_BASE_URL).buildUpon()
                .appendQueryParameter(B_PARAM_REQUEST, query)
//                .appendQueryParameter(PARAM_SORT, SORT_BY_YEAR)
//                .appendQueryParameter(PARAM_SORT_MODE, SORT_BY_DESC)
//                .appendQueryParameter(PARAM_RES_COUNT, RES_COUNT)
                .appendQueryParameter(B_PARAM_LANGUAGE, mLanguage);

        if(mOnlyEpub)
            builder.appendQueryParameter(B_PARAM_EXTENSION, B_EXTENSION);

        Uri uri = builder.build();
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
                StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(url.toString()).get();

                    Elements links = doc.select("a[href*=book][title][id]");
                    for(Element link : links){
                        builder.append(link.ownText()).append("\n").append("----------\n");
                    }
                    String titles = builder.toString();
                    Message message = new Message();
                    message.what = REQUEST_CODE_SHOW_RESPONSE_TEXT;
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_RESPONSE_TEXT, titles);
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
