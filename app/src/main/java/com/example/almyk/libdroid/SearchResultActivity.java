package com.example.almyk.libdroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.almyk.libdroid.utilities.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    //private TextView test;

    private URL mUrl;

    private ArrayList<String> mBookTitleList = new ArrayList<>();
    private ArrayList<String> mBookDownload = new ArrayList<>();

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mProgressBar = findViewById(R.id.pb_getTitles);

        final String query = getIntent().getStringExtra("query");
        mUrl = NetworkUtils.buildUrl(query);
        new getTitles().execute();
    }

    private class getTitles extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                int progress = 0;
                Document doc = Jsoup.connect(mUrl.toString()).get();

                Elements links = doc.select("a[href*=book][class=tdn]");
                for(Element link : links){
                    String title = link.ownText();
                    mBookTitleList.add(title);
                    progress++;
                    mProgressBar.setProgress(progress);
                }

                links = doc.select("a[href][title=Download book]");
                for(Element link : links){
                    String url = link.attr("abs:href");
                    mBookDownload.add(url);
                    progress++;
                    mProgressBar.setProgress(progress);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.INVISIBLE);
            RecyclerView mRecyclerView = findViewById(R.id.rv_book_list);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            DataAdapter mDataAdapter = new DataAdapter(mBookTitleList, mBookDownload);
            mRecyclerView.setAdapter(mDataAdapter);
        }
    }
}
