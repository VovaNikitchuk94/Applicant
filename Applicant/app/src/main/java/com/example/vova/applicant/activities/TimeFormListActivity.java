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
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
    private String mDetailCodeLink = "";

    @Override
    protected void iniActivity() {
        Log.d("My", "TimeFormListActivity --------> iniActivity");

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
                Log.d("My", "SwipeRefreshLayout -> updateData -> is start");
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "TimeFormListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
                    Log.d("My", "TimeFormListActivity -> (isDateComparison()) updateData(); is finished");
                }
                Log.d("My", "SwipeRefreshLayout -> updateData -> is finish");

                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewСhooseTimeFormTimeFormActivity);
        textViewHeadText.setText(mDetailUniverInfo.getStrDetailText());
        Log.d("My", "TimeFormListActivity onCreate   mStrFullTimeLink ->" + mDetailUniverInfo.getStrDetailText());

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
            }
            Log.d("My", "TimeFormListActivity -> parseData");
            parseData();

        } else {

            if (isDateComparison()) {
                Log.d("My", "TimeFormListActivity -> isDateComparison  getData(citiesInfoEngine); ");
                getData(timeFormEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "TimeFormListActivity -> (isDateComparison()) updateData() is started;");
                    updateData();
                    Log.d("My", "TimeFormListActivity -> (isDateComparison()) updateData(); is finished");

                }
            }
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
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE_STRING, mDetailUniverInfo.getStrDetailText());
        startActivity(intent);
    }

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = timeFormEngine.getTimeFormById(mLongDetailUNVId).getStrDateLastUpdate();

                Log.d("My", "isDateComparison dateAndTimeCities -> " + dateAndTime);

                if (parseDate.equals(dateAndTime)) {
                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
                    return true;
                } else {
                    return false;
                }
            }
        };

        FutureTask<Boolean> task = new FutureTask<>(callable);
        Thread t = new Thread(task);
        t.start();

        try {
            Log.d("My", " isDateComparison task.get() -> " + task.get());
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String parseDateAndTime() {
        Document document;
        String dateUpdateAndTime = null;
        try {
            document = Jsoup.connect(mDetailCodeLink).get();

            //TODO при обновлении нужно затирать всю цепочку связаных данных в БД

            //get timeUpdate and dateUpdate update page
            String strLastUpdatePage = document.select("div.title-page > small").text();
            Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage);
            String[] arrayTimeDate = strLastUpdatePage.split(" ");

            dateUpdateAndTime = arrayTimeDate[3] + "@" + arrayTimeDate[5];
            Log.d("My", "dateUpdateAndTime -> " + dateUpdateAndTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dateUpdateAndTime;
    }

    private void parseData() {

        new Thread(new Runnable() {
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId != 0) {
                    parse();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(timeFormEngine);
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

            private void parse() {
                Document document;
                try {
                    document = Jsoup.connect(mDetailCodeLink).get();
                    Element elementById = document.getElementById("okrArea");
                    Elements elementsSelectId = elementById.select("ul#myTab");
                    //TODO доделать этот кошмар правильно обработать формы обучения
                    Elements elements;
                    String[] findConstant = mDetailUniverInfo.getStrDetailLink().split("#");
                    String s = findConstant[1].substring(0, 3);
                    Log.d("My", "s -_----------------- > " + s);
                    switch (s) {
                        case INT_FULL_TIME_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(0).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EXTERNAL_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(1).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EVENING_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(2).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_DISTANCE_FORM:
                            //TODO падает приложение в 2016, универ гетьмана
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            if (elementsSelectId.size() > 3) {
                                elements = elementsSelectId.get(3).select("li");
                            } else {
                                elements = elementsSelectId.get(2).select("li");
                            }
                            loopElementsParse(elements);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void loopElementsParse(Elements elements) {
                for (Element element : elements) {
                    String textTimeForm = element.select("a").text();
                    String linkTimeForm = element.select("a").attr("abs:href");
                    String dateUpdate = mDetailUniverInfo.getStrDateLastUpdate();
                    timeFormEngine.addTimeForm(new TimeFormInfo(mLongDetailUNVId, textTimeForm, linkTimeForm, dateUpdate));
                }
            }
        }).start();
    }

    private void updateData() {

        new Thread(new Runnable() {
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mDetailUniverInfo.getStrDetailLink()) && mLongDetailUNVId != 0) {
                    update();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getData(timeFormEngine);
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

            private void update() {
                Document document;
                try {
                    document = Jsoup.connect(mDetailCodeLink).get();
                    Element elementById = document.getElementById("okrArea");
                    Elements elementsSelectId = elementById.select("ul#myTab");
                    //TODO доделать этот кошмар правильно обработать формы обучения
                    Elements elements;
                    String[] findConstant = mDetailUniverInfo.getStrDetailLink().split("#");
                    String s = findConstant[1].substring(0, 3);
                    Log.d("My", "s -_----------------- > " + s);
                    switch (s) {
                        case INT_FULL_TIME_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(0).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EXTERNAL_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(1).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_EVENING_FORM:
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            elements = elementsSelectId.get(2).select("li");
                            loopElementsParse(elements);
                            break;
                        case INT_DISTANCE_FORM:
                            //TODO падает приложение в 2016, универ гетьмана
                            Log.d("My", "INT_FULL_TIME_FORM is pressed -> " + s);
                            if (elementsSelectId.size() > 3) {
                                elements = elementsSelectId.get(3).select("li");
                            } else {
                                elements = elementsSelectId.get(2).select("li");
                            }
                            loopElementsParse(elements);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void loopElementsParse(Elements elements) {
                for (Element element : elements) {
                    String textTimeForm = element.select("a").text();
                    String linkTimeForm = element.select("a").attr("abs:href");
                    String dateUpdate = mDetailUniverInfo.getStrDateLastUpdate();
                    timeFormEngine.updateTimeForm(new TimeFormInfo(mLongDetailUNVId, textTimeForm, linkTimeForm, dateUpdate));
                }
            }
        }).start();
    }
}
