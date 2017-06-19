package com.example.vova.applicant.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.utils.Utils;
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
        Log.d("My", "CitiesListActivity --------> iniActivity");

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
//                TODO запускать только когда можно обновить данные
//                if ()
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

        //TODO проверять наличие интернета и уведомлять пользователя если его нет
        //TODO upgrade, check data in recyclerView
        if (!isOnline(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            finish();
        }
        setData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cities_list;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

                    //TODO при обновлении нужно затирать всю цепочку связаных данных в БД

                    //get timeUpdate and dateUpdate update page
                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage );
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3];
                    String timeUpdate = arrayTimeDate[5];
                    Log.d("My", "dateUpdate -> " + dateUpdate + "\ntimeUpdate -> " + timeUpdate);

                    Element elementRegion = document.getElementById("region");
                    Elements linksByTag = elementRegion.getElementsByTag("a");

                    if (citiesInfoEngine.getAllCitiesById(yearsId).isEmpty()) {
                        for (Element link : linksByTag) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.addCity(new CitiesInfo(yearsId, citiesName, citiesLink,
                                    dateUpdate, timeUpdate));
                        }
                    } else {
                        for (Element link : linksByTag) {

                            String citiesName = link.text();
                            String citiesLink = link.attr("abs:href");

                            citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink,
                                    dateUpdate, timeUpdate));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


