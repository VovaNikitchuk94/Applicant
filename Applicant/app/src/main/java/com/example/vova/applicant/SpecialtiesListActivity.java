package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SpecialtiesListActivity extends AppCompatActivity {

    public static final String KEY_SPECIALITIES_TITLE = "KEY_SPECIALITIES_TITLE";
    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";
    public static final String KEY_DEGREE = "KEY_DEGREE";

    private String mStrBachelorLink = "";
    private int mIntDegree = 0;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mSpecialtiesNames = new ArrayList<>();
    private ArrayList<String> mSpecialtiesLink = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("My", "SpecialtiesListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mStrBachelorLink = bundle.getString(KEY_SPECIALITIES_LINK);
                mIntDegree = bundle.getInt(KEY_DEGREE);
            }
        }

        mListView = (ListView) findViewById(R.id.listViewUniversal);

        new ParseBachelorList().execute();
        mAdapter = new ArrayAdapter<>(SpecialtiesListActivity.this,
                android.R.layout.simple_list_item_1,
                mSpecialtiesNames);
        Log.d("My", "onCreate   link ->" + mStrBachelorLink);
        Log.d("My", "onCreate   degree ->" + mIntDegree);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
                intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY,
                        mSpecialtiesLink.get((int) id));
                startActivity(intent);
                Log.d("My", "SpecialtiesListActivity -> position = " + position);
                Log.d("My", "SpecialtiesListActivity ->  mSpecialtiesLink.get(position) = " + mSpecialtiesLink.get(position));
            }
        });

    }

    public class ParseBachelorList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Document document;
            try {
                document = Jsoup.connect(mStrBachelorLink).get();

                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > html -> "  + mStrBachelorLink);

                Elements links = document.select("tr");
                Elements elements = document.getElementsByClass("button button-mini");

                for (Element link : links) {
                    if (mIntDegree == 5) {
                        if (link.text().contains("Бакалавр")) {
                            mSpecialtiesNames.add(link.text());
                            mSpecialtiesLink.add(elements.attr("abs:href"));
                        }
                    }
                    if (mIntDegree == 6) {
                        if (link.text().contains("Спеціаліст")) {
                            mSpecialtiesNames.add(link.text());
                            mSpecialtiesLink.add(elements.attr("abs:href"));
                        }
                    }
                    if (mIntDegree == 7) {
                        if (link.text().contains("Магістр")) {
                            mSpecialtiesNames.add(link.text());
                            mSpecialtiesLink.add(elements.attr("abs:href"));
                        }
                    }
                }

                int asd = mSpecialtiesLink.size();
                int asdqw = mSpecialtiesNames.size();
                Log.d("My", " SpecialtiesListActivity  -> doInBackground   mSpecialtiesNames ->" + mSpecialtiesNames);
                Log.d("My", "SpecialtiesListActivity -> doInBackground   mUniversityLinks ->" + mSpecialtiesLink);
                Log.d("My", "SpecialtiesListActivity -> doInBackground   mSpecialtiesNames count ->" + asd);
                Log.d("My", "SpecialtiesListActivity -> doInBackground   mUniversityLinks ->" + asdqw);

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
