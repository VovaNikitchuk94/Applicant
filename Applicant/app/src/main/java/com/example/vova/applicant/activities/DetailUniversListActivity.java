package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.DetailUniversAdapter;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.DetailUniverInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailUniversListActivity extends BaseActivity implements
        DetailUniversAdapter.OnClickDetailUniversItem {

    public static final String KEY_DETAIL_UNIVERSITY_LINK = "KEY_DETAIL_UNIVERSITY_LINK";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<DetailUniverInfo> mDetailUniverInfos = new ArrayList<>();

    private UniversityInfo mUniversityInfo;

    private long mLongDetailUNVId = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_univers_list);

        Log.d("My", "DetailUniversListActivity -> OnCreate");

        drawerAndToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mUniversityInfo = (UniversityInfo) bundle.get(KEY_DETAIL_UNIVERSITY_LINK);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_detail_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParseUniversitiesDetail().execute();
                    }
                }, 0);
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
    public void drawerAndToolbar() {
        super.drawerAndToolbar();
    }

    private void setData() {
        mLongDetailUNVId = mUniversityInfo.getId();
        DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
        if (detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId).isEmpty()) {
            new ParseUniversitiesDetail().execute();
        } else {
            getData(detailUniverInfoEngine);
        }
    }

    private void getData(DetailUniverInfoEngine detailUniverInfoEngine) {
        mDetailUniverInfos = detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId);
        DetailUniversAdapter detailUniversAdapter = new DetailUniversAdapter(mDetailUniverInfos);
        detailUniversAdapter.setOnClickDetailUniversItem(DetailUniversListActivity.this);
        mRecyclerView.setAdapter(detailUniversAdapter);
    }

    @Override
    public void onClickDetailUniversItem(DetailUniverInfo detailUniverInfo) {
        Intent intent;

        if (detailUniverInfo.equals(mDetailUniverInfos.get(0))) {
            intent = new Intent(this, AboutUniversityActivity.class);
            intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY, detailUniverInfo);
            startActivity(intent);
        } else {
            if (detailUniverInfo.getStrDetailText().contains("(0)")){
                Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(this, TimeFormListActivity.class);
                intent.putExtra(TimeFormListActivity.KEY_TIME_FORM_LINK, detailUniverInfo);
                startActivity(intent);
            }
        }
    }

    private class ParseUniversitiesDetail extends AsyncTask<Void, Void, Void> {
        ProgressDialog progDailog = new ProgressDialog(DetailUniversListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage(getString(R.string.textResourceLoading));
            progDailog.setIndeterminate(true);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());

            if (Utils.connectToData(mUniversityInfo.getStrUniversityLink()) && mLongDetailUNVId != 0) {
                parse(mLongDetailUNVId, detailUniverInfoEngine);
            }
            return null;
        }

        private void parse(long longDetailUNVId, DetailUniverInfoEngine detailUniverInfoEngine) {
            String html;
            html = mUniversityInfo.getStrUniversityLink();
            Log.d("My", "ParseUniversities doInBackground  mHtml ->" + html);
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                Elements elementsText = elementsByClass.select("a");
                if (detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId).isEmpty()) {
                    for (Element element : elementsText) {

                        String detailUniversityName = element.text();
                        String detailUniversityLink = element.attr("abs:href");

                        detailUniverInfoEngine.addDetailUniver(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                                detailUniversityLink));
                        Log.d("My", "ParseUniversities doInBackground  addDetailUniver link ->" + element.attr("abs:href"));
                        Log.d("My", "ParseUniversities doInBackground  addDetailUniver  detailUniversityLink ->" + detailUniversityLink);
                    }
                } else {
                    for (Element element : elementsText) {
                        String detailUniversityName = element.text();
                        String detailUniversityLink = element.attr("abs:href");

                        detailUniverInfoEngine.updateDetailUniver(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                                detailUniversityLink));
                        Log.d("My", "ParseUniversities doInBackground  addDetailUniver link ->" + element.attr("abs:href"));
                        Log.d("My", "ParseUniversities doInBackground  addDetailUniver  detailUniversityLink ->" + detailUniversityLink);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
            getData(detailUniverInfoEngine);
            progDailog.dismiss();
        }
    }
}
