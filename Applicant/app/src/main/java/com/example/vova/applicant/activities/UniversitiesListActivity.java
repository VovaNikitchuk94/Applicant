package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;
import com.example.vova.applicant.model.engines.UniversityInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesListActivity extends AppCompatActivity implements
        UniversitiesInfoAdapter.OnClickUniversityItem {

    private long universityLink;

    private RecyclerView mRecyclerView;
    private UniversitiesInfoAdapter mUniversitiesInfoAdapter;
    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();

    private TextView mTextViewHeadText;

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities_list);

        Log.d("OnCreate", "UniversitiesListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                universityLink = bundle.getLong(INTENT_KEY_UNIVERSITY_ACTIVITY, -1);
            }
        }

        Log.d("My", "onCreate   link ->" + universityLink);


//        mTextViewHeadText = (TextView) findViewById(R.id.textViewHeadUniversalListView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUniversitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        new ParseUniversityList().execute();

    }

    @Override
    public void onClickUniversityItem(long nIdUniversity) {
        Intent intent = new Intent(this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, nIdUniversity);
        startActivity(intent);
    }

    public class ParseUniversityList extends AsyncTask<String, Integer, String> {

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
            String html;
            CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());

            UniversityInfoEngine universityInfoEngine = new UniversityInfoEngine(getApplication());

            if (universityInfoEngine.getAllUniversities().isEmpty()) {

                CitiesInfo citiesInfo = citiesInfoEngine.getCityById(universityLink);
                if (universityLink > -1 && citiesInfo != null) {
                    html = citiesInfo.getStrCityLink();
                    Document document;
                    try {
                        document = Jsoup.connect(html).get();
                        Log.d("My", "UniversitiesListActivity -> ParseUniversityList - > documentLink" + document.text());

                        Elements elementsByClass = document.getElementsByClass("accordion-inner");
                        Elements elementsText = elementsByClass.select("a");
                        for (Element element : elementsText) {

                            String universityName = element.text();
                            String universityLink = element.attr("abs:href");

                            universityInfoEngine.addUniversity(new UniversityInfo(universityName, universityLink));
                            Log.d("My", "ParseUniversities doInBackground   link ->" + element.attr("abs:href"));
                            Log.d("My", "ParseUniversities doInBackground    name ->" + element.text());

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            final UniversityInfoEngine universityInfoEngine = new UniversityInfoEngine(getApplication());
            mUniversityInfos = universityInfoEngine.getAllUniversities();
            mUniversitiesInfoAdapter = new UniversitiesInfoAdapter(mUniversityInfos);
            mUniversitiesInfoAdapter.setOnClickUniversityItem(UniversitiesListActivity.this);
            mRecyclerView.setAdapter(mUniversitiesInfoAdapter);
            progDailog.dismiss();
        }
    }
}
