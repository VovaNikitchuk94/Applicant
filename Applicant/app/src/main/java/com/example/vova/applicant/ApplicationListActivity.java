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
    }

    public class ParseApplicantsList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = mStrApplicantCode;

            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                Elements elements = links.select("tbody");
                Elements tr = elements.select("tr");

                for (Element link : tr) {
//                    mCitiesLinks.add(link.attr("abs:href")); TODO использовать абсолютные ссылки!!!
//                    mCitiesLinks.add(link.attr("href"));
                    mApplicantArray.add(link.text());
                }

//                Log.d("My", "CitiesListActivity doInBackground   mCitiesLinks ->" + mCitiesLinks);
                Log.d("My", "CitiesListActivity doInBackground   mApplicantArray ->" + mApplicantArray);
                Log.d("My", "CitiesListActivity doInBackground   mApplicantArray.size ->" + mApplicantArray.size());

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
}
