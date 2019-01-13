package com.example.almyk.libdroid.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    final static String LIBGEN_BASE_URL = "http://libgen.io/search.php?";

    final static String PARAM_REQUEST = "req";

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

}
