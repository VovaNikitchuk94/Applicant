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
import com.example.vova.applicant.adapters.DetailUniversAdapter;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.DetailUniverInfoEngine;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Update;
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

public class DetailUniversListActivity extends BaseActivity implements
        DetailUniversAdapter.OnClickDetailUniversItem {

    public static final String KEY_DETAIL_UNIVERSITY_LINK = "KEY_CATEGORY_UNIVERSITY_LINK";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private ArrayList<DetailUniverInfo> mDetailUniverInfos = new ArrayList<>();
    private DetailUniversAdapter mDetailUniversAdapter;
    private UniversityInfo mUniversityInfo;
    private Calendar mCalendar;

    private long mLongDetailUNVId = -1;
    private String mUniversityCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "DetailUniversListActivity --------> initActivity");

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mUniversityInfo = (UniversityInfo) bundle.get(KEY_DETAIL_UNIVERSITY_LINK);
                if (mUniversityInfo != null) {
                    mLongDetailUNVId = mUniversityInfo.getId();
                    mUniversityCodeLink = mUniversityInfo.getStrUniversityLink();
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_detail_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        });

        TextView mTextViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        mTextViewHeadText.setText(mUniversityInfo.getStrUniversityName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewDetailUniversListActivity);
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
        return R.layout.activity_detail_univers_list;
    }

    private void setData() {
        DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
        if (detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                parseData(Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void getData() {
        DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
        mDetailUniverInfos = detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId);
        mDetailUniversAdapter = new DetailUniversAdapter(mDetailUniverInfos);
        mDetailUniversAdapter.setOnClickDetailUniversItem(DetailUniversListActivity.this);
        mRecyclerView.setAdapter(mDetailUniversAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickDetailUniversItem(DetailUniverInfo detailUniverInfo) {
        Intent intent;

        if (detailUniverInfo.equals(mDetailUniverInfos.get(0))) {
            intent = new Intent(this, AboutUniversityActivity.class);
            intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY, detailUniverInfo);
            startActivity(intent);
        } else {
            if (detailUniverInfo.getStrDetailText().contains("(0)")) {

                Toast.makeText(this, R.string.textEmptyData, Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(this, TimeFormListActivity.class);
                intent.putExtra(TimeFormListActivity.KEY_TIME_FORM_LINK, detailUniverInfo);
                startActivity(intent);
            }
        }
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
            @Override
            public void run() {
                DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());

                if (Utils.connectToData(mUniversityCodeLink) && mLongDetailUNVId > -1) {
                    parse(mLongDetailUNVId, detailUniverInfoEngine, isNeedUpdate);

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

            private void parse(long longDetailUNVId, DetailUniverInfoEngine detailUniverInfoEngine, boolean isNeedUpdate) {
                Document document;
                ArrayList<DetailUniverInfo> detailUniverInfos = new ArrayList<>();
                try {
                    document = Jsoup.connect(mUniversityCodeLink).get();

                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3] + "@" + arrayTimeDate[5];

                    Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                    Elements elementsText = elementsByClass.select("a");
                    for (Element element : elementsText) {

                        String detailUniversityName = element.text();
                        String detailUniversityLink = element.attr("abs:href");

                        detailUniverInfos.add(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                                detailUniversityLink, dateUpdate));
                    }

                    if (isNeedUpdate) {
                        detailUniverInfoEngine.updateAllDetailInfo(detailUniverInfos);
                    } else {
                        detailUniverInfoEngine.addAllDetailInfo(detailUniverInfos);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
