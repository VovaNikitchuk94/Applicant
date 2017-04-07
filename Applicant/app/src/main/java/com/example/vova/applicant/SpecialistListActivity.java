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

/**
 * Created by vovan on 06.04.2017.
 */

public class SpecialistListActivity extends AppCompatActivity{

    public static final String KEY_SPECIALIST_TITLE = "KEY_SPECIALIST_TITLE";
    public static final String KEY_SPECIALIST_LINK = "KEY_SPECIALIST_LINK";

    private String mStrSpecialistLink = "";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mSpecialistNames = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("My", "BachelorListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

//                mStrBachelorTitle = bundle.getString(KEY_BACHELOR_TITLE);
                mStrSpecialistLink = bundle.getString(KEY_SPECIALIST_LINK);
            }
        }

        mStrSpecialistLink = mStrSpecialistLink.substring(1);

        mListView = (ListView)findViewById(R.id.listViewUniversal);

        new ParseSpecialistList().execute();
        mAdapter = new ArrayAdapter<>(SpecialistListActivity.this,
                android.R.layout.simple_list_item_1,
                mSpecialistNames);
        Log.d("My", "onCreate   link ->" + mStrSpecialistLink);
    }

    public class ParseSpecialistList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);
//            String test = "http://www.vstup.info/2016/i2016i252.html#vnz#d_o_1";
            Document document;
            try {
                document = Jsoup.connect(html + mStrSpecialistLink).get();
//                document = Jsoup.connect(test).get();
                Log.d("My", "BachelorListActivity -> ParseSpecialistList - > documentLink"  + document.text());

                Elements links = document.select("td");

                for (Element link : links) {
                    if (link.text().contains("Спеціаліст")){
                        mSpecialistNames.add(link.text());
//                        mAboutFullTimeFormLink.add(link.attr("href"));
                    }

                }

                Log.d("My", "doInBackground   mBachelorNames ->" + mSpecialistNames);
//                Log.d("My", "doInBackground   mUniversityLinks ->" + mUniversityLinks);

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
