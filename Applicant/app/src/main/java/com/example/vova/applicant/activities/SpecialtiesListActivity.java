package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.SpecialtiesInfoAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SpecialtiesListActivity extends AppCompatActivity {

    // TODO rename all fields

    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";

    private String mStrSpecialtiesLink = "";

    private ListView mListView;
    private ArrayList<String> mSpecialtiesLink = new ArrayList<>();
    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();

    private SpecialtiesInfoAdapter mSpecialtiesInfoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);
//        setContentView(R.layout.activity_test);

        Log.d("My", "SpecialtiesListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mStrSpecialtiesLink = bundle.getString(KEY_SPECIALITIES_LINK);
            }
        }

        mListView = (ListView) findViewById(R.id.listViewUniversal);

        new ParseBachelorList().execute();

        mSpecialtiesInfoAdapter = new SpecialtiesInfoAdapter(SpecialtiesListActivity.this,
                R.layout.list_item_specialties_info, mSpecialtiesInfos);
        Log.d("My", "onCreate   link ->" + mStrSpecialtiesLink);

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


        ProgressDialog progDailog = new ProgressDialog(SpecialtiesListActivity.this);

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

            Document document;
            String specialty;
            String applications;
            String accepted = "";
            String amount;

            String strCategory = mStrSpecialtiesLink.substring(mStrSpecialtiesLink.length() - 5,
                    mStrSpecialtiesLink.length());
            int intDegree = Integer.parseInt(mStrSpecialtiesLink.substring(mStrSpecialtiesLink.length() - 1));

            try {
                document = Jsoup.connect(mStrSpecialtiesLink).get();


                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > html -> " + mStrSpecialtiesLink);

                Element form = document.getElementById(strCategory);

                Elements links = form.select("tbody > tr");

                mSpecialtiesLink.clear();
                mSpecialtiesInfos.clear();

                for (Element link : links) {

                    Elements elements = link.getElementsByClass("button button-mini");
                    Elements tds = link.select("td");

                    if (isContains2015()) {
                        specialty = tds.get(0).text();
                        applications = tds.get(1).text();
                        amount = "ліцензований обсяг: " + tds.get(2).text();
                    } else {
                        specialty = tds.get(0).text();
                        applications = tds.get(1).select("span").text();
                        accepted = tds.get(1).select("nobr").text();
                        amount = tds.get(2).text();
                    }
                    Log.d("My", "doInBackground + + specialty + applications + accepted + amount" + specialty +
                            ";" + applications + ";" + accepted + ";" + amount + ";");

                    switch (intDegree) {
                        case 1:
                            if (link.text().contains("Бакалавр")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, elements);
                            }
                            break;
                        case 3:
                            if (link.text().contains("Спеціаліст")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, elements);
                            }
                            break;
                        case 2:
                            if (link.text().contains("Магістр")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, elements);
                            }
                            break;
                        case 4:
                            if (link.text().contains("Молодший спеціаліст")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, elements);
                            }
                            break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void parseDataOfDegree(String specialty, String applications, String accepted,
                                       String amount, Elements elements) {
            if (isContains2015()) {
                addNameAndLink(specialty, applications, amount, elements);
            } else {
                addNameAndLink(specialty, applications, accepted,
                        amount, elements);
            }
        }

        private boolean isContains2015() {
            if (mStrSpecialtiesLink.contains("2015")) {
                return true;
            } else {
                return false;
            }
        }

        private void addNameAndLink(String specialty, String applications, String amount, Elements elements) {
            if (!elements.attr("abs:href").equals("")) {
                mSpecialtiesInfos.add(new SpecialtiesInfo(specialty, applications, amount));
                mSpecialtiesLink.add(elements.attr("abs:href"));

            }
        }

        private void addNameAndLink(String specialty, String applications, String accepted,
                                    String amount, Elements elements) {
            if (!elements.attr("abs:href").equals("")) {
                mSpecialtiesInfos.add(new SpecialtiesInfo(specialty, applications, accepted, amount));
                mSpecialtiesLink.add(elements.attr("abs:href"));
            }

        }

        @Override
        protected void onPostExecute(String srt) {
            progDailog.dismiss();
            mListView.setAdapter(mSpecialtiesInfoAdapter);
        }
    }
}
