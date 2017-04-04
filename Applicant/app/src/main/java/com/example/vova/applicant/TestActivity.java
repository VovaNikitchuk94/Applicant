package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TestActivity extends AppCompatActivity {

    String s;


    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
       s = intent.getStringExtra("Tag");

        mListView = (ListView)findViewById(R.id.listViewtest);


        new ParseYears().execute();
        mAdapter = new ArrayAdapter<>(TestActivity.this, android.R.layout.simple_list_item_1,
                mStrings);
        Log.d("My", "onCreate   mYearsArray ->" + s);
    }

    public class ParseYears extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = "http://vstup.info";
            Document document;
            try {
                document = Jsoup.connect(html + s).get();
                Elements cities = document.select("tr");

                mStrings.clear();

                for (Element element : cities){
                    mStrings.add(element.text());
                }

                Log.d("My", "doInBackground   mYearsArray ->" + mStrings);

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
