package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.AboutUniversityAdapter;
import com.example.vova.applicant.model.AboutUniversityInfo;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.engines.AboutUniversityEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutUniversityActivity extends BaseActivity implements
        AboutUniversityAdapter.OnClickAboutUniversityItem {

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY =
            "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private DetailUniverInfo mDetailUniverInfo;

    private long mLongDetailUNVId;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_university_list);

        Log.d("My", "AboutUniversityActivity -> OnCreate");

        drawerAndToolbar();

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_ABOUT_UNIVERSITY_ACTIVITY);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_about_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParseAboutUniversityList().execute();
                    }
                }, 0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView titleTextView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        titleTextView.setText(mDetailUniverInfo.getStrDetailText());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAboutUniversityActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    @Override
    public void drawerAndToolbar() {
        super.drawerAndToolbar();
    }

    private void setData() {
        mLongDetailUNVId = mDetailUniverInfo.getId();
        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
        if (aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId).isEmpty()) {
            new ParseAboutUniversityList().execute();
        } else {
            getData(aboutUniversityEngine);
        }
    }

    private void getData(AboutUniversityEngine aboutUniversityEngine) {
        ArrayList<AboutUniversityInfo> aboutUniversityInfos = aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId);
        AboutUniversityAdapter universityInfoAdapter = new AboutUniversityAdapter(aboutUniversityInfos);
        universityInfoAdapter.setOnClickListenerAdapter(AboutUniversityActivity.this);
        mRecyclerView.setAdapter(universityInfoAdapter);
    }

    @Override
    public void onClickAboutUniversityItem(long nIdItem) {

        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
        AboutUniversityInfo myPosition = aboutUniversityEngine.getAboutUniversityById(nIdItem);
        Intent intent = null;
        String dataLink = myPosition.getStrAboutUniversData();

        switch (myPosition.getStrAboutUniversType()) {
            case "Веб-сайт:":
                if ((!dataLink.contains("http://"))) {
                    dataLink = "http://" + dataLink;
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataLink));
                startActivity(intent);
                break;
            case "Адреса:":
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + dataLink);
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(intent);
                //open for google Maps
//                        intent.setPackage("com.google.android.apps.maps");
                break;
            case "Телефони:":
                //TODO правильность написания номера и если у номера нет кода города
                String[] phoneNumbers = new String[0];
                if ((dataLink.contains(",") || dataLink.contains(";"))) {
//                    if (dataLink.contains(",") && dataLink.contains(";")) {
//                        phoneNumbers = dataLink.split(",");
//                        for (int i = 0; i < phoneNumbers.length; i++) {
//                            String[] newArr = phoneNumbers[i].split(";");
//                            Log.d("My", "newArr" + newArr.toString());
//                        }
//                        phoneNumbers = dataLink.split(";");
                    if (dataLink.contains(",")) {
                        phoneNumbers = dataLink.split(",");
                    } else if (dataLink.contains(";")) {
                        phoneNumbers = dataLink.split(";");
                    }

                    if (phoneNumbers[0].length() > phoneNumbers[1].length()) {
                        if (phoneNumbers[0].startsWith("+")) {
                            String countryCode = (phoneNumbers[0]).substring(0, 8);
                            for (int i = 1; i < phoneNumbers.length; i++) {
                                phoneNumbers[i] = countryCode + phoneNumbers[i];
                            }
                            Log.d("My", "case \"Телефони:\": cityCode->>>>>" + countryCode);
                        } else {
                            String cityCode;
                            if (phoneNumbers[0].startsWith("(")) {
                                cityCode = (phoneNumbers[0]).substring(0, 5);
                                Log.d("My", "case \"Телефони:\": cityCode->>>>>" + cityCode);
                            } else {
                                cityCode = (phoneNumbers[0]).substring(0, 3);
                            }

                            for (int i = 1; i < phoneNumbers.length; i++) {
                                phoneNumbers[i] = cityCode + phoneNumbers[i];
                            }
                        }
                    }
                    final String[] finalPhoneNumber = phoneNumbers;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AboutUniversityActivity.this);
                    builder.setTitle(myPosition.getStrAboutUniversType())
                            .setItems(phoneNumbers, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + finalPhoneNumber[which]));
                                    startActivity(intent);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataLink));
                    startActivity(intent);
                }

                Log.d("My", "phoneNumbers " + phoneNumbers.length);
//                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataLink));
                break;
            case "E-mail:":
                //TODO обработать несколько имейлов
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dataLink));
                startActivity(intent);
                break;
        }
    }

    private class ParseAboutUniversityList extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog = new ProgressDialog(AboutUniversityActivity.this);

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
            AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplicationContext());

            if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId != 0) {
                parse(mLongDetailUNVId, aboutUniversityEngine);
            }
            return null;
        }

        private void parse(long longDetailUNVId, AboutUniversityEngine aboutUniversityEngine) {
            String html;
            html = mDetailUniverInfo.getStrDetailLink();
            Log.d("My", "ParseAboutUniversityList doInBackground  mHtml ->" + html);
            String wrongDog = " [ at ] ";
            String correctDog = "@";
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements elements = elementAboutUniversities.getElementsByTag("tr");

                //add new element to array only when text with data isn't empty
                if (aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId).isEmpty()) {
                    for (Element link : elements) {
                        String type = link.select("td").first().text();
                        String data = link.select("td").last().text();

                        if (!data.isEmpty()) {
                            data = data.replace(wrongDog, correctDog);
                            aboutUniversityEngine.addAboutUniversity(new AboutUniversityInfo(longDetailUNVId, type, data));
                            Log.d("My", "doInBackground   link.select(\"td\").first().text() ->" + link.select("td").first().text());
                            Log.d("My", "doInBackground    link.select(\"td\").last().text()) ->" + link.select("td").last().text());
                        }
                    }
                } else {
                    for (Element link : elements) {
                        String type = link.select("td").first().text();
                        String data = link.select("td").last().text();

                        if (!data.isEmpty()) {
                            data = data.replace(wrongDog, correctDog);
                            aboutUniversityEngine.updateAboutUniversity(new AboutUniversityInfo(longDetailUNVId, type, data));
                            Log.d("My", "doInBackground   link.select(\"td\").first().text() ->" + link.select("td").first().text());
                            Log.d("My", "doInBackground    link.select(\"td\").last().text()) ->" + link.select("td").last().text());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String srt) {
            AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
            getData(aboutUniversityEngine);
            progDailog.dismiss();
        }
    }
}
