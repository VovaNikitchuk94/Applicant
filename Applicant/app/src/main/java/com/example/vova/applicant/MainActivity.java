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

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mCitiesArray = new ArrayList<>();
    ArrayList<String> mCitiesLinks = new ArrayList<>();

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listViewMainActivity);
        mTextView = (TextView) findViewById(R.id.textViewСhooseCityMainActivity);
        mTextView.setText(getText(R.string.chooseCityMainActivity));

        new ParseYears().execute();
        mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                mCitiesArray);
        Log.d("My", "onCreate   mCitiesArray ->" + mCitiesArray);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            Intent intent = new Intent(MainActivity.this, UniversitiesActivity.class);
                            intent.putExtra(UniversitiesActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, mCitiesLinks.get(position));
                            startActivity(intent);
                    Log.d("My", "position = " + position + "id = " + id);

            }
        });
    }

    public class ParseYears extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = "http://www.vstup.info/2016/";
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
