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

import com.example.vova.applicant.R;
import com.example.vova.applicant.utils.Utils;
import com.example.vova.applicant.adapters.TimeFormAdapter;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.TimeFormEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TimeFormListActivity extends BaseActivity implements
        TimeFormAdapter.OnClickTimeFormItem {

    private DetailUniverInfo mDetailUniverInfo;

    public static final String KEY_TIME_FORM_LINK = "KEY_TIME_FORM_LINK";

    public static final String INT_FULL_TIME_FORM = "den";
    public static final String INT_EXTERNAL_FORM = "zao";
    public static final String INT_DISTANCE_FORM = "dis";
    public static final String INT_EVENING_FORM = "vec";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    private long mLongDetailUNVId = 0;

    @Override
    protected void iniActivity() {
        Log.d("My", "TimeFormListActivity --------> iniActivity");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_TIME_FORM_LINK);
            }
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_time_form_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                parseData();
                Log.d("My","SwipeRefreshLayout -> parseData -> is start");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewСhooseTimeFormTimeFormActivity);
        textViewHeadText.setText(mDetailUniverInfo.getStrDetailText());
        Log.d("My", "onCreate   mStrFullTimeLink ->" + mDetailUniverInfo.getStrDetailText());

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

    @Override
    public void setDrawer() {
        super.setDrawer();
    }

    private void setData() {
        mLongDetailUNVId = mDetailUniverInfo.getId();
        TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
        if (timeFormEngine.getAllTimeFormsById(mLongDetailUNVId).isEmpty()){
            parseData();
        } else {
            getData(timeFormEngine);
        }
    }

    private void getData(TimeFormEngine timeFormEngine) {
        ArrayList<TimeFormInfo> timeFormInfos = timeFormEngine.getAllTimeFormsById(mLongDetailUNVId);
        TimeFormAdapter formAdapter = new TimeFormAdapter(timeFormInfos);
        formAdapter.setOnClickTimeFormItem(TimeFormListActivity.this);
        mRecyclerView.setAdapter(formAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickTimeFormItem(TimeFormInfo timeFormInfo) {
        Intent intent = new Intent(TimeFormListActivity.this, SpecialtiesListActivity.class);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, timeFormInfo);
        startActivity(intent);
    }

    private void parseData() {

        new Thread(new Runnable() {
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId != 0) {
                    parse();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        getData(timeFormEngine);
                    }
                });
            }

            private void parse() {
                String html;
                html = mDetailUniverInfo.getStrDetailLink();
                Document document;
                try {
                    document = Jsoup.connect(html).get();
                    Element elementById = document.getElementById("okrArea");
                    Elements elementsSelectId = elementById.select("ul#myTab");
                    //TODO доделать этот кошмар правильно обработать формы обучения
                    Elements elements;
                    String[] findConstant = mDetailUniverInfo.getStrDetailLink().split("#");
                    String s = findConstant[1].substring(0 , 3);
                    Log.d("My","s -_----------------- > " + s);
                    switch (s) {
                        case INT_FULL_TIME_FORM:
                            elements = elementsSelectId.get(0).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EXTERNAL_FORM:
                            elements = elementsSelectId.get(1).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EVENING_FORM:
                            elements = elementsSelectId.get(2).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_DISTANCE_FORM:
                            elements = elementsSelectId.get(3).select("li");
                            loopElementsParse(elements);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void loopElementsParse(Elements elements) {
                if (timeFormEngine.getAllTimeFormsById(mLongDetailUNVId).isEmpty()){
                    for (Element element : elements) {
                        String text = element.select("a").text();
                        String link = element.select("a").attr("abs:href");
                        timeFormEngine.addTimeForm(new TimeFormInfo(mLongDetailUNVId, text, link));
                    }
                } else {
                    for (Element element : elements) {
                        String text = element.select("a").text();
                        String link = element.select("a").attr("abs:href");
                        timeFormEngine.updateTimeForm(new TimeFormInfo(mLongDetailUNVId, text, link));
                    }
                }
            }
        }).start();
    }
}
