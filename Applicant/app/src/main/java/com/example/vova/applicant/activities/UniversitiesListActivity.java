package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.UniversitiesAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesListActivity extends AppCompatActivity implements
        UniversitiesAdapter.OnClickUniversityItem {

    private CitiesInfo mCitiesInfo;

    private long mLongCityId = 0L;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities_list);

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCitiesInfo = (CitiesInfo) bundle.get(INTENT_KEY_UNIVERSITY_ACTIVITY);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParseUniversityList().execute();
                    }
                }, 0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        textViewHeadText.setText(mCitiesInfo.getStrCityName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUniversitiesListActivity);
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
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        if (universityInfoEngine.getAllUniversitiesById(mLongCityId).isEmpty()){
            new ParseUniversityList().execute();
        } else {
            getData(universityInfoEngine);
        }
    }

    private void getData(UniversitiesInfoEngine universityInfoEngine) {
        ArrayList<UniversityInfo> universityInfos = universityInfoEngine.getAllUniversitiesById(mLongCityId);
        UniversitiesAdapter universitiesAdapter = new UniversitiesAdapter(universityInfos);
        universitiesAdapter.setOnClickUniversityItem(UniversitiesListActivity.this);
        mRecyclerView.setAdapter(universitiesAdapter);
    }

    @Override
    public void onClickUniversityItem(UniversityInfo universityInfo) {
        Intent intent = new Intent(this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        startActivity(intent);
    }

    private class ParseUniversityList extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = new ProgressDialog(UniversitiesListActivity.this);

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
            UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());

            if (Utils.connectToData(mCitiesInfo.getStrCityLink()) && mLongCityId != 0) {
                parse(mLongCityId, universityInfoEngine);
            }
            return null;
        }

        private void parse(long universityCityId, UniversitiesInfoEngine universityInfoEngine) {
            String html;
            html = mCitiesInfo.getStrCityLink();
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements elementsByClass = document.getElementsByClass("accordion-inner");
                Elements elementsText = elementsByClass.select("a");
                if (universityInfoEngine.getAllUniversitiesById(mLongCityId).isEmpty()) {
                    for (Element element : elementsText) {
                        String universityName = element.text();
                        String universityLink = element.attr("abs:href");
                        universityInfoEngine.addUniversity(new UniversityInfo(universityCityId, universityName, universityLink));
                    }
                } else {
                    for (Element element : elementsText) {
                        String universityName = element.text();
                        String universityLink = element.attr("abs:href");
                        universityInfoEngine.updateUniversity(new UniversityInfo(universityCityId, universityName, universityLink));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
            getData(universityInfoEngine);
            progDailog.dismiss();
        }
    }
}
