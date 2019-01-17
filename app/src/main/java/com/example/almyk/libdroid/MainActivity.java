package com.example.almyk.libdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        TextView.OnEditorActionListener {


    private ImageButton mSearchButton;
    private EditText mQueryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = findViewById(R.id.ib_search);
        mSearchButton.setOnClickListener(this);

        mQueryText = findViewById(R.id.et_search_input);
        mQueryText.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch(viewID){
            case R.id.ib_search:
                launchSearchResult();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch(actionId) {
            case EditorInfo.IME_ACTION_DONE:
                launchSearchResult();
                return true;
        }
        return false;
    }

    private void launchSearchResult(){
        String query = mQueryText.getText().toString();
        if(!TextUtils.isEmpty(query)) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("query", query);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Please enter a query and try again", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
