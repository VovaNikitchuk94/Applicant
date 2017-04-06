package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutUniversityActivity extends AppCompatActivity {

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY =
            "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private String mAboutUniversityText;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mAboutUniversityArray = new ArrayList<>();

    private TextView mTextViewHeadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_university);

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                mAboutUniversityText = bundle.getString(KEY_ABOUT_UNIVERSITY_ACTIVITY);
            }
        }
        mAboutUniversityText = mAboutUniversityText.substring(1);

        mListView = (ListView)findViewById(R.id.listViewAboutUniversityActivity);
        mTextViewHeadText = (TextView)findViewById(R.id.textViewHeadAboutUniversityActivity);

        new ParseAboutUniversityList().execute();
        mAdapter = new ArrayAdapter<>(AboutUniversityActivity.this, android.R.layout.simple_list_item_1,
                mAboutUniversityArray);
        Log.d("My", "onCreate   link ->" + mAboutUniversityText);
    }

    public class ParseAboutUniversityList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);

            Document document;
            try {
                document = Jsoup.connect(html + mAboutUniversityText).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements texts1 = elementAboutUniversities.getElementsByTag("tr");

                for (Element link : texts1) {
                    mAboutUniversityArray.add(link.text());
                }

                Log.d("My", "doInBackground   mAboutExternalFormArray ->" + mAboutUniversityArray);

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
