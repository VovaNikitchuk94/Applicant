package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class CitiesListActivity extends AppCompatActivity {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mCitiesArray = new ArrayList<>();
    private ArrayList<String> mCitiesLinks = new ArrayList<>();

    private TextView mTextView;

    private String yearsCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        mListView = (ListView) findViewById(R.id.listViewCitiesListActivity);
        mTextView = (TextView) findViewById(R.id.textViewСhooseCityMainActivity);
        mTextView.setText(getText(R.string.chooseCityMainActivity));

        Intent intent = getIntent();
        if (intent != null){

             Bundle bundle = intent.getExtras();
            if (bundle != null){

                yearsCode = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
            }
        }

        new ParseCitiesList().execute();
        mAdapter = new ArrayAdapter<>(CitiesListActivity.this, android.R.layout.simple_list_item_1,
                mCitiesArray);
        Log.d("My", "onCreate   mCitiesArray ->" + mCitiesArray);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            Intent intent = new Intent(CitiesListActivity.this, UniversitiesListActivity.class);
                            intent.putExtra(UniversitiesListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, mCitiesLinks.get(position));
                            startActivity(intent);
                    Log.d("My", "position = " + position + " id = " + id);
            }
        });
    }

    public class ParseCitiesList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = yearsCode;
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Element elementRegion = document.getElementById("region");
                Elements links = elementRegion.getElementsByTag("a");

                for (Element link : links) {
                    mCitiesLinks.add(link.attr("href"));
                    mCitiesArray.add(link.text());
                }

                Log.d("My", "doInBackground   mCitiesLinks ->" + mCitiesLinks);
                Log.d("My", "doInBackground   mCitiesArray ->" + mCitiesArray);

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
