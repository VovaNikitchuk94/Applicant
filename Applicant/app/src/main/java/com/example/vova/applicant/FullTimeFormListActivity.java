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

public class FullTimeFormListActivity extends AppCompatActivity {

    public static final String KEY_ABOUT_FULL_TIME_FORM = "KEY_ABOUT_EXTERNAL_FORM";

    private String mAboutFullTimeForm;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mAboutFullTimeFormArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_time_form_list);

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mAboutFullTimeForm = bundle.getString(KEY_ABOUT_FULL_TIME_FORM);
            }
        }
        mAboutFullTimeForm = mAboutFullTimeForm.substring(1);

        mListView = (ListView) findViewById(R.id.listViewFullTimeFormListActivity);

        new ParseFullTimeFormUniversity().execute();
        mAdapter = new ArrayAdapter<String>(FullTimeFormListActivity.this,  android.R.layout.simple_list_item_1,
                mAboutFullTimeFormArray);
    }

    public class ParseFullTimeFormUniversity extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);

            Document document;
            try {
                document = Jsoup.connect(html + mAboutFullTimeForm).get();

                Elements texts2 = document.select("td");

                for (Element link : texts2) {
                    if (link.text().contains("Бакалавр")){
                        mAboutFullTimeFormArray.add(link.text());
                    }

                }

                Log.d("My", "doInBackground   mAboutExternalFormArray ->" + mAboutFullTimeFormArray);

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
