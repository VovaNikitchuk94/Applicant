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

public class DetailUniversListActivity extends BaseActivity implements
        DetailUniversAdapter.OnClickDetailUniversItem {

    public static final String KEY_DETAIL_UNIVERSITY_LINK = "KEY_CATEGORY_UNIVERSITY_LINK";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private ArrayList<DetailUniverInfo> mDetailUniverInfos = new ArrayList<>();
    private UniversityInfo mUniversityInfo;

    private long mLongDetailUNVId = 0;
    private String mUniversityCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "DetailUniversListActivity --------> initActivity");

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
                Log.d("My", "SwipeRefreshLayout -> updateData -> is start");
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    updateData();
                }
                Log.d("My", "SwipeRefreshLayout -> updateData -> is finish");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
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
            }
            Log.d("My", "DetailUniverInfoEngine -> parseData");
            parseData();

        } else {

            //TODO разобраться что за хуйня здесь происходит
            if (isDateComparison()) {
                Log.d("My", "DetailUniverInfoEngine -> isDateComparison  getData(); ");
                getData(detailUniverInfoEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "DetailUniverInfoEngine -> (isDateComparison()) updateData() is started;");
                    updateData();
                    Log.d("My", "DetailUniverInfoEngine -> (isDateComparison()) updateData(); is finished");

                }
            }
        }
    }

    private void getData(DetailUniverInfoEngine detailUniverInfoEngine) {
        mDetailUniverInfos = detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId);
        DetailUniversAdapter detailUniversAdapter = new DetailUniversAdapter(mDetailUniverInfos);
        detailUniversAdapter.setOnClickDetailUniversItem(DetailUniversListActivity.this);
        mRecyclerView.setAdapter(detailUniversAdapter);
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

    private String parseDateAndTime() {

        Document document;
        String dateUpdateAndTime = null;
        try {
            document = Jsoup.connect(mUniversityCodeLink).get();

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

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = detailUniverInfoEngine.getDetailUniverById(mLongDetailUNVId).getStrDateLastUpdate();

                Log.d("My", " isDateComparison parseDate -> " + parseDate);
                Log.d("My", " isDateComparison dateAndTime) -> " + dateAndTime);

                if (parseDate.equals(dateAndTime)) {

                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
                    return true;
                } else {
                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + false);
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

    private void parseData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());

                if (Utils.connectToData(mUniversityCodeLink) && mLongDetailUNVId != 0) {
                    parse(mLongDetailUNVId, detailUniverInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
                            getData(detailUniverInfoEngine);
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

            private void parse(long longDetailUNVId, DetailUniverInfoEngine detailUniverInfoEngine) {

                Document document;
                try {
                    document = Jsoup.connect(mUniversityCodeLink).get();

                    String dateUpdate = parseDateAndTime();

                    Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                    Elements elementsText = elementsByClass.select("a");
                    for (Element element : elementsText) {

                        String detailUniversityName = element.text();
                        String detailUniversityLink = element.attr("abs:href");

                        detailUniverInfoEngine.addDetailUniver(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                                detailUniversityLink, dateUpdate));
                        Log.d("My", "DetailUniversListActivity doInBackground  addDetailUniver link ->" + element.attr("abs:href"));
                        Log.d("My", "DetailUniversListActivity doInBackground  addDetailUniver  dateUpdate ->" + dateUpdate);
                        Log.d("My", "DetailUniversListActivity doInBackground  addDetailUniver  detailUniversityLink ->" + detailUniversityLink);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void updateData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());

                if (Utils.connectToData(mUniversityCodeLink) && mLongDetailUNVId != 0) {
                    update(mLongDetailUNVId, detailUniverInfoEngine);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
                            getData(detailUniverInfoEngine);
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

            private void update(long longDetailUNVId, DetailUniverInfoEngine detailUniverInfoEngine) {

                Document document;
                try {
                    document = Jsoup.connect(mUniversityCodeLink).get();

                    String dateUpdate = parseDateAndTime();
//                    String dateUpdate = "01/02/17@06:02";

                    Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                    Elements elementsText = elementsByClass.select("a");

                    for (Element element : elementsText) {
                        String detailUniversityName = element.text();
                        String detailUniversityLink = element.attr("abs:href");

                        detailUniverInfoEngine.updateDetailUniver(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                                detailUniversityLink, dateUpdate));
                        Log.d("My", "DetailUniversListActivity doInBackground  update link ->" + element.attr("abs:href"));
                        Log.d("My", "DetailUniversListActivity doInBackground  update  detailUniversityLink ->" + detailUniversityLink);
                        Log.d("My", "DetailUniversListActivity doInBackground  update  dateUpdate ->" + dateUpdate);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
