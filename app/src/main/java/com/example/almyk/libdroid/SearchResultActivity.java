package com.example.almyk.libdroid;

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


    private static final int REQUEST_CODE_SHOW_RESPONSE_TEXT = 1;
    private static final String KEY_RESPONSE_TEXT = "KEY_RESPONSE_TEXT";
    private Handler uiUpdater = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        final String query = getIntent().getStringExtra("query");
        test = findViewById(R.id.tv_test);
        test.setText("Please wait while fetching: "+query);

        uiUpdater = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == REQUEST_CODE_SHOW_RESPONSE_TEXT)
                {
                    Bundle bundle = msg.getData();
                    if(bundle != null)
                    {
                        String responseText = bundle.getString(KEY_RESPONSE_TEXT);
                        test.setText(responseText);
                    }
                }
            }
        };






        Thread sendHttpRequestThread = new Thread() {

            @Override
            public void run() {
                URL url = NetworkUtils.buildUrl(query);
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
                    test.setText("Exception: " + e.toString());
                } finally {
                    urlConnection.disconnect();
                }
            }
        };
        sendHttpRequestThread.start();
    }
}
