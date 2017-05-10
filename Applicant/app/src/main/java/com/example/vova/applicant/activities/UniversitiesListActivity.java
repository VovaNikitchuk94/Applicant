package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversitiesAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversityInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesListActivity extends AppCompatActivity implements
        UniversitiesAdapter.OnClickUniversityItem {

    private CitiesInfo mCitiesInfo;

    private RecyclerView mRecyclerView;
    private UniversitiesAdapter mUniversitiesAdapter;
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
                mCitiesInfo = (CitiesInfo) bundle.get(INTENT_KEY_UNIVERSITY_ACTIVITY);
            }
        }

        Log.d("My", "onCreate   link ->" + mCitiesInfo);

        mTextViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        mTextViewHeadText.setText(mCitiesInfo.getStrCityName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUniversitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseUniversityList().execute();

    }

    @Override
    public void onClickUniversityItem(UniversityInfo universityInfo) {
        Intent intent = new Intent(this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        startActivity(intent);
    }

    private class ParseUniversityList extends AsyncTask<String, Integer, String> {

        ProgressDialog progDailog = new ProgressDialog(UniversitiesListActivity.this);
        long mLongUniversityCityId = mCitiesInfo.getId();


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

            UniversityInfoEngine universityInfoEngine = new UniversityInfoEngine(getApplication());

                if (universityInfoEngine.getAllUniversitiesById(mLongUniversityCityId).isEmpty()){
                    parse(mLongUniversityCityId, universityInfoEngine);
                }
            return null;
        }

        private void parse(long universityCityId, UniversityInfoEngine universityInfoEngine) {
            String html;
            html = mCitiesInfo.getStrCityLink();
            Document document;
            try {
                document = Jsoup.connect(html).get();
                Log.d("My", "UniversitiesListActivity -> ParseUniversityList - > mCitiesInfo.getStrCityLink()" + mCitiesInfo.getStrCityLink());

                Elements elementsByClass = document.getElementsByClass("accordion-inner");
                Elements elementsText = elementsByClass.select("a");
                for (Element element : elementsText) {


                    String universityName = element.text();
                    String universityLink = element.attr("abs:href");

                    universityInfoEngine.addUniversity(new UniversityInfo(universityCityId, universityName, universityLink));
                    Log.d("My", "ParseUniversities doInBackground   link ->" + element.attr("abs:href"));
                    Log.d("My", "ParseUniversities doInBackground    name ->" + element.text());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            final UniversityInfoEngine universityInfoEngine = new UniversityInfoEngine(getApplication());
            mUniversityInfos = universityInfoEngine.getAllUniversitiesById(mLongUniversityCityId);
            mUniversitiesAdapter = new UniversitiesAdapter(mUniversityInfos);
            mUniversitiesAdapter.setOnClickUniversityItem(UniversitiesListActivity.this);
            mRecyclerView.setAdapter(mUniversitiesAdapter);
            progDailog.dismiss();
        }
    }
}
