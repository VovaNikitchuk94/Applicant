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

import com.example.vova.applicant.R;
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

public class TimeFormListActivity extends AppCompatActivity implements
        TimeFormAdapter.OnClickTimeFormItem {

    private DetailUniverInfo mDetailUniverInfo;

    public static final String KEY_TIME_FORM_LINK = "KEY_TIME_FORM_LINK";

    public static final String INT_FULL_TIME_FORM = "den";
    public static final String INT_EXTERNAL_FORM = "zao";
    public static final String INT_DISTANCE_FORM = "dis";
    public static final String INT_EVENING_FORM = "vec";

    private RecyclerView mRecyclerView;
    private TimeFormAdapter mFormAdapter;
    private ArrayList<TimeFormInfo> mTimeFormInfos = new ArrayList<>();

    private TextView mTextViewHeadText;

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
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_TIME_FORM_LINK);
            }
        }

        mTextViewHeadText = (TextView) findViewById(R.id.textViewСhooseTimeFormTimeFormActivity);
        mTextViewHeadText.setText(mDetailUniverInfo.getStrDetailText());
        Log.d("My", "onCreate   mStrFullTimeLink ->" + mDetailUniverInfo.getStrDetailText());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTimeFormListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseTimeForm().execute();

    }

    @Override
    public void onClickTimeFormItem(TimeFormInfo timeFormInfo) {
        Intent intent = new Intent(TimeFormListActivity.this, SpecialtiesListActivity.class);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, timeFormInfo);
        startActivity(intent);
    }

    private class ParseTimeForm extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progDailog = new ProgressDialog(TimeFormListActivity.this);
        long mLongDetailUNVId = mDetailUniverInfo.getId();
        final TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

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

            TimeFormEngine timeFormEngine = new TimeFormEngine(getApplication());

                if (timeFormEngine.getAllTimeFormsById(mLongDetailUNVId).isEmpty()){
                    parse();
                }

            return null;
        }

        private void parse() {
            String html;
            html = mDetailUniverInfo.getStrDetailLink();
            Document document;
            try {
                document = Jsoup.connect(html).get();
                Element elementById = document.getElementById("okrArea");
                Elements elementsSelectId = elementById.select("ul#myTab");
                //TODO доделать этот кошмар/ правильно обработать формы обучения
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
            for (Element element : elements) {
                String text = element.select("a").text();
                String link = element.select("a").attr("abs:href");
                timeFormEngine.addTimeForm(new TimeFormInfo(mLongDetailUNVId, text, link));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mTimeFormInfos = timeFormEngine.getAllTimeFormsById(mLongDetailUNVId);
            mFormAdapter = new TimeFormAdapter(mTimeFormInfos);
            mFormAdapter.setOnClickTimeFormItem(TimeFormListActivity.this);
            mRecyclerView.setAdapter(mFormAdapter);
            progDailog.dismiss();
        }
    }
}
