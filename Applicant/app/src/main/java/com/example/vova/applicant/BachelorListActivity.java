package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BachelorListActivity extends AppCompatActivity{

    public static final String KEY_BACHELOR_TITLE = "KEY_BACHELOR_TITLE";
    public static final String KEY_BACHELOR_LINK = "KEY_BACHELOR_LINK";

//    private String mStrBachelorTitle;
    private String mStrBachelorLink = "";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mBachelorNames = new ArrayList<>();

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
                mStrBachelorLink = bundle.getString(KEY_BACHELOR_LINK);
            }
        }

            mStrBachelorLink = mStrBachelorLink.substring(1);

        mListView = (ListView)findViewById(R.id.listViewUniversal);

        new ParseBachelorList().execute();
        mAdapter = new ArrayAdapter<>(BachelorListActivity.this,
                android.R.layout.simple_list_item_1,
                mBachelorNames);
        Log.d("My", "onCreate   link ->" + mStrBachelorLink);

    }

    public class ParseBachelorList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);
//            String test = "http://www.vstup.info/2016/i2016i252.html#vnz#d_o_1";
            Document document;
            try {
                document = Jsoup.connect(html + mStrBachelorLink).get();
//                document = Jsoup.connect(test).get();
                Log.d("My", "BachelorListActivity -> ParseBachelorList - > documentLink"  + document.text());

                Elements links = document.select("td");

                for (Element link : links) {
                    if (link.text().contains("Бакалавр")){
                        mBachelorNames.add(link.text());
//                        mAboutFullTimeFormLink.add(link.attr("href"));
                    }

                }

                Log.d("My", "doInBackground   mBachelorNames ->" + mBachelorNames);
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



    //TODO create  & manifest

}
