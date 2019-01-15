package com.example.almyk.libdroid;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
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

    private ArrayList<String> mBookTitleList = new ArrayList<>();
    private ArrayList<String> mBookDownloadList = new ArrayList<>();
    private ArrayList<String> mAuthorList = new ArrayList<>();

    public DataAdapter(ArrayList<String> bookTitleList, ArrayList<String> bookDownloadList, ArrayList<String> authorList){
        mBookTitleList = bookTitleList;
        mBookDownloadList = bookDownloadList;
        mAuthorList = authorList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_book_title;
        private TextView tv_author_name;
        private ImageButton ib_book_download;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_title = itemView.findViewById(R.id.tv_book_title);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            ib_book_download = itemView.findViewById(R.id.ib_download);

            ib_book_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Uri uri = Uri.parse(mBookDownloadList.get(pos));
                    // TODO : make it work using downloadmanager
//                    DownloadManager.Request dlRequest = new DownloadManager.Request(uri);
//                    dlRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mBookTitleList.get(pos));
//                    dlRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    DownloadManager dm = (DownloadManager) v.getContext().getSystemService(DOWNLOAD_SERVICE);
//                    dm.enqueue(dlRequest);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);
                }
            });
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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_book_title.setText(mBookTitleList.get(i));
        myViewHolder.tv_author_name.setText(mAuthorList.get(i));
    }

    @Override
    public int getItemCount() {
        return mBookTitleList.size();
    }


}
