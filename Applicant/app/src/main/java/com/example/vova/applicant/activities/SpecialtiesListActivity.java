package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.SpecialitiesAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.SpecialityInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SpecialtiesListActivity extends BaseActivity implements
        SpecialitiesAdapter.OnClickSpecialityItem {

    // TODO rename all fields

    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";

    private TimeFormInfo mTimeFormInfo;

    private long mLongTimeFormId = 0;
    private long mLongDegree = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void iniActivity() {

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mTimeFormInfo = (TimeFormInfo) bundle.get(KEY_SPECIALITIES_LINK);
            }
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_speciality_swipe_refresh_layout);
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

        TextView textView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        textView.setText(mTimeFormInfo.getStrTimeFormName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSpecialityListActivity);
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
        return R.layout.activity_speciality_list;
    }

    private void setData() {
        mLongTimeFormId = mTimeFormInfo.getId();
        mLongDegree = Long.parseLong(mTimeFormInfo.getStrTimeFormLink().substring(mTimeFormInfo.getStrTimeFormLink().length() - 1));
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
            parseData();
        } else {
            getData(specialityInfoEngine);
        }
    }

    private void getData(SpecialityInfoEngine specialityInfoEngine) {
        ArrayList<SpecialtiesInfo> specialtiesInfos = specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree);
        SpecialitiesAdapter specialitiesAdapter = new SpecialitiesAdapter(specialtiesInfos);
        specialitiesAdapter.setOnClickSpecialityItem(SpecialtiesListActivity.this);
        mRecyclerView.setAdapter(specialitiesAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo) {
        Log.d("My", "specialtiesInfo.getStrLink().isEmpty()" + specialtiesInfo.getStrLink().isEmpty());
        if (!specialtiesInfo.getStrLink().isEmpty()) {
            Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
            intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY, specialtiesInfo);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseData() {

        new Thread(new Runnable() {

            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

            String mHtml = mTimeFormInfo.getStrTimeFormLink();

            @Override
            public void run() {
                if (Utils.connectToData(mTimeFormInfo.getStrTimeFormLink()) && mLongTimeFormId != 0) {
                    parse();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        getData(specialityInfoEngine);
                    }
                });
            }

            private void parse() {
                Document document;
                String specialty;
                String applications;
                String accepted;
                String amount;
                String newLink;

                String strCategory = mHtml.substring(mHtml.length() - 5, mHtml.length());

                try {
                    document = Jsoup.connect(mHtml).get();

                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > mHtml -> " + mHtml);

                    Element form = document.getElementById(strCategory);
                    Elements links = form.select("tbody > tr");
                    //TODO fix this shit
                    if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
                        for (Element link : links) {
                            Elements elements = link.getElementsByClass("button button-mini");
                            Elements tds = link.select("td");
                            if (isContains2015()) {
                                specialty = tds.get(0).text();
                                applications = tds.get(1).text();
                                amount = "ліцензований обсяг: " + tds.get(2).text();
                                newLink = elements.attr("abs:href");

                                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                        specialty, applications, amount, newLink));

                                Log.d("My", "newLink -> " + newLink);
                            } else {
                                specialty = tds.get(0).text();
                                applications = tds.get(1).select("span").text();
                                accepted = tds.get(1).select("nobr").text();
                                amount = tds.get(2).text();
                                newLink = elements.attr("abs:href");

                                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                        specialty, applications, accepted, amount, newLink));

                                Log.d("My", "newLink -> " + newLink);
                            }
                        }
                    } else {
                        for (Element link : links) {
                            Elements elements = link.getElementsByClass("button button-mini");
                            Elements tds = link.select("td");
                            if (isContains2015()) {
                                specialty = tds.get(0).text();
                                applications = tds.get(1).text();
                                amount = "ліцензований обсяг: " + tds.get(2).text();
                                newLink = elements.attr("abs:href");

                                specialityInfoEngine.updateSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                        specialty, applications, amount, newLink));
                            } else {
                                specialty = tds.get(0).text();
                                applications = tds.get(1).select("span").text();
                                accepted = tds.get(1).select("nobr").text();
                                amount = tds.get(2).text();
                                newLink = elements.attr("abs:href");

                                specialityInfoEngine.updateSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                        specialty, applications, accepted, amount, newLink));
                            }
                        }
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            private boolean isContains2015() {
                return mHtml.contains("2015");
            }
        }).start();
    }
}
