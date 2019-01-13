package com.example.almyk.libdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mSearchButton;
    private EditText mQueryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = findViewById(R.id.ib_search);
        mSearchButton.setOnClickListener(this);

        mQueryText = findViewById(R.id.et_search_input);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch(viewID){
            case R.id.ib_search:
                String query = mQueryText.getText().toString();
                if(!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(this, SearchResultActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                } else {
                    // TODO : show a toast with error message
                }
                break;
            default:
                break;
        }
    }
}
