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

public class MasterListActivity extends AppCompatActivity{

    public static final String KEY_MASTER_TITLE = "KEY_MASTER_TITLE";
    public static final String KEY_MASTER_LINK = "KEY_MASTER_LINK";

    private String mStrMasterLink = "";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mMasterNames = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("My", "MasterListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

//                mStrBachelorTitle = bundle.getString(KEY_BACHELOR_TITLE);
                mStrMasterLink = bundle.getString(KEY_MASTER_LINK);
            }
        }
        mStrMasterLink = mStrMasterLink.substring(1);

        mListView = (ListView)findViewById(R.id.listViewUniversal);

        new ParseMasterList().execute();
        mAdapter = new ArrayAdapter<>(MasterListActivity.this,
                android.R.layout.simple_list_item_1,
                mMasterNames);
        Log.d("My", "onCreate   link ->" + mStrMasterLink);
    }

    public class ParseMasterList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = TopLevelActivity.yearsCodeLink;

            Log.d("My", "onCreate   html ->" + html);
//            String test = "http://www.vstup.info/2016/i2016i252.html#vnz#d_o_1";
            Document document;
            try {
                document = Jsoup.connect(html + mStrMasterLink).get();
//                document = Jsoup.connect(test).get();
                Log.d("My", "MasterListActivity -> ParseMasterList - > documentLink"  + document.text());

                Elements links = document.select("td");

                for (Element link : links) {
                    if (link.text().contains("Магістр")){
                        mMasterNames.add(link.text());
//                        mAboutFullTimeFormLink.add(link.attr("href"));
                    }

                }

                Log.d("My", "doInBackground   mBachelorNames ->" + mMasterNames);
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
