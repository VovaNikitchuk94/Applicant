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
import com.example.vova.applicant.adapters.SpecialitiesAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.SpecialityInfoEngine;
import com.example.vova.applicant.utils.Utils;

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
    public static final String KEY_SPECIALITIES_TITLE_STRING = "KEY_SPECIALITIES_TITLE_STRING";

    private TimeFormInfo mTimeFormInfo;

    private long mLongTimeFormId = 0;
    private long mLongDegree = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void iniActivity() {
        Log.d("My", "SpecialtiesListActivity --------> iniActivity");
        String strTitle = "";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mTimeFormInfo = (TimeFormInfo) bundle.get(KEY_SPECIALITIES_LINK);
                strTitle = bundle.getString(KEY_SPECIALITIES_TITLE_STRING, "");
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_speciality_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                parseData();
                Log.d("My", "SwipeRefreshLayout -> parseData -> is start");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        if (!strTitle.equals("")) {
            strTitle = strTitle.substring(0, strTitle.indexOf(" ("));
            textView.setText(strTitle + ", " + mTimeFormInfo.getStrTimeFormName());
        } else {
            textView.setText(mTimeFormInfo.getStrTimeFormName());
        }

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
                String accepted = null;
                String recommended = null;
                String licenseOrder = null;
                String volumeOrder = null;
                String exam = null;
                String newLink = null;

                String strCategory = mHtml.substring(mHtml.length() - 5, mHtml.length());

                try {
                    document = Jsoup.connect(mHtml).get();

                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > mHtml -> " + mHtml);

                    Element form = document.getElementById(strCategory);
                    Elements links = form.select("tbody > tr");
                    //TODO правильно обработать загрузку данных
                    //TODO пустые данные о (рекомендовано, зараховано) получают  поля с прошлых не пустых данных
                    if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
                        for (Element link : links) {
                            Elements elements = link.getElementsByClass("button button-mini");
                            Elements tdElements = link.select("td");

//                            if (isContains2015()) {
////                                specialty = tdElements.get(0).text();
////                                applications = tdElements.get(1).text();
////                                amount = "ліцензований обсяг: " + tdElements.get(2).text();
////                                newLink = elements.attr("abs:href");
////
////                                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
////                                        specialty, applications, amount, newLink));
//
////                                Log.d("My", "newLink -> " + newLink);
//                            } else {
                            //try to get data from speciality
                            specialty = (tdElements.get(0).toString()).replaceAll("(?i)<td[^>]*>", "")
                                    .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");
                            Log.d("My", "specialty -> " + specialty);

                            //try get more data applications
                            applications = tdElements.get(1).select("span").text();
                            Log.d("My", "applications -> " + tdElements.get(1).select("span").text());
                            if (tdElements.get(1).select("nobr").size() == 2) {
                                accepted = tdElements.get(1).select("nobr").get(0).text();
                                recommended = tdElements.get(1).select("nobr").get(1).text();
                                Log.d("My", "applications -> " + tdElements.get(1).select("nobr").get(0).text());
                                Log.d("My", "applications -> " + tdElements.get(1).select("nobr").get(1).text());
                            } else if (tdElements.get(1).select("nobr").size() == 1) {
                                Log.d("My", "applications -> " + tdElements.get(1).select("nobr").get(0).text());
                                recommended = tdElements.get(1).select("nobr").get(0).text();
                            } else {
                                Log.d("My", "get more data applications -> is empty");
                            }

                            //try get more data amount
                            if (tdElements.get(2).select("nobr").size() == 2) {
                                licenseOrder = tdElements.get(2).select("nobr").get(0).text();
                                volumeOrder = tdElements.get(2).select("nobr").get(1).text();

                                Log.d("My", "get more data amount -> " + tdElements.get(2).select("nobr").get(0).text());
                                Log.d("My", "get more data amount -> " + tdElements.get(2).select("nobr").get(1).text());
                            } else if (tdElements.get(2).select("nobr").size() == 1) {
                                Log.d("My", "get more data amount -> " + tdElements.get(2).select("nobr").get(0).text());
                                volumeOrder = tdElements.get(2).select("nobr").get(0).text();
                            } else {
                                Log.d("My", "get more data amount -> is empty");
                            }

                            //attempt to get more data from exams
                            exam = (tdElements.get(3).toString()).replaceAll("(?i)<td[^>]*>", "")
                                    .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");
                            Log.d("My", "get more data amount -> " + exam);

                            newLink = elements.attr("abs:href");

                            specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                    specialty, applications, accepted, recommended, licenseOrder, volumeOrder, exam, newLink));

//                            }
                        }
                    } else {
                        for (Element link : links) {
                            Elements elements = link.getElementsByClass("button button-mini");
                            Elements tds = link.select("td");
//                            if (isContains2015()) {
//                                specialty = tds.get(0).text();
//                                applications = tds.get(1).text();
//                                exam = tds.get(3).text();
//                                newLink = elements.attr("abs:href");
//
//                                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
//                                        specialty, applications, accepted, recommended, licenseOrder, volumeOrder, exam, newLink));
//                            } else {
                            specialty = tds.get(0).text();
                            applications = tds.get(1).select("span").text();
                            accepted = tds.get(1).select("nobr").text();
                            exam = tds.get(3).text();
                            newLink = elements.attr("abs:href");

                            specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                    specialty, applications, accepted, recommended, licenseOrder, volumeOrder, exam, newLink));
//                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            private boolean isContains2015() {
//                return mHtml.contains("2015");
//            }

        }).start();
    }
}
