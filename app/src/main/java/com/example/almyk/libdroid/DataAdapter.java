package com.example.almyk.libdroid;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<String> mBookTitleList;
    private ArrayList<String> mBookDownloadList;
    private ArrayList<String> mAuthorList;
    private ArrayList<String> mPublishedYearList;
    private ArrayList<String> mFileTypeList;

    public DataAdapter(ArrayList<String> bookTitleList,
                       ArrayList<String> bookDownloadList,
                       ArrayList<String> authorList,
                       ArrayList<String> publishedYearList,
                       ArrayList<String> fileTypeList){

        mBookTitleList = bookTitleList;
        mBookDownloadList = bookDownloadList;
        mAuthorList = authorList;
        mPublishedYearList = publishedYearList;
        mFileTypeList = fileTypeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_book_title;
        private TextView tv_author_name;
        private TextView tv_year;
        private TextView tv_file;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_book_title = itemView.findViewById(R.id.tv_book_title);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_file = itemView.findViewById(R.id.tv_file);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        if(mBookTitleList.size() > i)
            myViewHolder.tv_book_title.setText(mBookTitleList.get(i));
        if(mAuthorList.size() > i)
            myViewHolder.tv_author_name.setText(mAuthorList.get(i));
        if(mPublishedYearList.size() > i)
            myViewHolder.tv_year.setText(mPublishedYearList.get(i));
        if(mFileTypeList.size() > i)
            myViewHolder.tv_file.setText(mFileTypeList.get(i));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = myViewHolder.getAdapterPosition();
                Uri uri = Uri.parse(mBookDownloadList.get(pos));
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.enableUrlBarHiding();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                customTabsIntent.launchUrl(v.getContext(), uri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookTitleList.size();
    }


}
