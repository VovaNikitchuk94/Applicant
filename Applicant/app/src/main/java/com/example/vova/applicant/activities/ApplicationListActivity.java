package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.ApplicationAdapter;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.engines.ApplicationInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ApplicationListActivity extends AppCompatActivity implements ApplicationAdapter.OnClickApplicationItem{

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SpecialtiesInfo mSpecialtiesInfo;

    private long mLongSpecialityId = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applcation_list);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSpecialtiesInfo = (SpecialtiesInfo) bundle.get(INTENT_KEY_APPLICANT_ACTIVITY);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_applicant_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParseApplicantsList().execute();
                    }
                }, 0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewApplicationListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mLongSpecialityId = mSpecialtiesInfo.getId();
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
            new ParseApplicantsList().execute();
        } else {
            getData(applicationInfoEngine);
        }
    }

    private void getData(ApplicationInfoEngine applicationInfoEngine) {
        ArrayList<ApplicationsInfo> applicationsInfos = applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId);
        ApplicationAdapter adapter = new ApplicationAdapter(applicationsInfos);
        adapter.setOnClickApplicationItem(ApplicationListActivity.this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickApplicationItem(ApplicationsInfo applicationInfo) {

    }

    private class ParseApplicantsList extends AsyncTask<String, String, String> {
        ProgressDialog progDailog = new ProgressDialog(ApplicationListActivity.this);
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());

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
        protected String doInBackground(String... params) {
            if (Utils.connectToData(mSpecialtiesInfo.getStrLink()) && mLongSpecialityId != 0) {
                parse(applicationInfoEngine);
            }
            return null;
        }

        private void parse(ApplicationInfoEngine applicationInfoEngine) {
            String html;
            String number;
            String name;
            String score;
            String someLink;

            Document document;
            try {
                html = mSpecialtiesInfo.getStrLink();
                document = Jsoup.connect(html).get();

                Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                Elements elements = links.select("tbody");
                Elements selectTr = elements.select("tr");

                if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
                    for (Element link : selectTr) {
                        Elements tds = link.select("td");
                        number = tds.get(0).text();
                        name = tds.get(1).text();
                        score = tds.get(3).text();
                        someLink = tds.attr("abs:href");

                        applicationInfoEngine.addApplication(new ApplicationsInfo(mLongSpecialityId,
                                number, name, score, someLink));
                    }
                } else {
                    for (Element link : selectTr) {
                        Elements tds = link.select("td");
                        number = tds.get(0).text();
                        name = tds.get(1).text();
                        score = tds.get(3).text();
                        someLink = tds.attr("abs:href");

                        applicationInfoEngine.updateApplicant(new ApplicationsInfo(mLongSpecialityId,
                                number, name, score, someLink));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String srt) {
            getData(applicationInfoEngine);
            progDailog.dismiss();
        }
    }
}
