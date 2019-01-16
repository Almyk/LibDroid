package com.example.almyk.libdroid;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.almyk.libdroid.utilities.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private URL mUrl;

    private ArrayList<String> mBookTitleList = new ArrayList<>();
    private ArrayList<String> mBookDownload = new ArrayList<>();
    private ArrayList<String> mAuthorList = new ArrayList<>();
    private ArrayList<String> mPublishYearList = new ArrayList<>();
    private ArrayList<String> mFileTypeList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mRecyclerView = findViewById(R.id.rv_book_list);
        mProgressBar = findViewById(R.id.pb_getTitles);
        mPleaseWait = findViewById(R.id.tv_pls_wait);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final String query = getIntent().getStringExtra("query");
        mUrl = NetworkUtils.buildUrl(query);
        new getTitles().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private class getTitles extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
            mPleaseWait.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(mUrl.toString()).get();
                mProgressBar.incrementProgressBy(100);

                // get titles
                Elements links = doc.select("a[href*=book][class=tdn]");
                for(Element link : links){
                    String title = link.ownText();
                    mBookTitleList.add(title);
                    mProgressBar.incrementProgressBy(1);
                }

                // get page where you can download the book
                Elements dlPages = doc.select("a[href][title=Download book]");
                for(Element page : dlPages){
                    String url = page.attr("abs:href");
                    mBookDownload.add(url);
                    mProgressBar.incrementProgressBy(1);
                }

                // get authors
                Elements authors = doc.select("div[class=authors]");
                for(Element author : authors){
                    int i, j;
                    String name = author.text();

                    // show only first author
                    i = name.indexOf('[');
                    j = name.indexOf(']');
                    if(i != -1 && j != -1){
                        name = name.substring(0,i) + name.substring(j+1);
                    }

                    i = name.indexOf(',');
                    j = name.indexOf('&');
                    if((i < j || j == -1) && i != -1){
                        name = name.substring(0, i) + " et al.";
                    }
                    else if((i > j || i == -1) && j != -1) {
                        name = name.substring(0, j) + " et al.";
                    }

                    mAuthorList.add(name);
                    mProgressBar.incrementProgressBy(1);
                }

                // get publish year
                Elements publishedYears = doc.select("div[class*=property_year]");
                for(Element pubYear : publishedYears){
                    String year = pubYear.text();
                    mPublishYearList.add(year);
                    mProgressBar.incrementProgressBy(1);
                }

                // get filetype
                Elements fileTypes = doc.select("div[class*=property__file]");
                for(Element fileType: fileTypes){
                    String type = fileType.text();
                    int i = type.indexOf(',');
                    type = type.substring(0,i);
                    mFileTypeList.add(type);
                    mProgressBar.incrementProgressBy(1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mPleaseWait.setVisibility(View.INVISIBLE);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            DataAdapter mDataAdapter = new DataAdapter(mBookTitleList, mBookDownload, mAuthorList, mPublishYearList, mFileTypeList);
            mRecyclerView.setAdapter(mDataAdapter);
        }
    }
}
