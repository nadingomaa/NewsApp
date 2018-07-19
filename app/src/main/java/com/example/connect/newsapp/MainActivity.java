package com.example.connect.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class  MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private  static final String NEWS_URL="https://content.guardianapis.com/search?api-key=fa14f9b7-fd04-4fcd-986d-541317727a76";
    private TextView emptyTextView;
    private NewsAdapter adapter;
    String getNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyTextView=findViewById(R.id.emptyTextView);

        ListView list=findViewById(R.id.list);
        adapter=new NewsAdapter(this,new ArrayList<News>());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNew=adapter.getItem(position);

                Uri earthquakeUri= Uri.parse(currentNew.getWebUrl());

                Intent webSiteIntent =new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(webSiteIntent);
            }
        });


        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();

        if (info != null &&info.isConnected()) {
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);}
        else{
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText("no internet ");
        }

        emptyTextView=findViewById(R.id.emptyTextView);
        list.setEmptyView(emptyTextView);

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key), " ");
        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key), " ");

        Uri baseUri=Uri.parse(NEWS_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();

        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("q",topic);
        uriBuilder.appendQueryParameter("sectionName",section);
        return new NewsLoader(this,uriBuilder.toString());



    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        if(data!=null&&!data.isEmpty()){
            adapter.addAll(data);
        }

        View progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
        emptyTextView.setText("No news found");

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
