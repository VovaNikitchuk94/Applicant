package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vova.applicant.R;
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
    private ArrayList<ApplicationsInfo> mApplicationsInfos = new ArrayList<>();
    private ApplicationAdapter mAdapter;

    private SpecialtiesInfo mSpecialtiesInfo;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewApplicationListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseApplicantsList().execute();
    }

    @Override
    public void onClickApplicationItem(ApplicationsInfo applicationInfo) {

    }

    private class ParseApplicantsList extends AsyncTask<String, String, String> {

        ProgressDialog progDailog = new ProgressDialog(ApplicationListActivity.this);
        long mLongSpecialityId = mSpecialtiesInfo.getId();

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
            ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
            if (applicationInfoEngine.getAllApplicantions().isEmpty()) {
                parse(applicationInfoEngine);
            } else {
                if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
                    parse(applicationInfoEngine);
                }
            }

            return null;
        }

        private void parse(ApplicationInfoEngine applicationInfoEngine) {
            String html;//TODO update for many years
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

                mApplicationsInfos.clear();

                for (Element link : selectTr) {

                    Elements tds = link.select("td");
                    number = tds.get(0).text();
                    name = tds.get(1).text();
                    score = tds.get(3).text();
                    someLink = tds.attr("abs:href");

                    applicationInfoEngine.addApplication(new ApplicationsInfo(mLongSpecialityId, number, name, score, someLink));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String srt) {
            final ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
            mApplicationsInfos = applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId);
            mAdapter = new ApplicationAdapter(mApplicationsInfos);
            mAdapter.setOnClickApplicationItem(ApplicationListActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            progDailog.dismiss();
        }
    }
}
