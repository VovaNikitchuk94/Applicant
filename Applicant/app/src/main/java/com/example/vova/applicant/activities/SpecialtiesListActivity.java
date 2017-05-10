package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.SpecialitiesAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.SpecialityInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SpecialtiesListActivity extends AppCompatActivity implements
        SpecialitiesAdapter.OnClickSpecialityItem {

    // TODO rename all fields

    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";

    private TimeFormInfo mTimeFormInfo;

    private RecyclerView mRecyclerView;
    private SpecialitiesAdapter mSpecialitiesAdapter;
    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speciality_list);

        Log.d("My", "SpecialtiesListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mTimeFormInfo = (TimeFormInfo) bundle.get(KEY_SPECIALITIES_LINK);
            }
        }

        TextView textView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        textView.setText(mTimeFormInfo.getStrTimeFormName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSpecialityListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseSpecialitiesList().execute();

    }

    @Override
    public void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo) {
        Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
        intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY, specialtiesInfo);
        startActivity(intent);
    }

    private class ParseSpecialitiesList extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog = new ProgressDialog(SpecialtiesListActivity.this);
        final SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

        long mLongTimeFormId = mTimeFormInfo.getId();
        String html = mTimeFormInfo.getStrTimeFormLink();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > onPre");
            progDailog.setMessage(getString(R.string.textResourceLoading));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
                parse();
            }
            return null;
        }

        private void parse() {
            Document document;
            String specialty;
            String applications;
            String accepted = "";
            String amount;
            String newLink;

            String strCategory = html.substring(html.length() - 5, html.length());
            int intDegree = Integer.parseInt(html.substring(html.length() - 1));

            try {
                document = Jsoup.connect(html).get();

                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > html -> " + html);

                Element form = document.getElementById(strCategory);
                Elements links = form.select("tbody > tr");

                mSpecialtiesInfos.clear();

                for (Element link : links) {
                    Elements elements = link.getElementsByClass("button button-mini");
                    Elements tds = link.select("td");

                    if (isContains2015()) {
                        specialty = tds.get(0).text();
                        applications = tds.get(1).text();
                        amount = "ліцензований обсяг: " + tds.get(2).text();
                        newLink = elements.attr("abs:href");
                    } else {
                        specialty = tds.get(0).text();
                        applications = tds.get(1).select("span").text();
                        accepted = tds.get(1).select("nobr").text();
                        amount = tds.get(2).text();
                        newLink = elements.attr("abs:href");
                    }

                    switch (intDegree) {
                        case 1:
                            if (link.text().contains("Бакалавр")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, newLink);
                            }
                            break;
                        case 3:
                            if (link.text().contains("Спеціаліст")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, newLink);
                            }
                            break;
                        case 2:
                            if (link.text().contains("Магістр")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, newLink);
                            }
                            break;
                        case 4:
                            if (link.text().contains("Молодший спеціаліст")) {
                                parseDataOfDegree(specialty, applications, accepted, amount, newLink);
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void parseDataOfDegree(String specialty, String applications, String accepted,
                                       String amount, String newLink) {
            if (isContains2015()) {
                addNameAndLink(specialty, applications, amount, newLink);
            } else {
                addNameAndLink(specialty, applications, accepted, amount, newLink);
            }
        }

        private boolean isContains2015() {
            return html.contains("2015");
        }

        private void addNameAndLink(String specialty, String applications, String amount, String newLink) {
            if (!newLink.equals("")) {
                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, specialty, applications, amount, newLink));
            }
        }

        private void addNameAndLink(String specialty, String applications, String accepted,
                                    String amount, String newLink) {
            if (!newLink.equals("")) {
                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(mLongTimeFormId, specialty, applications, accepted, amount, newLink));
            }
        }

        @Override
        protected void onPostExecute(String srt) {
            mSpecialtiesInfos = specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId);
            mSpecialitiesAdapter = new SpecialitiesAdapter(mSpecialtiesInfos);
            mSpecialitiesAdapter.setOnClickSpecialityItem(SpecialtiesListActivity.this);
            mRecyclerView.setAdapter(mSpecialitiesAdapter);
            progDailog.dismiss();

        }
    }
}
