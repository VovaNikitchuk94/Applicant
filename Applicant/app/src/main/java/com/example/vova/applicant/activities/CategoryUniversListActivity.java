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

public class CategoryUniversListActivity extends BaseActivity implements
        CategoryUniversAdapter.OnClickCategoryUniversItem {

    private CitiesInfo mCitiesInfo;

    private long mLongCityId = 0L;

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
                parseData();
                Log.d("My","SwipeRefreshLayout -> parseData -> is start");
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
        mLongCityId = mCitiesInfo.getId();
        CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
        if (categoryUniversEngine.getAllCategoryById(mLongCityId).isEmpty()) {
            parseData();
        } else {
            getData(categoryUniversEngine);
        }
    }

    private void getData(CategoryUniversEngine engine) {
        ArrayList<CategoryUniversInfo> categoryUniversInfos = engine.getAllCategoryById(mLongCityId);
        Log.d("My", "universityInfos ------>" + categoryUniversInfos.size());
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

    private void parseData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
                UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

                if (Utils.connectToData(mCitiesInfo.getStrCityLink()) && mLongCityId != 0) {
                    parse(mLongCityId, categoryUniversEngine, universityInfoEngine);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        getData(categoryUniversEngine);
                    }
                });
            }

            private void parse(long universityCityId, CategoryUniversEngine categoryUniversEngine,
                               UniversitiesInfoEngine universityInfoEngine) {
                String html;
                html = mCitiesInfo.getStrCityLink();
                Document document;
                try {
                    String categoryName = "";
                    String categoryLink = "";
                    String universityName = "";
                    String universityLink = "";

                    document = Jsoup.connect(html).get();

                    //get timeUpdate and dateUpdate update page
                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage );
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3];
                    String timeUpdate = arrayTimeDate[5];
                    Log.d("My", "dateUpdate -> " + dateUpdate + "\ntimeUpdate -> " + timeUpdate);

                    Elements groupElements = document.getElementsByClass("accordion-group");

                    //add category universities to DB
                    if (categoryUniversEngine.getAllCategoryById(universityCityId).isEmpty()){

                        Elements elementsNameType = groupElements.select(".accordion-toggle");
                        Elements elementsTextType = elementsNameType.select("a");
                        for (Element elementType : elementsTextType) {
                            categoryName = elementType.text();
//                            categoryLink = elementType.attr("abs:href");
                            categoryUniversEngine.addCategory(new CategoryUniversInfo(universityCityId,
                                    categoryName, dateUpdate, timeUpdate));

                            Log.d("My", "add category universities to DB categoryName -> " + categoryName);
                            Log.d("My", "add category universities to DB categoryLink-> " + categoryLink);
                        }
                    }

                    //add universities to DB
                    if (universityInfoEngine.getAllUniversitiesById(mLongCityId).isEmpty()) {

                        for (Element group : groupElements) {

                            Elements elementsNameType = group.select(".accordion-toggle");
                            Elements elementsTextType = elementsNameType.select("a");

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

                                Log.d("My", "add universities to DB categoryName -> " + categoryName);
                                Log.d("My", "add universities to DB categoryLink-> " + categoryLink);
                                Log.d("My", "element name -> " + element.text());
                                Log.d("My", "element link-> " + element.attr("abs:href"));
                            }
                        }
                    } else {
                        for (Element group : groupElements) {

                            Elements elementsNameType = group.select(".accordion-toggle");
                            Elements elementsTextType = elementsNameType.select("a");

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

                                Log.d("My", "element categoryName -> " + categoryName);
                                Log.d("My", "element categoryLink-> " + categoryLink);
                                Log.d("My", "element name -> " + element.text());
                                Log.d("My", "element link-> " + element.attr("abs:href"));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
