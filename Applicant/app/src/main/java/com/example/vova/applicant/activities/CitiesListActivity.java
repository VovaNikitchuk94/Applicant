package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.CitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CitiesListActivity extends AppCompatActivity implements CitiesInfoAdapter.OnClickCityItem {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private CitiesInfoAdapter mCitiesInfoAdapter;
    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();

    private String yearsCodeLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        mTextView = (TextView) findViewById(R.id.textViewСhooseCityCitiesActivity);
        mTextView.setText(getText(R.string.chooseCityMainActivity));

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                yearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewCitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        new ParseCitiesList().execute();
    }

    @Override
    public void onClickCityItem(long nIdCity) {
        Intent intent = new Intent(this, UniversitiesListActivity.class);
        intent.putExtra(UniversitiesListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, nIdCity);
        startActivity(intent);
    }

    public class ParseCitiesList extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(CitiesListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.textResourceLoading));
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());

            if (citiesInfoEngine.getAllCities().isEmpty()) {
                Document document;
                try {
                    document = Jsoup.connect(yearsCodeLink).get();
                    Element elementRegion = document.getElementById("region");
                    Elements links = elementRegion.getElementsByTag("a");
                    for (Element link : links) {

                        String citiesName = link.text();
                        String citiesLink = link.attr("abs:href");

                        citiesInfoEngine.addCity(new CitiesInfo(citiesName, citiesLink));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
            mCitiesInfos = citiesInfoEngine.getAllCities();
            mCitiesInfoAdapter = new CitiesInfoAdapter(mCitiesInfos);
            mCitiesInfoAdapter.setOnClickCityInfoItem(CitiesListActivity.this);
            mRecyclerView.setAdapter(mCitiesInfoAdapter);
            progressDialog.dismiss();

        }
    }

}


