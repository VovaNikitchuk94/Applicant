package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesActivity extends AppCompatActivity {

    String s;

    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mUniversityArray = new ArrayList<>();
    ArrayList<String> mUniversityLinks = new ArrayList<>();

    TextView mTextViewHeadText;

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities);

       s = (String) getIntent().getExtras().get("INTENT_KEY_UNIVERSITY_ACTIVITY");


        mListView = (ListView)findViewById(R.id.listViewUniversityActivity);
        mTextViewHeadText = (TextView)findViewById(R.id.textViewHeadAboutUniversityActivity);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(UniversitiesActivity.this, AboutUniversityActivity.class);
                intent.putExtra(AboutUniversityActivity.INTENT_KEY_ABOUT_UNIVERSITY_ACTIVITY,
                        mUniversityLinks.get(position));
                startActivity(intent);
                Log.d("My", "position = " + position + "id = " + id);

            }
        });

        new ParseYears().execute();
        mAdapter = new ArrayAdapter<>(UniversitiesActivity.this, android.R.layout.simple_list_item_1,
                mUniversityArray);
        Log.d("My", "onCreate   s ->" + s);
    }

    public class ParseYears extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = "http://vstup.info";

            Log.d("My", "onCreate   html ->" + html);

            Document document;
            try {
                document = Jsoup.connect(html + s).get();

                Element elementUnivers = document.getElementById("okrArea");
                Elements links = elementUnivers.getElementsByTag("a");

                for (Element link : links) {
                    mUniversityLinks.add(link.attr("href"));
                    mUniversityArray.add(link.text());
                }
//                Elements cities = document.select("tr");

//                mUniversityArray.clear();
//
//                for (Element element : cities){
//                    mUniversityArray.add(element.text());
//                }

                Log.d("My", "doInBackground   mUniversityArray ->" + mUniversityArray);
                Log.d("My", "doInBackground   mUniversityLinks ->" + mUniversityLinks);

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
