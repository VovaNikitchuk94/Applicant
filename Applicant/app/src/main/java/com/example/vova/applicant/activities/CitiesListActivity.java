package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.CitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CitiesListActivity extends BaseActivity implements CitiesInfoAdapter.OnClickCityItem {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    long mLongYearId = 0L;
    private String yearsCodeLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        drawerAndToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                yearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_cities_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                parseData();
                Log.d("My","SwipeRefreshLayout -> parseData -> is start");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        new ParseCitiesList().execute(yearsCodeLink);
////                        parseData();
//                        Log.d("My","SwipeRefreshLayout -> parseData -> is start");
//                    }
//                }, 0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textView = (TextView) findViewById(R.id.textViewСhooseCityCitiesActivity);
        textView.setText(getText(R.string.chooseCityMainActivity));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewCitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

//    @Override
//    public void drawerAndToolbar() {
//        super.drawerAndToolbar();
//    }

    //TODO modified method
    private void setData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        mLongYearId = Long.parseLong(yearsCodeLink.
                substring(yearsCodeLink.length() - 2, yearsCodeLink.length() - 1));
        if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {
//            new ParseCitiesList().execute(yearsCodeLink);
            parseData();
            Log.d("My","setData -> parseData -> is start");
        } else {

            getData(citiesInfoEngine);
        }
    }

    private void getData(CitiesInfoEngine citiesInfoEngine) {
        ArrayList<CitiesInfo> citiesInfos = citiesInfoEngine.getAllCitiesById(mLongYearId);
        CitiesInfoAdapter citiesInfoAdapter = new CitiesInfoAdapter(citiesInfos);
        citiesInfoAdapter.setOnClickCityInfoItem(CitiesListActivity.this);
        mRecyclerView.setAdapter(citiesInfoAdapter);
    }

    @Override
    public void onClickCityItem(CitiesInfo citiesInfo) {
        Intent intent = new Intent(this, CategoryUniversListActivity.class);
        intent.putExtra(CategoryUniversListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, citiesInfo);
        startActivity(intent);
    }

//    private class ParseCitiesList extends AsyncTask<String, Void, Void> {
//        ProgressDialog progressDialog = new ProgressDialog(CitiesListActivity.this);
////        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressBar.setVisibility(ProgressBar.VISIBLE);
//            progressDialog.setMessage(getString(R.string.textResourceLoading));
//            progressDialog.setIndeterminate(false);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setCancelable(true);
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(String... urls) {
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
//            if (Utils.connectToData(urls[0]) && mLongYearId != 0) {
//                parse(mLongYearId, citiesInfoEngine);
//            }
//            return null;
//        }
//
//        private void parse(long yearsId, CitiesInfoEngine citiesInfoEngine) {
//            Document document;
//            try {
//                document = Jsoup.connect(yearsCodeLink).get();
//                Element elementRegion = document.getElementById("region");
//                Elements links = elementRegion.getElementsByTag("a");
//                if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {
//                    for (Element link : links) {
//
//                        String citiesName = link.text();
//                        String citiesLink = link.attr("abs:href");
//
//                        citiesInfoEngine.addCity(new CitiesInfo(yearsId, citiesName, citiesLink));
//
//                        Log.d("My", "parse isEmpty -> " + citiesInfoEngine.getCityById(yearsId));
//                        Log.d("My", "yearsId -> " + yearsId);
//                    }
//                } else {
//                    for (Element link : links) {
//
//                        String citiesName = link.text();
//                        String citiesLink = link.attr("abs:href");
//
//                        citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink));
//
//                        Log.d("My", "parse update -> " + citiesInfoEngine.getCityById(yearsId));
//                        Log.d("My", "yearsId -> " + yearsId);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
//            getData(citiesInfoEngine);
////            progressBar.setVisibility(ProgressBar.INVISIBLE);
//            progressDialog.dismiss();
//
//        }
//    }

    private void parseData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                if (Utils.connectToData(yearsCodeLink) && mLongYearId != 0) {
                    parse(mLongYearId, citiesInfoEngine);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                        getData(citiesInfoEngine);
                    }
                });
            }

            private void parse(long yearsId, CitiesInfoEngine citiesInfoEngine) {
                Document document;
                try {
                    document = Jsoup.connect(yearsCodeLink).get();
                    Element elementRegion = document.getElementById("region");
                    Elements links = elementRegion.getElementsByTag("a");
                    if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {
                        for (Element link : links) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.addCity(new CitiesInfo(yearsId, citiesName, citiesLink));

                            Log.d("My", "parse isEmpty -> " + citiesInfoEngine.getCityById(yearsId));
                            Log.d("My", "yearsId -> " + yearsId);
                        }
                    } else {
                        for (Element link : links) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink));

                            Log.d("My", "parse update -> " + citiesInfoEngine.getCityById(yearsId));
                            Log.d("My", "yearsId -> " + yearsId);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


