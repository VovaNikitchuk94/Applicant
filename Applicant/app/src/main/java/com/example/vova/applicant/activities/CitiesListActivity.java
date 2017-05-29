package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private ProgressBar mProgressBar;

    long mLongYearId = 0L;
    private String yearsCodeLink = "";

    @Override
    protected void iniActivity() {

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                yearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
            }
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_cities_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                parseData();
                Log.d("My","SwipeRefreshLayout -> parseData -> is start");
                mRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cities_list;
    }

    //TODO modified method
    private void setData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        mLongYearId = Long.parseLong(yearsCodeLink.
                substring(yearsCodeLink.length() - 2, yearsCodeLink.length() - 1));
        if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {
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
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickCityItem(CitiesInfo citiesInfo) {
        Intent intent = new Intent(this, CategoryUniversListActivity.class);
        intent.putExtra(CategoryUniversListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, citiesInfo);
        startActivity(intent);
    }

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
                        mProgressBar.setVisibility(View.VISIBLE);
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

                    Elements elementsHeadData = document.getElementsByClass("title-page");

                    if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {
                        for (Element link : links) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.addCity(new CitiesInfo(yearsId, citiesName, citiesLink));

                            String strHead = elementsHeadData.select(".title-description").first().text();
                            String strTime = elementsHeadData.select("small").text();

                            Log.d("My", "parse isEmpty -> " + citiesInfoEngine.getCityById(yearsId));
                            Log.d("My", "yearsId -> " + yearsId);

                            Log.d("My", "parse strHead -> " + strHead);
                            Log.d("My", "strTime -> " + strTime);
                        }
                    } else {
                        for (Element link : links) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink));

                            String strHead = elementsHeadData.select(".title-description").first().text();
                            String strTime = elementsHeadData.select("small").text();

                            Log.d("My", "parse update -> " + citiesInfoEngine.getCityById(yearsId));
                            Log.d("My", "yearsId -> " + yearsId);

                            Log.d("My", "parse strHead -> " + strHead);
                            Log.d("My", "strTime -> " + strTime);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


