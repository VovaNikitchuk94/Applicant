package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
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

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_univers_list);

        Log.d("My", "CategoryUniversListActivity onCreate");

        drawerAndToolbar();

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCitiesInfo = (CitiesInfo) bundle.get(INTENT_KEY_UNIVERSITY_ACTIVITY);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_category_univers_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParseCategoryList().execute();
                    }
                }, 0);
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

    @Override
    public void drawerAndToolbar() {
        super.drawerAndToolbar();
    }

    private void setData() {
        mLongCityId = mCitiesInfo.getId();
        CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
        if (categoryUniversEngine.getAllCategoryById(mLongCityId).isEmpty()) {
            new ParseCategoryList().execute();
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
    }

    //TODO fix
//    @Override
//    public void onClickCategoryUniversItem(CategoryUniversInfo categoryUniversInfo) {
//        Intent intent = new Intent(this, UniversitiesListActivity.class);
////        Intent intent = new Intent(this, DetailUniversListActivity.class);
////        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
//        intent.putExtra(UniversitiesListActivity.KEY_DETAIL_UNIVERSITY_LINK, categoryUniversInfo);
//        startActivity(intent);
//    }

    @Override
    public void onClickCategoryItem(CategoryUniversInfo categoryUniversInfo) {
        Intent intent = new Intent(this, UniversitiesListActivity.class);
//        Intent intent = new Intent(this, DetailUniversListActivity.class);
//        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        intent.putExtra(UniversitiesListActivity.KEY_DETAIL_UNIVERSITY_LINK, categoryUniversInfo);
        startActivity(intent);
    }

    private class ParseCategoryList extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = new ProgressDialog(CategoryUniversListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage(getString(R.string.textResourceLoading));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
            UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

            if (Utils.connectToData(mCitiesInfo.getStrCityLink()) && mLongCityId != 0) {
                parse(mLongCityId, categoryUniversEngine, universityInfoEngine);
//                parse(mLongCityId, categoryUniversEngine);
            }
            return null;
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

                Elements groupElements = document.getElementsByClass("accordion-group");

                if (categoryUniversEngine.getAllCategoryById(universityCityId).isEmpty()){
//                    for (Element group : groupElements) {

                        Elements elementsNameType = groupElements.select(".accordion-toggle");
                        Elements elementsTextType = elementsNameType.select("a");
                        for (Element elementType : elementsTextType) {
                             categoryName = elementType.text();
                             categoryLink = elementType.attr("abs:href");
                            categoryUniversEngine.addCategory(new CategoryUniversInfo(universityCityId, categoryName, categoryLink));

                            Log.d("My", "element categoryName -> " + categoryName);
                            Log.d("My", "element categoryLink-> " + categoryLink);
                        }
//                    }
                }

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
//                            categoryUniversEngine.addCategory(new CategoryUniversInfo(universityCityId, categoryName, categoryLink));
                            universityInfoEngine.addUniversity(new UniversityInfo(universityCityId, categoryName, categoryLink, universityName, universityLink) );

                            Log.d("My", "element categoryName -> " + categoryName);
                            Log.d("My", "element categoryLink-> " + categoryLink);
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
//                            categoryUniversEngine.updateCategory(new CategoryUniversInfo(universityCityId, categoryName, categoryLink));
                            universityInfoEngine.updateUniversity(new UniversityInfo(universityCityId, categoryName, categoryLink, universityName, universityLink));

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

        @Override
        protected void onPostExecute(String result) {
            CategoryUniversEngine categoryUniversEngine = new CategoryUniversEngine(getApplication());
            getData(categoryUniversEngine);
            progDailog.dismiss();
        }
    }
}
