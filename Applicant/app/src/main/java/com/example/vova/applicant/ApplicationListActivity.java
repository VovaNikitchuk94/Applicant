package com.example.vova.applicant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vova.applicant.adapters.ApplicationInfoAdapter;
import com.example.vova.applicant.model.ApplicationsInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ApplicationListActivity extends AppCompatActivity {

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private ListView mListView;
//    private ArrayAdapter<String> mAdapter;
//    private ArrayList<String> mApplicantArray = new ArrayList<>();

    private ArrayList<ApplicationsInfo> mApplicationsInfos = new ArrayList<>();
    private ApplicationInfoAdapter mApplicationInfoAdapter;

    private String mStrApplicantCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        mListView = (ListView) findViewById(R.id.listViewUniversal);

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                mStrApplicantCode = bundle.getString(INTENT_KEY_APPLICANT_ACTIVITY);
            }
        }

        new ParseApplicantsList().execute();

        mApplicationInfoAdapter = new ApplicationInfoAdapter(ApplicationListActivity.this,
                R.layout.list_item_application_info, mApplicationsInfos);
//        mAdapter = new ArrayAdapter<>(ApplicationListActivity.this, android.R.layout.simple_list_item_1,
//                mApplicantArray);
//
//        Log.d("My", "onCreate   mApplicantArray ->" + mApplicantArray);
//        Log.d("My", "ApplicationListActivity onCreate   mApplicantArray ->" + mApplicantArray);
//        Log.d("My", "ApplicationListActivity onCreate   mApplicantArray.size ->" + mApplicantArray.size());
//        Log.d("My", "ApplicationListActivity onCreate   mListView.getCount() ->" + mListView.getCount());

    }

    public class ParseApplicantsList extends AsyncTask<String, String, String> {

        ProgressDialog progDailog = new ProgressDialog(ApplicationListActivity.this);

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

            String number;
            String name;
            String score;
            String scoreBDO;
            String scoreZNO;

            Document document;
            try {
                document = Jsoup.connect(mStrApplicantCode).get();

                Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                Elements elements = links.select("tbody");
                Elements selectTr = elements.select("tr");

//                mApplicantArray.clear();
//                mAdapter.notifyDataSetChanged();

                for (Element link : selectTr) {

//                    number = link.select("td").first().text();
//                    name = link.select("td").last().text();

                    Elements tds = link.select("td");
                    number = tds.get(0).text();
                    name = tds.get(1).text();
                    score = tds.get(2).text();
                    scoreBDO = tds.get(3).text();
                    scoreZNO = tds.get(4).text();

//
//                    mApplicationsInfos.add(new ApplicationsInfo(number, name, score));
                    mApplicationsInfos.add(new ApplicationsInfo(number, name, score, scoreBDO, scoreZNO));
//                    mApplicantArray.add(link.text());
                }

//                Log.d("My", "CitiesListActivity doInBackground   mCitiesLinks ->" + mCitiesLinks);
//                Log.d("My", "ApplicationListActivity doInBackground   mApplicantArray ->" + mApplicantArray);
//                Log.d("My", "ApplicationListActivity doInBackground   mApplicantArray.size ->" + mApplicantArray.size());
//                Log.d("My", "ApplicationListActivity doInBackground   mAdapter.getCount ->" + mAdapter.getCount());
//                Log.d("My", "ApplicationListActivity doInBackground  mListView ->" + mListView);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
//            mListView.setAdapter(mAdapter);
            mListView.setAdapter(mApplicationInfoAdapter);
            progDailog.dismiss();
        }
    }
}
