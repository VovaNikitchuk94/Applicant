package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mYearsArray = new ArrayList<>();

    TextView mTextView;
    String name;
    String cite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listViewMainActivity);
        mTextView = (TextView) findViewById(R.id.textViewMainActivity);


        new ParseYears().execute();
        mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                mYearsArray);
        Log.d("My", "onCreate   mYearsArray ->" + mYearsArray);

    }

    public class ParseYears extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String html = "http://vstup.info/";
            Document document;
            try {
                document = Jsoup.connect(html).get();
                Elements years = document.select("a[href]");

//                Elements elements666 = document.select("a");

                mYearsArray.clear();

//                for (Element element : elements666){
//                    mYearsArray.add(element.attr("href"));
//                }
//                String elements = elements666.attr("href");
//                name = elements;
//               cite = String.valueOf(document.select("a[href]").first());

                for (Element element : years) {
                    if (element.text().contains("рік"))
                        mYearsArray.add(element.text());
                }

                Log.d("My", "doInBackground   mYearsArray ->" + mYearsArray);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mAdapter);
//            Log.d("My", "onPostExecute  mYearsArray ->" + mYearsArray);
//            mTextView.setText(name);
//            mTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
//                    intent.putExtra("Tag", name);
//                    startActivity(intent);
//
//                }
//            });
        }
    }
}
