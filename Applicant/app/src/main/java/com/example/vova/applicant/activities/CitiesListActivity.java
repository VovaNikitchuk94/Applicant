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
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.CitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CitiesListActivity extends BaseActivity implements CitiesInfoAdapter.OnClickCityItem {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private long mLongYearId = 0;
    private String mYearsCodeLink = "";

    @Override
    protected void iniActivity() {
        Log.d("My", "CitiesListActivity --------> iniActivity");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mYearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
                if (mYearsCodeLink != null) {
                    mLongYearId = Long.parseLong(mYearsCodeLink.
                            substring(mYearsCodeLink.length() - 2, mYearsCodeLink.length() - 1));
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_cities_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
//                TODO запускать только когда можно обновить данные
                Log.d("My", "SwipeRefreshLayout -> updateData -> is start");
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData(); is finished");
                }
                Log.d("My", "SwipeRefreshLayout -> updateData -> is finish");
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
        if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {

            //TODO проверять наличие интернета и уведомлять пользователя если его нет
            //TODO upgrade, check data in recyclerView
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            }
            Log.d("My", "CitiesListActivity -> parseData");
            parseData();

        } else {

            if (isDateComparison()) {
                Log.d("My", "CitiesListActivity -> isDateComparison  getData(citiesInfoEngine); ");
                getData(citiesInfoEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
//                    parseData();
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData(); is finished");
                }
            }
        }
    }

    //try use Kostya's method
    private void getData(CitiesInfoEngine citiesInfoEngine) {
        ArrayList<CitiesInfo> citiesInfos = citiesInfoEngine.getAllCitiesById(mLongYearId);
        for (CitiesInfo info : citiesInfoEngine.getAllCitiesById(mLongYearId)) {
            Log.d("My", "CitiesInfo info.getId() + info.getStrDateLastUpdate() -> " + info.getId() + " ~ " + info.getStrCityName() + " ~ " + info.getStrCityLink() +  " ~ " + info.getStrDateLastUpdate());
        }
        Log.d("My", "CitiesInfo citiesInfos.size() -> " + citiesInfoEngine.getAllCities().size());
//        ArrayList<CitiesInfo> citiesInfos = new ArrayList<>();
//        citiesInfos.clear();
//        citiesInfos.addAll(citiesInfoEngine.getAllCitiesById(mLongYearId));
        CitiesInfoAdapter citiesInfoAdapter = new CitiesInfoAdapter(citiesInfos);
//        citiesInfoAdapter.notifyDataSetChanged();
        citiesInfoAdapter.setOnClickCityInfoItem(CitiesListActivity.this);
        mRecyclerView.setAdapter(citiesInfoAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = citiesInfoEngine.getCityById(mLongYearId).getStrDateLastUpdate();

                Log.d("My", "isDateComparison dateAndTimeCities -> " + dateAndTime);

                if (parseDate.equals(dateAndTime)) {
                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
                    return true;
                }
                return false;
            }
        };

        FutureTask<Boolean> task = new FutureTask<>(callable);
        Thread t = new Thread(task);
        t.start();

        try {
            Log.d("My", " isDateComparison task.get() -> " + task.get());
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String parseDateAndTime() {
        Document document;
        String dateUpdateAndTime = null;
        try {
            document = Jsoup.connect(mYearsCodeLink).get();

            //TODO при обновлении нужно затирать всю цепочку связаных данных в БД

            //get timeUpdate and dateUpdate update page
            String strLastUpdatePage = document.select("div.title-page > small").text();
            Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage);
            String[] arrayTimeDate = strLastUpdatePage.split(" ");

            dateUpdateAndTime = arrayTimeDate[3] + "@" + arrayTimeDate[5];
            Log.d("My", "dateUpdateAndTime -> " + dateUpdateAndTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dateUpdateAndTime;
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
                if (Utils.connectToData(mYearsCodeLink) && mLongYearId != 0) {
                    parse(mLongYearId, citiesInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                            getData(citiesInfoEngine);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void parse(long yearsId, CitiesInfoEngine citiesInfoEngine) {
                Document document;
                try {
                    document = Jsoup.connect(mYearsCodeLink).get();

                    String dateUpdate = parseDateAndTime();

                    Element elementRegion = document.getElementById("region");
                    Elements linksByTag = elementRegion.getElementsByTag("a");

                    for (Element link : linksByTag) {

                        String citiesName = link.text();
                        String citiesLink = link.attr("abs:href");

                        citiesInfoEngine.addCity(new CitiesInfo(yearsId, citiesName, citiesLink, dateUpdate));

                        Log.d("My", "parse yearsId -> " + yearsId);
                        Log.d("My", "parse citiesName -> " + citiesName);
                        Log.d("My", "parse citiesLink -> " + citiesLink);
                        Log.d("My", "parse dateUpdate -> " + dateUpdate);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //try use single method for update data
    private void updateData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                if (Utils.connectToData(mYearsCodeLink) && mLongYearId != 0) {
                    update(mLongYearId, citiesInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                            getData(citiesInfoEngine);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void update(long yearsId, CitiesInfoEngine citiesInfoEngine) {
                Document document;
                try {
                    document = Jsoup.connect(mYearsCodeLink).get();

                    String dateUpdate = parseDateAndTime();
//                    String dateUpdate = "20/02/17@23:45";

                    Element elementRegion = document.getElementById("region");
                    Elements linksByTag = elementRegion.getElementsByTag("a");

                    for (Element link : linksByTag) {

                        String citiesName = link.text();
                        String citiesLink = link.attr("abs:href");

                        citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink,
                                dateUpdate));
                        Log.d("My", "update yearsId -> " + yearsId);
                        Log.d("My", "update citiesName -> " + citiesName);
                        Log.d("My", "update citiesLink -> " + citiesLink);
                        Log.d("My", "update dateUpdate -> " + dateUpdate);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


