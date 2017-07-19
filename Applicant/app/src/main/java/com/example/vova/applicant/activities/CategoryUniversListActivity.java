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
import com.example.vova.applicant.toolsAndConstans.DBConstants.Favorite;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Update;
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
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CategoryUniversListActivity extends BaseActivity implements
        CategoryUniversAdapter.OnClickCategoryUniversItem {

    private CitiesInfo mCitiesInfo;
    private Calendar mCalendar;

    private long mLongCityId = -1;
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
    protected void initActivity(Bundle savedInstanceState) {
        Log.d("My", "CategoryUniversListActivity --------> initActivity");

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

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
                }else {
                    parseData(Update.NEED_AN_UPDATE);
                }
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
            } else {
                parseData(Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void getData() {
        CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
        ArrayList<CategoryUniversInfo> categoryUniversInfos = categoryUniversEngine.getAllCategoryById(mLongCityId);
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

        Calendar calendarCurrentTime = Calendar.getInstance();
        if (Utils.isNeedToEqualsTime()) {

            mCalendar.after(calendarCurrentTime);
            return true;
        } else {
            mCalendar = Utils.getModDeviceTime();
            return false;
        }
    }

    private void parseData(final boolean isNeedUpdate) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

                Log.d("My", " mCityCodeLink -> " + mCityCodeLink);
                if (Utils.connectToData(mCityCodeLink) && mLongCityId > -1) {
                    parse(mLongCityId, categoryUniversEngine, universityInfoEngine, isNeedUpdate);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                getData();
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                mProgressBar.setVisibility(View.VISIBLE);
                                getData();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

            private void parse(long cityId, CategoryUniversEngine categoryUniversEngine,
                               UniversitiesInfoEngine universityInfoEngine, boolean isNeedUpdate) {

                Document document;
                ArrayList<CategoryUniversInfo> categoryUniversInfos = new ArrayList<>();
                ArrayList<UniversityInfo> universityInfos = new ArrayList<>();
                try {
                    String categoryName = "";
                    String categoryLink = "";
                    String universityName = "";
                    String universityLink = "";

                    document = Jsoup.connect(mCityCodeLink).get();

                    //get dateUpdate update page
                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3] + "@" + arrayTimeDate[5];

                    //add category universities to DB
                    Elements groupElements = document.getElementsByClass("accordion-group");
                        Elements elementsNameType = groupElements.select(".accordion-toggle");
                        Elements elementsTextType = elementsNameType.select("a");
                        for (Element elementType : elementsTextType) {
                            categoryName = elementType.text();
                            categoryLink = elementType.attr("abs:href");
                            Log.d("My", " categoryLink in first loop -> " + categoryLink);

                            categoryUniversInfos.add(new CategoryUniversInfo(cityId, categoryName,
                                    categoryLink, dateUpdate));
                        }

                    //add universities to DB
                        for (Element group : groupElements) {

                            elementsNameType = group.select(".accordion-toggle");
                            elementsTextType = elementsNameType.select("a");

                            for (Element elementType : elementsTextType) {
                                categoryName = elementType.text();
                                categoryLink = elementType.attr("abs:href");

                                Log.d("My", " categoryLink in second loop -> " + categoryLink);
                            }

                            Elements elementsByClass = group.select(".accordion-inner");
                            Elements elementsText = elementsByClass.select("a");

                            for (Element element : elementsText) {

                                universityName = element.text();
                                universityLink = element.attr("abs:href");

                                Log.d("My", " universityLink in second loop -> " + universityLink);

                                universityInfos.add(new UniversityInfo(cityId,
                                        categoryName, categoryLink, universityName, universityLink, Favorite.NOT_A_FAVORITE));
                            }
                        }

                    if (isNeedUpdate) {
                        categoryUniversEngine.updateAllCategory(categoryUniversInfos);
                        universityInfoEngine.updateAllUniversities(universityInfos);
                    } else {
                        categoryUniversEngine.addAllCategory(categoryUniversInfos);
                        universityInfoEngine.addAllItems(universityInfos);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
