package com.example.almyk.libdroid;

import android.net.UrlQuerySanitizer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.almyk.libdroid.utilities.NetworkUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchResultActivity extends AppCompatActivity {

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        final String query = getIntent().getStringExtra("query");
        test = findViewById(R.id.tv_test);
        test.setText("Please wait while fetching: "+query);

        URL url = NetworkUtils.buildUrl(query);
        NetworkUtils.getRequestFromHttpUrl(url, test);
    }
}
