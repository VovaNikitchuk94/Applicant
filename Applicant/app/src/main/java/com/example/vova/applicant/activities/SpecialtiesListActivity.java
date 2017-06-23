package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SpecialtiesListActivity extends BaseActivity implements
        SpecialitiesAdapter.OnClickSpecialityItem {

    // TODO rename all fields

    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";
    public static final String KEY_SPECIALITIES_TITLE_STRING = "KEY_SPECIALITIES_TITLE_STRING";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView searchView;

    private TimeFormInfo mTimeFormInfo;
    private SpecialitiesAdapter mSpecialitiesAdapter;
    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();

    private long mLongTimeFormId = 0;
    private long mLongDegree = 0;
    private String mTimeFormCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "SpecialtiesListActivity --------> initActivity");
        String strTitle = "";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mTimeFormInfo = (TimeFormInfo) bundle.get(KEY_SPECIALITIES_LINK);
                strTitle = bundle.getString(KEY_SPECIALITIES_TITLE_STRING, "");
                if (mTimeFormInfo != null) {
                    mLongTimeFormId = mTimeFormInfo.getId();
                    mTimeFormCodeLink = mTimeFormInfo.getStrTimeFormLink();
                    mLongDegree = Long.parseLong(mTimeFormInfo.getStrTimeFormLink().substring(mTimeFormInfo.getStrTimeFormLink().length() - 1));
                    Log.d("My", "SpecialtiesListActivity -> mLongDegree -> " + mLongDegree);
                }
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
//                Log.d("My", "SwipeRefreshLayout -> updateData -> is start");
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
//                    Log.d("My", "SpecialtiesListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
//                    Log.d("My", "SpecialtiesListActivity -> (isDateComparison()) updateData(); is finished");
                }
//                Log.d("My", "SwipeRefreshLayout -> updateData -> is finish");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        //зачем он нужен?
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//
//                return true;
//            }
//        });
//        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                Log.d("My", "onMenuItemActionExpand");
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Log.d("My", "onMenuItemActionCollapse");
//                updateInfo("");
//                return true;
//            }
//        });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                updateInfo(query);
//                if(!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
                searchView.setIconified(false);
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    updateInfo(newText);
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d("My", "setOnQueryTextFocusChangeListener onFocusChange");
//                Log.d("My", "setOnQueryTextFocusChangeListener hasFocus -> "  + hasFocus);
                if (!hasFocus) {
                    searchView.onActionViewCollapsed();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setData() {

        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            }
//            Log.d("My", "SpecialtiesListActivity -> parseData");
            parseData();

        } else {
            if (isDateComparison()) {
//                Log.d("My", "SpecialtiesListActivity -> isDateComparison  getData(citiesInfoEngine); ");
                getData(specialityInfoEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
//                    Log.d("My", "SpecialtiesListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
//                    Log.d("My", "SpecialtiesListActivity -> (isDateComparison()) updateData(); is finished");

                }
            }
        }
    }

    private void updateInfo(){
        mSpecialtiesInfos.clear();
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        if (mSpecialitiesAdapter != null) {
            mSpecialitiesAdapter.notifyDataSetChanged();
        }
    }

    private void updateInfo(String search){
        mSpecialtiesInfos.clear();
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        if(search.isEmpty()){
            mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        } else {
            mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesBySearchString(mLongTimeFormId, mLongDegree, search));
        }
        if (mSpecialitiesAdapter != null) {
            mSpecialitiesAdapter.notifyDataSetChanged();
        }
    }

    private void getData(SpecialityInfoEngine specialityInfoEngine) {
        mSpecialtiesInfos.clear();
        mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        mSpecialitiesAdapter = new SpecialitiesAdapter(mSpecialtiesInfos);
//        Log.d("My", "specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree) size - > " + specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree).size());
//        Log.d("My", "specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId) size - > " + specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).size());
//        for (SpecialtiesInfo info : specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree))  {
//            Log.d("My", "info.getStrSpecialty() + info.getStrDateLastUpdate() - > " + info.getStrSpecialty() + " ~ " + info.getStrDateLastUpdate());
//        }
        mSpecialitiesAdapter.setOnClickSpecialityItem(SpecialtiesListActivity.this);
        mRecyclerView.setAdapter(mSpecialitiesAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo) {
//        Log.d("My", "specialtiesInfo.getStrLink().isEmpty()" + specialtiesInfo.getStrLink().isEmpty());
        if (!specialtiesInfo.getStrLink().isEmpty()) {
            Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
            intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY, specialtiesInfo);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = specialityInfoEngine.getSpecialityById(mLongTimeFormId).getStrDateLastUpdate();
                String dateAndTime2 = specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree).get(0).getStrDateLastUpdate();
                String dateAndTime3 = parseDateAndTime();

//                Log.d("My", "isDateComparison dateAndTimeCities -> " + dateAndTime);
//                Log.d("My", "isDateComparison parseDate -> " + parseDate);
//                Log.d("My", "isDateComparison dateAndTime2 -> " + dateAndTime2);
//                Log.d("My", "isDateComparison dateAndTime3 -> " + dateAndTime3);

                //                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
//                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + false);
                return parseDate.equals(dateAndTime);

            }
        };

        FutureTask<Boolean> task = new FutureTask<>(callable);
        Thread t = new Thread(task);
        t.start();

        try {
//            Log.d("My", " isDateComparison task.get() -> " + task.get());
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
            document = Jsoup.connect(mTimeFormCodeLink).get();

            //TODO при обновлении нужно затирать всю цепочку связаных данных в БД

            //get timeUpdate and dateUpdate update page
            String strLastUpdatePage = document.select("div.title-page > small").text();
//            Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage);
            String[] arrayTimeDate = strLastUpdatePage.split(" ");

            dateUpdateAndTime = arrayTimeDate[3] + "@" + arrayTimeDate[5];
//            Log.d("My", "dateUpdateAndTime -> " + dateUpdateAndTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dateUpdateAndTime;
    }

    private void parseData() {

        new Thread(new Runnable() {

            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mTimeFormInfo.getStrTimeFormLink()) && mLongTimeFormId != 0) {
                    parse();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(specialityInfoEngine);
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
                String dateUpdate = null;

                String strCategory = mTimeFormCodeLink.substring(mTimeFormCodeLink.length() - 5, mTimeFormCodeLink.length());

                try {
                    document = Jsoup.connect(mTimeFormCodeLink).get();

//                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
//                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > mHtml -> " + mTimeFormCodeLink);

                    Element form = document.getElementById(strCategory);
                    Elements links = form.select("tbody > tr");
                    //TODO правильно обработать загрузку данных
                    //TODO пустые данные о (рекомендовано, зараховано) получают  поля с прошлых не пустых данных
//                    if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
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
//                            Log.d("My", "specialty -> " + specialty);

                            // get more data applications
                            applications = tdElements.get(1).select("span").text();
                            if (tdElements.get(1).select("nobr").size() == 2) {
                                accepted = tdElements.get(1).select("nobr").get(0).text();
                                recommended = tdElements.get(1).select("nobr").get(1).text();
                            } else if (tdElements.get(1).select("nobr").size() == 1) {
                                recommended = tdElements.get(1).select("nobr").get(0).text();
                            } else {
                                recommended = null;
                            }

                            // get more data amount
                            if (tdElements.get(2).select("nobr").size() == 2) {
                                licenseOrder = tdElements.get(2).select("nobr").get(0).text();
                                volumeOrder = tdElements.get(2).select("nobr").get(1).text();
                            } else if (tdElements.get(2).select("nobr").size() == 1) {
                                volumeOrder = tdElements.get(2).select("nobr").get(0).text();
                            } else {
                                volumeOrder = null;
                            }

                            //attempt to get more data from exams
                            exam = (tdElements.get(3).toString()).replaceAll("(?i)<td[^>]*>", "")
                                    .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");

                            newLink = elements.attr("abs:href");

                            dateUpdate = parseDateAndTime();
//                            Log.d("My", "SpecialtiesListActivity get more data dateUpdate -> " + dateUpdate);

                            specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                    specialty, applications, accepted, recommended, licenseOrder, volumeOrder, exam, newLink, dateUpdate));

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

    private void updateData() {

        new Thread(new Runnable() {

            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mTimeFormInfo.getStrTimeFormLink()) && mLongTimeFormId != 0) {
                    update();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(specialityInfoEngine);
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

            private void update() {
                Document document;

                String specialty;
                String applications;
                String accepted = null;
                String recommended = null;
                String licenseOrder = null;
                String volumeOrder = null;
                String exam = null;
                String newLink = null;
                String dateUpdate = null;

                String strCategory = mTimeFormCodeLink.substring(mTimeFormCodeLink.length() - 5, mTimeFormCodeLink.length());

                try {
                    document = Jsoup.connect(mTimeFormCodeLink).get();

//                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
//                    Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > mHtml -> " + mTimeFormCodeLink);

                    Element form = document.getElementById(strCategory);
                    Elements links = form.select("tbody > tr");
                    //TODO правильно обработать загрузку данных
                    //TODO загружает данные всех бакалавров, например Гетьмана веч форма -> бакалавр показало всех бакалавров, нужно открывать не только по специальности а еще и по timeForm
                    //TODO пустые данные о (рекомендовано, зараховано) получают  поля с прошлых не пустых данных
                        for (Element link : links) {
                            Elements elements = link.getElementsByClass("button button-mini");
                            Elements tdElements = link.select("td");

                            //try to get data from speciality
                            specialty = (tdElements.get(0).toString()).replaceAll("(?i)<td[^>]*>", "")
                                    .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");
//                            Log.d("My", "SpecialtiesListActivity specialty -> " + specialty);

                            // get more data applications
                            applications = tdElements.get(1).select("span").text();
                            if (tdElements.get(1).select("nobr").size() == 2) {
                                accepted = tdElements.get(1).select("nobr").get(0).text();
                                recommended = tdElements.get(1).select("nobr").get(1).text();
                            } else if (tdElements.get(1).select("nobr").size() == 1) {
                                recommended = tdElements.get(1).select("nobr").get(0).text();
                            } else {
                                recommended = null;
                            }

                            // get more data amount
                            if (tdElements.get(2).select("nobr").size() == 2) {
                                licenseOrder = tdElements.get(2).select("nobr").get(0).text();
                                volumeOrder = tdElements.get(2).select("nobr").get(1).text();
                            } else if (tdElements.get(2).select("nobr").size() == 1) {
                                volumeOrder = tdElements.get(2).select("nobr").get(0).text();
                            } else {
                                volumeOrder = null;
                            }

                            //attempt to get more data from exams
                            exam = (tdElements.get(3).toString()).replaceAll("(?i)<td[^>]*>", "")
                                    .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");

                            newLink = elements.attr("abs:href");

                            dateUpdate = parseDateAndTime();
//                            dateUpdate = "test";
//                            Log.d("My", "SpecialtiesListActivity get more data dateUpdate -> " + dateUpdate);

                            specialityInfoEngine.updateSpeciality(new SpecialtiesInfo(mLongTimeFormId, mLongDegree,
                                    specialty, applications, accepted, recommended, licenseOrder, volumeOrder, exam, newLink, dateUpdate));
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
