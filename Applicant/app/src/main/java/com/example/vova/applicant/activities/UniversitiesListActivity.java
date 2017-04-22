package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
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

import com.example.vova.applicant.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesListActivity extends AppCompatActivity {

    private String link;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mUniversitiesName = new ArrayList<>();
    private ArrayList<String> mUniversityLinks = new ArrayList<>();

    private TextView mTextViewHeadText;

    public static final String INTENT_KEY_UNIVERSITY_ACTIVITY = "INTENT_KEY_UNIVERSITY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO refresh layout and listView
//        setContentView(R.layout.activity_universities);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("OnCreate", "UniversitiesListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                link = bundle.getString(INTENT_KEY_UNIVERSITY_ACTIVITY);
            }
        }

//        mListView = (ListView)findViewById(R.id.listViewUniversityActivity);
        mListView = (ListView)findViewById(R.id.listViewUniversal);
        mTextViewHeadText = (TextView)findViewById(R.id.textViewHeadUniversalListView);
        mTextViewHeadText.setText(R.string.textChooseYouUniversity);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(UniversitiesListActivity.this, UniversitiesDetailListActivity.class);
                intent.putExtra(UniversitiesDetailListActivity.KEY_UNIVERSITY_LINK,
                        mUniversityLinks.get(position));
                intent.putExtra(UniversitiesDetailListActivity.KEY_UNIVERSITY_TITLE,
                        mUniversitiesName.get(position));
                startActivity(intent);
                Log.d("My", "position = " + position + "id = " + id);

            }
        });

        new ParseUniversityList().execute();
        mAdapter = new ArrayAdapter<>(UniversitiesListActivity.this, android.R.layout.simple_list_item_1,
                mUniversitiesName);
        Log.d("My", "onCreate   link ->" + link);
    }

    public class ParseUniversityList extends AsyncTask<String, Integer, String> {

        ProgressDialog progDailog = new ProgressDialog(UniversitiesListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDailog.setMessage(getString(R.string.textResourceLoading));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // TODO правильно обработать и распарсить данныем по ВНЗ
            Document document;
            try {
                document = Jsoup.connect(link).get();
                Log.d("My", "UniversitiesListActivity -> ParseUniversityList - > documentLink"  + document.text());

                Elements elementsByClass = document.getElementsByClass("accordion-inner");
                Elements elementsText = elementsByClass.select("a");
                for (Element element: elementsText){
                    mUniversitiesName.add(element.text());
                    mUniversityLinks.add(element.attr("abs:href"));
                }
//                Element elementUnivers = document.getElementById("okrArea");
//                Elements links = elementUnivers.getElementsByTag("a");
//
//                mUniversityLinks.clear();
//                mUniversitiesName.clear();
//
//                for (Element link : links) {
//                    mUniversityLinks.add(link.attr("abs:href"));
//                    mUniversitiesName.add(link.text());
//                }

                Log.d("My", "doInBackground   mUniversitiesName ->" + mUniversitiesName);
                Log.d("My", "doInBackground   mUniversityLinks ->" + mUniversityLinks);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mAdapter);
            progDailog.dismiss();

        }
    }
}
