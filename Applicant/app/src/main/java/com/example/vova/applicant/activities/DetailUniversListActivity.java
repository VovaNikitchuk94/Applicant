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
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.DetailUniversAdapter;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.DetailUniverInfoEngine;
import com.example.vova.applicant.model.engines.UniversityInfoEngine;

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

    private long universityDetailLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_univers_list);

        Log.d("My", "UniversityPageActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                universityDetailLink = bundle.getLong(KEY_DETAIL_UNIVERSITY_LINK, -1);
            }
        }

        Log.d("My", "UniversityPageActivity -> OnCreate -> universityDetailLink : " + universityDetailLink);

        TextView mTextViewHeadText = (TextView) findViewById(R.id.textViewHeadUniversalListView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewDetailUniversListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        new ParseUniversitiesDetail().execute();
    }

    @Override
    public void onClickDetailUniversItem(long nIdDetailUnivers) {

        //TODO setClickListener for all items
        final DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
        DetailUniverInfo detailUniverInfo = detailUniverInfoEngine.getDetailUniverById(nIdDetailUnivers);

        Intent intent = null;
        String errorMsg = getString(R.string.error);
        switch ((int) detailUniverInfo.getId()) {
            case 1:
                intent = new Intent(this, AboutUniversityActivity.class);
                //TODO try use absolute link
                intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY, nIdDetailUnivers);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(DetailUniversListActivity.this, TimeFormListActivity.class);
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                        detailUniverInfo.getStrDetailText());
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                        detailUniverInfo.getStrDetailLink());
                intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                        TimeFormListActivity.INT_FULL_TIME_FORM);
                break;
            case 3:
                intent = new Intent(DetailUniversListActivity.this, TimeFormListActivity.class);
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                        detailUniverInfo.getStrDetailText());
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                        detailUniverInfo.getStrDetailLink());
                intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                        TimeFormListActivity.INT_EXTERNAL_FORM);
                Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity " +
                        "-> universityDetailLink" + universityDetailLink);
                Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity" +
                        " ->universityDetailLink" + universityDetailLink);
                break;
            case 4:
                intent = new Intent(DetailUniversListActivity.this, TimeFormListActivity.class);
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                        detailUniverInfo.getStrDetailText());
                intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                        detailUniverInfo.getStrDetailLink());
                intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                        TimeFormListActivity.INT_DISTANCE_FORM);
                Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity " +
                        "-> universityDetailLink" + universityDetailLink);
                Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity" +
                        " ->universityDetailLink" + universityDetailLink);

                break;
        }
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(DetailUniversListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
            String html = "";
            UniversityInfoEngine universityInfoEngine = new UniversityInfoEngine(getApplication());
            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
            if (detailUniverInfoEngine.getAllDetailUnivers().isEmpty()) {
                UniversityInfo universityInfo = universityInfoEngine.getUniversityById(universityDetailLink);
                if (universityDetailLink > -1 && universityInfo != null) {
                    html = universityInfo.getStrUniversityLink();
                    Log.d("My", "ParseUniversities doInBackground  html ->" + html);
                    Document document;
                    try {
                        document = Jsoup.connect(html).get();

                        Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                        Elements elementsText = elementsByClass.select("a");
                        for (Element element : elementsText) {

                            String detailUniversityName = element.text();
                            String detailUniversityLink = element.attr("abs:href");

                            detailUniverInfoEngine.addDetailUniver(new DetailUniverInfo(detailUniversityName,
                                    detailUniversityLink));
                            Log.d("My", "ParseUniversities doInBackground  addDetailUniver link ->" + element.attr("abs:href"));
                            Log.d("My", "ParseUniversities doInBackground  addDetailUniver  name ->" + element.text());

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
            mDetailUniverInfos = detailUniverInfoEngine.getAllDetailUnivers();
            mDetailUniversAdapter = new DetailUniversAdapter(mDetailUniverInfos);
            mDetailUniversAdapter.setOnClickDetailUniversItem(DetailUniversListActivity.this);
            mRecyclerView.setAdapter(mDetailUniversAdapter);
            progDailog.dismiss();
        }
    }
}
