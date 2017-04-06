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

public class ExternalFormListActivity extends AppCompatActivity {

    public static final String KEY_ABOUT_EXTERNAL_FORM =
            "KEY_ABOUT_EXTERNAL_FORM";

    private String mAboutExternalForm;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mAboutExternalFormArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_form_list);

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mAboutExternalForm = bundle.getString(KEY_ABOUT_EXTERNAL_FORM);
            }
        }
        mAboutExternalForm = mAboutExternalForm.substring(1);

        mListView = (ListView) findViewById(R.id.listViewExternalForm);

        new ParseExternalFormUniversity().execute();
        mAdapter = new ArrayAdapter<String>(ExternalFormListActivity.this,  android.R.layout.simple_list_item_1,
                mAboutExternalFormArray);
    }

    public class ParseExternalFormUniversity extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);

            Document document;
            try {
                document = Jsoup.connect(html + mAboutExternalForm).get();

                Element elementExternalForm = document.getElementById("zaoch1");
                Elements texts3 = elementExternalForm.getElementsByTag("tr");

                for (Element link : texts3) {
                    mAboutExternalFormArray.add(link.text());
                }

                Log.d("My", "doInBackground   mAboutExternalFormArray ->" + mAboutExternalFormArray);

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
