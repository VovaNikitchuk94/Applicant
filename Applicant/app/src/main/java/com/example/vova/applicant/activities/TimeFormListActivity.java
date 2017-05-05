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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.TimeFormAdapter;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.TimeFormEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TimeFormListActivity extends AppCompatActivity implements
        TimeFormAdapter.OnClickTimeFormItem{

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";
    public static final String KEY_TIME_FORM = "KEY_TIME_FORM";

    public static final int INT_FULL_TIME_FORM = 1;
    public static final int INT_EXTERNAL_FORM = 2;
    public static final int INT_DISTANCE_FORM = 3;

    private RecyclerView mRecyclerView;
    private TimeFormAdapter mFormAdapter;
    private ArrayList<TimeFormInfo> mTimeFormInfos = new ArrayList<>();

    private TextView mTextViewHeadText;

    private String mStrFullTimeName;
    private String mStrFullTimeLink;
    private int mIntTimeForm = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form_list);

        Log.d("OnCreate", "UniversityPageActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mStrFullTimeName = bundle.getString(KEY_DEGREE_TITLE);
                mStrFullTimeLink = bundle.getString(KEY_DEGREE_LINK, null);
                mIntTimeForm = bundle.getInt(KEY_TIME_FORM);
            }
        }

        mTextViewHeadText = (TextView) findViewById(R.id.textViewСhooseTimeFormTimeFormActivity);
        mTextViewHeadText.setText(mStrFullTimeName);
        Log.d("My", "onCreate   mStrFullTimeName ->" + mStrFullTimeName);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTimeFormListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        new ParseTimeForm().execute();

    }

    @Override
    public void onClickTimeFormItem(long nIdTimeForm) {
        final TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
        TimeFormInfo timeFormInfo = timeFormEngine.getTimeFormById(nIdTimeForm);

        Intent intent = new Intent(TimeFormListActivity.this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, timeFormInfo.getStrTimeFormLink());
                startActivity(intent);

    }

    private class ParseTimeForm extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progDailog = new ProgressDialog(TimeFormListActivity.this);

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
        protected Void doInBackground(Void... voids) {

//            DetailUniverInfoEngine detailUniverInfoEngine = new DetailUniverInfoEngine(getApplication());
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
            if (timeFormEngine.getAllTimeForms().isEmpty() && mStrFullTimeLink != null){
                Document document;
                try {
                    document = Jsoup.connect(mStrFullTimeLink).get();

                    //TODO обработать дистаниционную форму обучения и посмотреть если ли еще другие формы

                    Element elementById = document.getElementById("okrArea");
                    Elements elementsSelectId = elementById.select("ul#myTab");

                    //TODO доделать этот кошмар
                    Elements elements;

                    switch (mIntTimeForm) {
                        case 1:
                            elements = elementsSelectId.get(0).select("li");
                            loopElementsParse(elements);
                            break;
                        case 2:
                            elements = elementsSelectId.get(1).select("li");
                            loopElementsParse(elements);
                            break;
                        case 3:
                            elements = elementsSelectId.get(2).select("li");
                            loopElementsParse(elements);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        private void loopElementsParse(Elements elements) {
            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
            for (Element element : elements) {
                String text = element.select("a").text();
                String link = element.select("a").attr("abs:href");

                timeFormEngine.addTimeForm(new TimeFormInfo(text, link));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());
            mTimeFormInfos = timeFormEngine.getAllTimeForms();
            mFormAdapter = new TimeFormAdapter(mTimeFormInfos);
            mFormAdapter.setOnClickTimeFormItem(TimeFormListActivity.this);
            mRecyclerView.setAdapter(mFormAdapter);
            progDailog.dismiss();
        }
    }
}
