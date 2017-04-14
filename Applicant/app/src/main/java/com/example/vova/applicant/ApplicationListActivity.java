package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ApplicationListActivity extends AppCompatActivity {

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mApplicantArray = new ArrayList<>();

    private String mStrApplicantCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        mListView = (ListView) findViewById(R.id.listViewUniversal);

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

                mStrApplicantCode = bundle.getString(INTENT_KEY_APPLICANT_ACTIVITY);
            }
        }

        new ParseApplicantsList().execute();

        mAdapter = new ArrayAdapter<>(ApplicationListActivity.this, android.R.layout.simple_list_item_1,
                mApplicantArray);

        Log.d("My", "onCreate   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onCreate   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onCreate   mApplicantArray.size ->" + mApplicantArray.size());
        Log.d("My", "ApplicationListActivity onCreate   mListView.getCount() ->" + mListView.getCount());

    }

    public class ParseApplicantsList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String html = mStrApplicantCode;

            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                Elements elements = links.select("tbody");
                Elements tr = elements.select("tr");

                mApplicantArray.clear();
//                mAdapter.notifyDataSetChanged();

                for (Element link : tr) {
                    mApplicantArray.add(link.text());
                }

//                Log.d("My", "CitiesListActivity doInBackground   mCitiesLinks ->" + mCitiesLinks);
                Log.d("My", "ApplicationListActivity doInBackground   mApplicantArray ->" + mApplicantArray);
                Log.d("My", "ApplicationListActivity doInBackground   mApplicantArray.size ->" + mApplicantArray.size());
                Log.d("My", "ApplicationListActivity doInBackground   mAdapter.getCount ->" + mAdapter.getCount());
                Log.d("My", "ApplicationListActivity doInBackground  mListView ->" + mListView);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("My", "ApplicationListActivity onStart   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onStart   mApplicantArray.size ->" + mApplicantArray.size());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("My", "ApplicationListActivity onStop   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onStop   mApplicantArray.size ->" + mApplicantArray.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("My", "ApplicationListActivity onDestroy   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onDestroy   mApplicantArray.size ->" + mApplicantArray.size());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("My", "ApplicationListActivity onPause   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onPause   mApplicantArray.size ->" + mApplicantArray.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("My", "ApplicationListActivity onResume   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onResume   mApplicantArray.size ->" + mApplicantArray.size());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("My", "ApplicationListActivity onRestart   mApplicantArray ->" + mApplicantArray);
        Log.d("My", "ApplicationListActivity onRestart   mApplicantArray.size ->" + mApplicantArray.size());
    }

}
