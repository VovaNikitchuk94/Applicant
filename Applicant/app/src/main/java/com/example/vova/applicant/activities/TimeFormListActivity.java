package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.TimeFormAdapter;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.TimeFormEngine;
import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TimeFormListActivity extends BaseActivity implements
        TimeFormAdapter.OnClickTimeFormItem {

    public static final String KEY_TIME_FORM_LINK = "KEY_TIME_FORM_LINK";

    public static final String INT_FULL_TIME_FORM = "den";
    public static final String INT_EXTERNAL_FORM = "zao";
    public static final String INT_DISTANCE_FORM = "dis";
    public static final String INT_EVENING_FORM = "vec";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    private TimeFormAdapter mTimeFormAdapter;
    private DetailUniverInfo mDetailUniverInfo;
    private Calendar mCalendar;

    private long mLongDetailUNVId = -1;
    private String mDetailCodeLink = "";

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        Log.d("My", "TimeFormListActivity --------> initActivity");

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_TIME_FORM_LINK);
                if (mDetailUniverInfo != null) {
                    mLongDetailUNVId = mDetailUniverInfo.getId();
                    mDetailCodeLink = mDetailUniverInfo.getStrDetailLink();
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_time_form_swipe_refresh_layout);
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

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewСhooseTimeFormTimeFormActivity);
        textViewHeadText.setText(mDetailUniverInfo.getStrDetailText());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTimeFormListActivity);
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
        return R.layout.activity_time_form_list;
    }

    private void setData() {
        TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
        if (timeFormEngine.getAllTimeFormsById(mLongDetailUNVId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            }else {
                parseData(DBConstants.Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    parseData(DBConstants.Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void getData() {
        TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
        ArrayList<TimeFormInfo> timeFormInfos = timeFormEngine.getAllTimeFormsById(mLongDetailUNVId);
        mTimeFormAdapter = new TimeFormAdapter(timeFormInfos);
        mTimeFormAdapter.setOnClickTimeFormItem(TimeFormListActivity.this);
        mRecyclerView.setAdapter(mTimeFormAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    //TODO почему и зачем? проверить!
    @Override
    public void onClickTimeFormItem(TimeFormInfo timeFormInfo) {
        Intent intent = new Intent(TimeFormListActivity.this, SpecialtiesListActivity.class);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, timeFormInfo);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE_STRING, mDetailUniverInfo.getStrDetailText());
        startActivity(intent);
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

    private void parseData(final boolean isNeedUpdate) {

        new Thread(new Runnable() {
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
            int timeFormCode;

            @Override
            public void run() {
                if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId != 0) {
                    parse();

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
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

            private void parse() {
                Document document;
                try {
                    document = Jsoup.connect(mDetailCodeLink).get();
                    Element elementById = document.getElementById("okrArea");
                    Elements elementsSelectId = elementById.select("ul#myTab");

                    String[] findConstant = mDetailUniverInfo.getStrDetailLink().split("#");
                    String timeFormName = findConstant[1].substring(0, 3);
                    switch (timeFormName) {
                        case INT_FULL_TIME_FORM:
                            timeFormCode = 0;
                            loopElementsParse(elementsSelectId, timeFormCode);
                            break;
                        case INT_EXTERNAL_FORM:
                            timeFormCode = 1;
                            loopElementsParse(elementsSelectId, timeFormCode);
                            break;
                        case INT_EVENING_FORM:
                            timeFormCode = 2;
                            loopElementsParse(elementsSelectId, timeFormCode);
                            break;
                        case INT_DISTANCE_FORM:
                            if (elementsSelectId.size() > 3) {
                                timeFormCode = 3;
                            } else {
                                timeFormCode = 2;
                            }
                            loopElementsParse(elementsSelectId, timeFormCode);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void loopElementsParse(Elements elementsSelectId, int timeCode) {
                ArrayList<TimeFormInfo> timeFormInfos = new ArrayList<>();

                Elements elements  = elementsSelectId.get(timeCode).select("li");
                for (Element element : elements) {
                    String textTimeForm = element.select("a").text();
                    String linkTimeForm = element.select("a").attr("abs:href");
                    String dateUpdate = mDetailUniverInfo.getStrDateLastUpdate();
                    timeFormInfos.add(new TimeFormInfo(mLongDetailUNVId, textTimeForm, linkTimeForm, dateUpdate));
                }

                if (isNeedUpdate) {
                    timeFormEngine.updateAllTimeForms(timeFormInfos);
                } else {
                    timeFormEngine.addAllTimeForms(timeFormInfos);
                }
            }
        }).start();
    }
}
