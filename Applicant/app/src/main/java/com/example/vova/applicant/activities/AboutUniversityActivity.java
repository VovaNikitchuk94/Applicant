package com.example.vova.applicant.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.AboutUniversityAdapter;
import com.example.vova.applicant.model.AboutUniversityInfo;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.engines.AboutUniversityEngine;
import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AboutUniversityActivity extends BaseActivity implements
        AboutUniversityAdapter.OnClickAboutUniversityItem {

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY = "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private DetailUniverInfo mDetailUniverInfo;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Calendar mCalendar;

    private long mLongDetailUNVId;
    private String mDetailCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "AboutUniversityActivity -> OnCreate");

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_ABOUT_UNIVERSITY_ACTIVITY);
                if (mDetailUniverInfo != null) {
                    mLongDetailUNVId = mDetailUniverInfo.getId();
                    mDetailCodeLink = mDetailUniverInfo.getStrDetailLink();
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_about_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(DBConstants.Update.NEED_AN_UPDATE);
                }
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
    protected int getLayoutId() {
        return R.layout.activity_about_university_list;
    }

    private void setData() {
        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());

        if (aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                parseData(DBConstants.Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(DBConstants.Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void getData() {
        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
        ArrayList<AboutUniversityInfo> aboutUniversityInfos = aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId);
        AboutUniversityAdapter universityInfoAdapter = new AboutUniversityAdapter(aboutUniversityInfos);
        universityInfoAdapter.setOnClickListenerAdapter(AboutUniversityActivity.this);
        mRecyclerView.setAdapter(universityInfoAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private Boolean isDateComparison() {

        Calendar calendarCurrentTime = Calendar.getInstance();
        if (Utils.isNeedToEqualsTime()) {

            mCalendar.after(calendarCurrentTime);
            return true;
        } else {
            mCalendar = Utils.getModDeviceTime();
            return false;
        }
    }

    @Override
    public void onClickAboutUniversityItem(long nIdItem) {

        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
        AboutUniversityInfo myPosition = aboutUniversityEngine.getAboutUniversityById(nIdItem);
        Intent intent;
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
//              intent.setPackage("com.google.android.apps.maps");
                break;
            case "Телефони:":
                //TODO правильность написания номера и если у номера нет кода города
                String[] phoneNumbers = new String[0];
                if ((dataLink.contains(",") || dataLink.contains(";"))) {
                    if (dataLink.contains(",")) {
                        phoneNumbers = dataLink.split(",");
                    } else if (dataLink.contains(";")) {
                        phoneNumbers = dataLink.split(";");
                    }

                    //add country or city code for all phones
                    if (phoneNumbers[0].length() > phoneNumbers[1].length()) {
                        int numberFirstCount = phoneNumbers[0].length();
                        int numberSecondCount = phoneNumbers[1].length();
                        Log.d("My", "numberFirstCount -> " + numberFirstCount);
                        Log.d("My", "numberSecondCount -> " + numberSecondCount);

                        String phoneCode = (phoneNumbers[0].substring(0, numberFirstCount - numberSecondCount));
                        Log.d("My", "phoneCode -> " + phoneCode);
                        for (int i = 1; i < phoneNumbers.length; i++) {
                            phoneNumbers[i] = phoneCode + phoneNumbers[i];
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
                break;
            case "E-mail:":
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dataLink));
                startActivity(intent);
                break;
        }
    }

    private void parseData(final boolean isNeedUpdate) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());

                if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId > -1) {
                    parse(mLongDetailUNVId, aboutUniversityEngine, isNeedUpdate);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                getData();
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                mProgressBar.setVisibility(View.VISIBLE);
                                getData();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void parse(long longDetailUNVId, AboutUniversityEngine aboutUniversityEngine, boolean isNeedUpdate) {

                String wrongDog = " [ at ] ";
                String correctDog = "@";
                Document document;
                ArrayList<AboutUniversityInfo> aboutUniversityInfos = new ArrayList<>();
                try {
                    document = Jsoup.connect(mDetailCodeLink).get();

                    Element elementAboutUniversities = document.getElementById("about");
                    Elements elements = elementAboutUniversities.getElementsByTag("tr");

                    for (Element link : elements) {
                        String type = link.select("td").first().text();
                        String data = link.select("td").last().text();

                        if (!data.isEmpty()) {
                            data = data.replace(wrongDog, correctDog);
                            aboutUniversityInfos.add(new AboutUniversityInfo(longDetailUNVId, type, data));
                        }
                    }

                    if (isNeedUpdate) {
                        aboutUniversityEngine.updateAllAboutUniversities(aboutUniversityInfos);
                    } else {
                        aboutUniversityEngine.addAllAboutUniversities(aboutUniversityInfos);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
