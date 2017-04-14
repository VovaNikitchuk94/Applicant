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
    public static final String KEY_SPECIALITIES_CATEGORY = "KEY_SPECIALITIES_CATEGORY";
    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";
    public static final String KEY_DEGREE = "KEY_DEGREE";

    private String mStrBachelorLink = "";
    private int mIntDegree = 0;
    private String mCategory = "";

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
                mCategory = bundle.getString(KEY_SPECIALITIES_CATEGORY);
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
                        mSpecialtiesLink.get(position));
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
//                document = Jsoup.connect("http://www.vstup.info/2016/i2016i82.html#vnz").get();

                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > html -> " + mStrBachelorLink);

                Element form = document.getElementById(mCategory);
//                Log.d("My", "SpecialtiesListActivity -> doInBackground   form ->" + form);

                Elements links = form.select("tbody > tr");

//                Elements d01 = document.getElementById("z_o_1").getAllElements();
//                Elements links = d01.addClass("tablesaw tablesaw-stack tablesaw-sortable");
//                Elements elements = links.select("tbody");
//                Elements tr = elements.select("tr");
//                Elements element = tr.addClass("tablesaw-cell-content");
//                Elements lin = element.select("a[href]");

                mSpecialtiesLink.clear();
                mSpecialtiesNames.clear();

                for (Element link : links) {
                    Elements elements = link.getElementsByClass("button button-mini");

                    switch (mIntDegree) {
                        case 5:
                            if (link.text().contains("Бакалавр")) {
                                addNameAndLink(link, elements);
                            }
                            break;
                        case 6:
                            if (link.text().contains("Спеціаліст")) {
                                addNameAndLink(link, elements);
                            }
                            break;
                        case 7:
                            if (link.text().contains("Магістр")) {
                                addNameAndLink(link, elements);
                            }
                            break;
                        case 8:
                            if (link.text().contains("Молодший спеціаліст")) {
                                addNameAndLink(link, elements);
                            }
                            break;
                    }
                }


                mIntDegree = 0;

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

        private void addNameAndLink(Element link, Elements elements) {
            if (!elements.attr("abs:href").equals("")){
                mSpecialtiesNames.add(link.text());
                mSpecialtiesLink.add(elements.attr("abs:href"));
            }

        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mAdapter);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("My", "SpecialtiesListActivity onStart   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onStart   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("My", "SpecialtiesListActivity onStop   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onStop   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("My", "SpecialtiesListActivity onDestroy   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onDestroy   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("My", "SpecialtiesListActivity onPause   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onPause   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("My", "SpecialtiesListActivity onResume   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onResume   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("My", "SpecialtiesListActivity onRestart   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onRestart   mSpecialtiesLink ->" + mSpecialtiesLink);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        this.mAdapter.clear();
//        this.mAdapter.notifyDataSetChanged();
//       mListView.setAdapter(null);
//        mAdapter.notifyDataSetChanged();
        Log.d("My", "SpecialtiesListActivity onBackPressed   mSpecialtiesNames ->" + mSpecialtiesNames);
        Log.d("My", "SpecialtiesListActivity onBackPressed   mSpecialtiesLink ->" + mSpecialtiesLink);
        Log.d("My", "SpecialtiesListActivity doInBackground   mAdapter.getCount ->" + mAdapter.getCount());
        Log.d("My", "SpecialtiesListActivity doInBackground  mListView ->" + mListView.getCount());

    }
}
