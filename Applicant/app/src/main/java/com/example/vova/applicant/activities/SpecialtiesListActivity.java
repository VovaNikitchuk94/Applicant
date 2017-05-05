package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.SpecialitiesAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;
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

    private String mStrSpecialtiesLink = "";

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
                mStrSpecialtiesLink = bundle.getString(KEY_SPECIALITIES_LINK, null);
            }
        }
        Log.d("My", "SpecialtiesListActivity -> OnCreate mStrSpecialtiesLink -> " + mStrSpecialtiesLink);

        TextView textView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSpecialityListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        new ParseSpecialitiesList().execute();

    }

    @Override
    public void onClickSpecialityItem(long nIdSpeciality) {
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        SpecialtiesInfo position = specialityInfoEngine.getSpecialityById(nIdSpeciality);

        Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
        intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY,
                position.getStrLink());
        startActivity(intent);
    }

    private class ParseSpecialitiesList extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog = new ProgressDialog(SpecialtiesListActivity.this);

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
            Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > doInBackground");
            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
//            if (specialityInfoEngine.getAllSpecialities().isEmpty() && mStrSpecialtiesLink != null) {
            Document document;
            String specialty;
            String applications;
            String accepted = "";
            String amount;
            String newLink;

            String strCategory = mStrSpecialtiesLink.substring(mStrSpecialtiesLink.length() - 5,
                    mStrSpecialtiesLink.length());
            int intDegree = Integer.parseInt(mStrSpecialtiesLink.substring(mStrSpecialtiesLink.length() - 1));

            try {
                document = Jsoup.connect(mStrSpecialtiesLink).get();

                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > documentLink" + document.text());
                Log.d("My", "SpecialtiesListActivity -> ParseSpecialistList - > html -> " + mStrSpecialtiesLink);

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
                    Log.d("My", "doInBackground + + specialty + applications + accepted + amount + newLink" + specialty +
                            ";" + applications + ";" + accepted + ";" + amount + newLink + ";");
//                    specialityInfoEngine.addSpeciality(new SpecialtiesInfo(specialty, applications, accepted, amount, newLink));

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
//            } else {
////                  mSpecialtiesInfos = specialityInfoEngine.getAllSpecialities();
//                Log.d("My", "mSpecialtiesInfosmSpecialtiesInfosmSpecialtiesInfosmSpecialtiesInfos" + mSpecialtiesInfos.size());
//            }

            return null;
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
            return mStrSpecialtiesLink.contains("2015");
        }

        private void addNameAndLink(String specialty, String applications, String amount, String newLink) {
            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
            if (!newLink.equals("")) {
                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(specialty, applications, amount, newLink));
            }
        }

        private void addNameAndLink(String specialty, String applications, String accepted,
                                    String amount, String newLink) {
            SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
            if (!newLink.equals("")) {
                specialityInfoEngine.addSpeciality(new SpecialtiesInfo(specialty, applications, accepted, amount, newLink));
            }

        }

        @Override
        protected void onPostExecute(String srt) {
            final SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
            mSpecialtiesInfos = specialityInfoEngine.getAllSpecialities();
            mSpecialitiesAdapter = new SpecialitiesAdapter(mSpecialtiesInfos);
            mSpecialitiesAdapter.setOnClickSpecialityItem(SpecialtiesListActivity.this);
            mRecyclerView.setAdapter(mSpecialitiesAdapter);
            progDailog.dismiss();

        }
    }
}
