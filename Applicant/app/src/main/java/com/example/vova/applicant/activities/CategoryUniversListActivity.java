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
import com.example.vova.applicant.utils.Utils;
import com.example.vova.applicant.adapters.CategoryUniversAdapter;
import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.CategoryUniversEngine;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CategoryUniversListActivity extends BaseActivity implements
        CategoryUniversAdapter.OnClickCategoryUniversItem {

    private CitiesInfo mCitiesInfo;

    private long mLongCityId = 0;
    private String mCityCodeLink = "";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category_univers_list;
    }

    @Override
    protected void iniActivity() {
        Log.d("My", "CategoryUniversListActivity --------> iniActivity");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCitiesInfo = (CitiesInfo) bundle.get(INTENT_KEY_UNIVERSITY_ACTIVITY);
                if (mCitiesInfo != null) {
                    mLongCityId = mCitiesInfo.getId();
                    mCityCodeLink = mCitiesInfo.getStrCityLink();
                }
            }
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_category_univers_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
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

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewChooseUniverDetailCategoryUniversActivity);
        textViewHeadText.setText(mCitiesInfo.getStrCityName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategoryUniversListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {

        CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
        if (categoryUniversEngine.getAllCategoryById(mLongCityId).isEmpty()) {

            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            }
            Log.d("My", "CitiesListActivity -> parseData");
            parseData();

        } else {
            if (isDateComparison()) {
                Log.d("My", "CitiesListActivity -> isDateComparison  getData(citiesInfoEngine); ");
                getData(categoryUniversEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData(); is finished");

                }
            }
        }
    }

    private void getData(CategoryUniversEngine engine) {
        ArrayList<CategoryUniversInfo> categoryUniversInfos = engine.getAllCategoryById(mLongCityId);
        CategoryUniversAdapter adapter = new CategoryUniversAdapter(categoryUniversInfos);
        adapter.setOnClickCategoryUniversItem(CategoryUniversListActivity.this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickCategoryItem(CategoryUniversInfo categoryUniversInfo) {
        Intent intent = new Intent(this, UniversitiesListActivity.class);
        intent.putExtra(UniversitiesListActivity.KEY_CATEGORY_UNIVERSITY_LINK, categoryUniversInfo);
        startActivity(intent);
    }

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = categoryUniversEngine.getCategoryById(mLongCityId).getStrDateLastUpdate();

                Log.d("My", "isDateComparison dateAndTimeCities -> " + dateAndTime);

                if (parseDate.equals(dateAndTime)) {
                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
                    return true;
                } else {
                    return false;
                }

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
            document = Jsoup.connect(mCityCodeLink).get();

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

    private void parseData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

                if (Utils.connectToData(mCityCodeLink) && mLongCityId != 0) {
                    parse(mLongCityId, categoryUniversEngine, universityInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(categoryUniversEngine);
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

            private void parse(long universityCityId, CategoryUniversEngine categoryUniversEngine,
                               UniversitiesInfoEngine universityInfoEngine) {

                Document document;
                try {
                    String categoryName = "";
                    String categoryLink = "";
                    String universityName = "";
                    String universityLink = "";

                    document = Jsoup.connect(mCityCodeLink).get();

                    //get dateUpdate update page
                    String dateUpdate = parseDateAndTime();

                    //add category universities to DB
                    Elements groupElements = document.getElementsByClass("accordion-group");
                        Elements elementsNameType = groupElements.select(".accordion-toggle");
                        Elements elementsTextType = elementsNameType.select("a");
                        for (Element elementType : elementsTextType) {
                            categoryName = elementType.text();
                            categoryUniversEngine.addCategory(new CategoryUniversInfo(universityCityId,
                                    categoryName, dateUpdate));

                            Log.d("My", "add category universities to DB categoryName -> " + categoryName);
                            Log.d("My", "add category universities to DB categoryLink-> " + categoryLink);
                        }


                    //add universities to DB
                        for (Element group : groupElements) {

                            elementsNameType = group.select(".accordion-toggle");
                            elementsTextType = elementsNameType.select("a");

                            for (Element elementType : elementsTextType) {
                                categoryName = elementType.text();
                                categoryLink = elementType.attr("abs:href");
                            }

                            Elements elementsByClass = group.select(".accordion-inner");
                            Elements elementsText = elementsByClass.select("a");

                            for (Element element : elementsText) {

                                universityName = element.text();
                                universityLink = element.attr("abs:href");

                                universityInfoEngine.addUniversity(new UniversityInfo(universityCityId,
                                        categoryName, categoryLink, universityName, universityLink));

                                Log.d("My", "add element categoryName -> " + categoryName);
                                Log.d("My", "add element categoryLink -> " + categoryLink);
                                Log.d("My", "add element universityName -> " + universityName);
                                Log.d("My", "add element universityLink-> " + universityLink);
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

                if (Utils.connectToData(mCityCodeLink) && mLongCityId != 0) {
                    update(mLongCityId, categoryUniversEngine, universityInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(categoryUniversEngine);
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

            private void update(long universityCityId, CategoryUniversEngine categoryUniversEngine,
                                UniversitiesInfoEngine universityInfoEngine) {
                Document document;
                try {
                    String categoryName = "";
                    String categoryLink = "";
                    String universityName = "";
                    String universityLink = "";

                    document = Jsoup.connect(mCityCodeLink).get();

                    //get timeUpdate and dateUpdate update page
                    String dateUpdate = parseDateAndTime();

                    //add category universities to DB
                    Elements groupElements = document.getElementsByClass("accordion-group");

                        Elements elementsNameType = groupElements.select(".accordion-toggle");
                        Elements elementsTextType = elementsNameType.select("a");
                        for (Element elementType : elementsTextType) {
                            categoryName = elementType.text();
                            categoryUniversEngine.updateCategory(new CategoryUniversInfo(universityCityId,
                                    categoryName, dateUpdate));

                            Log.d("My", "add category universities to DB categoryName -> " + categoryName);
                            Log.d("My", "add category universities to DB categoryLink-> " + categoryLink);
                        }

                    //add universities to DB
                        for (Element group : groupElements) {

                            elementsNameType = group.select(".accordion-toggle");
                            elementsTextType = elementsNameType.select("a");

                            for (Element elementType : elementsTextType) {
                                categoryName = elementType.text();
                                categoryLink = elementType.attr("abs:href");

                            }

                            Elements elementsByClass = group.select(".accordion-inner");
                            Elements elementsText = elementsByClass.select("a");
                            for (Element element : elementsText) {

                                universityName = element.text();
                                universityLink = element.attr("abs:href");
                                universityInfoEngine.updateUniversity(new UniversityInfo(universityCityId,
                                        categoryName, categoryLink, universityName, universityLink));

                                Log.d("My", "update element categoryName -> " + categoryName);
                                Log.d("My", "update element categoryLink -> " + categoryLink);
                                Log.d("My", "update element universityName -> " + universityName);
                                Log.d("My", "update element universityLink-> " + universityLink);
                            }
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
