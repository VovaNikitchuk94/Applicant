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
import android.widget.Toast;

import com.example.vova.applicant.R;
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

public class DetailUniversListActivity extends AppCompatActivity implements
        DetailUniversAdapter.OnClickDetailUniversItem {

    public static final String KEY_DETAIL_UNIVERSITY_LINK = "KEY_DETAIL_UNIVERSITY_LINK";

    private RecyclerView mRecyclerView;
    private DetailUniversAdapter mDetailUniversAdapter;
    private ArrayList<DetailUniverInfo> mDetailUniverInfos = new ArrayList<>();

    private UniversityInfo mUniversityInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_univers_list);

        Log.d("My", "DetailUniversListActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mUniversityInfo = (UniversityInfo) bundle.get(KEY_DETAIL_UNIVERSITY_LINK);
            }
        }

        TextView mTextViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        mTextViewHeadText.setText(mUniversityInfo.getStrUniversityName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewDetailUniversListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseUniversitiesDetail().execute();
    }

    @Override
    public void onClickDetailUniversItem(DetailUniverInfo detailUniverInfo) {
        Intent intent;

        if (detailUniverInfo == mDetailUniverInfos.get(0)){
            intent = new Intent(this, AboutUniversityActivity.class);
            intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY, detailUniverInfo);
            startActivity(intent);
        } else {
            intent = new Intent(this, TimeFormListActivity.class);
            intent.putExtra(TimeFormListActivity.KEY_TIME_FORM_LINK, detailUniverInfo);
            startActivity(intent);
        }
    }

    private class ParseUniversitiesDetail extends AsyncTask<Void, Void, Void> {

        ProgressDialog progDailog = new ProgressDialog(DetailUniversListActivity.this);
        long mLongDetailUNVId = mUniversityInfo.getId();

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

                if (detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId).isEmpty()) {
                    parse(mLongDetailUNVId, detailUniverInfoEngine);
                }
            return null;
        }

        private void parse(long longDetailUNVId, DetailUniverInfoEngine detailUniverInfoEngine) {
            String html;
            html = mUniversityInfo.getStrUniversityLink();
            Log.d("My", "ParseUniversities doInBackground  html ->" + html);
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                Elements elementsText = elementsByClass.select("a");
                for (Element element : elementsText) {

                    String detailUniversityName = element.text();
                    String detailUniversityLink = element.attr("abs:href");

                    detailUniverInfoEngine.addDetailUniver(new DetailUniverInfo(longDetailUNVId, detailUniversityName,
                            detailUniversityLink));
                    Log.d("My", "ParseUniversities doInBackground  addDetailUniver link ->" + element.attr("abs:href"));
                    Log.d("My", "ParseUniversities doInBackground  addDetailUniver  name ->" + element.text());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
            mDetailUniverInfos = detailUniverInfoEngine.getAllDetailUniversById(mLongDetailUNVId);
            mDetailUniversAdapter = new DetailUniversAdapter(mDetailUniverInfos);
            mDetailUniversAdapter.setOnClickDetailUniversItem(DetailUniversListActivity.this);
            mRecyclerView.setAdapter(mDetailUniversAdapter);
            progDailog.dismiss();
        }
    }
}
