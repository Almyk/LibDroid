package com.example.almyk.libdroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ViewBookActivity extends AppCompatActivity {

    private WebView mWebview;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        mWebview = findViewById(R.id.wv_book_view);

        mUrl = getIntent().getStringExtra("url");

        mWebview.setWebViewClient(new WebViewClient());
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mWebview.loadUrl(mUrl);
    }
}
