package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vova.applicant.adapters.UniversityInfoAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutUniversityActivity extends AppCompatActivity {

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY =
            "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private String mAboutUniversityLink;

    private ListView mListView;

    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();
    private UniversityInfoAdapter mUniversityInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_university);

        Log.d("OnCreate", "AboutUniversityActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mAboutUniversityLink = bundle.getString(KEY_ABOUT_UNIVERSITY_ACTIVITY);
            }
        }

        mListView = (ListView) findViewById(R.id.listViewAboutUniversityActivity);

        new ParseAboutUniversityList().execute();
        // TODO modified listView style

        mUniversityInfoAdapter = new UniversityInfoAdapter(AboutUniversityActivity.this,
                R.layout.list_item_university_info, mUniversityInfos);
        Log.d("My", "onCreate   link -> mAboutUniversityLink" + mAboutUniversityLink);
        Log.d("My", "onCreate   link -> mUniversityInfos" + mUniversityInfos);

        mListView.setAdapter(mUniversityInfoAdapter);
    }

    public class ParseAboutUniversityList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Document document;
            try {
                document = Jsoup.connect(mAboutUniversityLink).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements texts1 = elementAboutUniversities.getElementsByTag("tr");

                for (Element link : texts1){
                    mUniversityInfos.add(new UniversityInfo(
                            link.select("td").first().text(),
                            link.select("td").last().text()));
                }

                Log.d("My", "doInBackground   mUniversityInfos ->" + mUniversityInfos);
                Log.d("My", "doInBackground   mUniversityInfos ->" + mUniversityInfos.size());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mUniversityInfoAdapter);

        }
    }
}
