package com.example.vova.applicant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vova.applicant.activities.SpecialtiesListActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TimeFormPageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";
    public static final String KEY_TIME_FORM = "KEY_TIME_FORM";

    public static final int INT_FULL_TIME_FORM = 1;
    public static final int INT_EXTERNAL_FORM = 2;

    private static final String KEY_BACHELOR_FULL_TIME = "d_o_1";
    private static final String KEY_SPECIALIST_FULL_TIME = "d_o_3";
    private static final String KEY_MASTER_FULL_TIME = "d_o_2";
    private static final String KEY_JUNIOR_SPECIALIST_FULL_TIME = "d_o_4";

    private static final String KEY_BACHELOR_EXTERNAL = "z_o_1";
    private static final String KEY_SPECIALIST_EXTERNAL = "z_o_3";
    private static final String KEY_JUNIOR_SPECIALIST_EXTERNAL = "z_o_4";
    private static final String KEY_MASTER_EXTERNAL = "z_o_2";

    private TextView mTextViewDegree;

    private Button mButtonBachelor;
    private Button mButtonMaster;
    private Button mButtonSpecialist;
    private Button mButtonJuniorSpecialist;

    private String mStrFullTimeName;
    private String mStrFullTimeLink;
    private int mIntTimeForm = 0;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_form_page);

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mStrFullTimeName = bundle.getString(KEY_DEGREE_TITLE);
                mStrFullTimeLink = bundle.getString(KEY_DEGREE_LINK);
                mIntTimeForm = bundle.getInt(KEY_TIME_FORM);
            }
        }

        ParseTimeForm parseTimeForm = new ParseTimeForm();
        parseTimeForm.execute();
        initializeView();
    }

    private void initializeView() {
        mTextViewDegree = (TextView) findViewById(R.id.textViewChooseDegreeTimeFormPageActivity);
        mTextViewDegree.setText(getText(R.string.textChooseDegreeTimeFormPageActivity));

        mButtonBachelor = (Button) findViewById(R.id.buttonBachelorDegreeTimeFormPageActivity);
        mButtonSpecialist = (Button) findViewById(R.id.buttonSpecialistDegreeTimeFormPageActivity);
        mButtonJuniorSpecialist = (Button) findViewById(R.id.buttonJuniorSpecialistDegreeTimeFormPageActivity);
        mButtonMaster = (Button) findViewById(R.id.buttonMasterDegreeTimeFormPageActivity);

        mButtonBachelor.setOnClickListener(this);
        mButtonSpecialist.setOnClickListener(this);
        mButtonJuniorSpecialist.setOnClickListener(this);
        mButtonMaster.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonBachelorDegreeTimeFormPageActivity:
                intent = getIntent(mStrFullTimeName, mStrFullTimeLink, 5);
                if (mIntTimeForm == INT_FULL_TIME_FORM) {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_BACHELOR_FULL_TIME);
                } else {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_BACHELOR_EXTERNAL);
                }
                break;
            case R.id.buttonSpecialistDegreeTimeFormPageActivity:
                intent = getIntent(mStrFullTimeName, mStrFullTimeLink, 6);
                if (mIntTimeForm == INT_FULL_TIME_FORM) {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_SPECIALIST_FULL_TIME);
                } else {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_SPECIALIST_EXTERNAL);
                }
                break;
            case R.id.buttonJuniorSpecialistDegreeTimeFormPageActivity:
                intent = getIntent(mStrFullTimeName, mStrFullTimeLink, 8);
                if (mIntTimeForm == INT_FULL_TIME_FORM) {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_JUNIOR_SPECIALIST_FULL_TIME);
                } else {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_JUNIOR_SPECIALIST_EXTERNAL);
                }
                break;
            case R.id.buttonMasterDegreeTimeFormPageActivity:
                intent = getIntent(mStrFullTimeName, mStrFullTimeLink, 7);
                if (mIntTimeForm == INT_FULL_TIME_FORM) {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_MASTER_FULL_TIME);
                } else {
                    intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_MASTER_EXTERNAL);
                }
                break;
        }

        startActivity(intent);

    }

    @NonNull
    public Intent getIntent(String timeName, String link, int degree) {
//        Intent intent;
        intent = new Intent(this, SpecialtiesListActivity.class);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, timeName);
        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, link + KEY_BACHELOR_FULL_TIME);
        intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, degree);
        Log.d("My", "KEY_SPECIALITIES_TITLE, timeName); -> " + timeName);
        Log.d("My", "KEY_SPECIALITIES_LINK, link); -> " + link);
        Log.d("My", "KEY_DEGREE, degree -> " + degree);
        return intent;
    }

    public class ParseTimeForm extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String html = mStrFullTimeLink;
            Log.d("My", "ParseTimeForm -> mStrFullTimeLink -> " + mStrFullTimeLink);
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<String> linkList = new ArrayList<>();
            ArrayList<String> linkList666 = new ArrayList<>();
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Elements elements666 = document.getElementsByClass("accordion-heading togglize");
                Elements elements666777 = elements666.select("a");
                for (Element element666: elements666777){
                    linkList666.add(element666.text());
                    linkList666.add(element666.attr("abs:href"));
                }



                Elements elements1 = document.getElementsByClass("nav nav-tabs");
                Elements elements = elements1.select("a");
//                Element elements = document.getElementById("myTab");
//                Elements elements1 = elements.getElementsByTag("a");

                for (Element element : elements){

//                    Elements elements1 = element.select("a");
                    strings.add(element.text());
                    linkList.add(element.attr("abs:href"));
                }
                Log.d("My", "ParseTimeForm -> strings -> " + strings);
                Log.d("My", "ParseTimeForm -> linkList666 -> " + linkList666);
                Log.d("My", "ParseTimeForm -> linkList -> " + linkList);
                Log.d("My", "ParseTimeForm -> strings -> " + strings.size());
                Log.d("My", "ParseTimeForm -> linkList -> " + linkList.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            mButtonBachelor.setText(s2);
//            mListView.setAdapter(mAdapter);
//            progDailog.dismiss();
//            mListView.setAdapter(mSpecialtiesInfoAdapter);
        }
    }
}
