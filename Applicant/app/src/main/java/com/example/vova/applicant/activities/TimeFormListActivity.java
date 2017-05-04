package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vova.applicant.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TimeFormListActivity extends AppCompatActivity {

    public static final String KEY_DEGREE_TITLE = "KEY_DEGREE_TITLE";
    public static final String KEY_DEGREE_LINK = "KEY_DEGREE_LINK";
    public static final String KEY_TIME_FORM = "KEY_TIME_FORM";

    public static final int INT_FULL_TIME_FORM = 1;
    public static final int INT_EXTERNAL_FORM = 2;
    public static final int INT_DISTANCE_FORM = 3;

    private static final String KEY_BACHELOR_FULL_TIME = "d_o_1";
    private static final String KEY_SPECIALIST_FULL_TIME = "d_o_3";
    private static final String KEY_MASTER_FULL_TIME = "d_o_2";
    private static final String KEY_JUNIOR_SPECIALIST_FULL_TIME = "d_o_4";

    private static final String KEY_BACHELOR_EXTERNAL = "z_o_1";
    private static final String KEY_SPECIALIST_EXTERNAL = "z_o_3";
    private static final String KEY_MASTER_EXTERNAL = "z_o_2";
    private static final String KEY_JUNIOR_SPECIALIST_EXTERNAL = "z_o_4";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mTimeFormArray = new ArrayList<>();
    private ArrayList<String> mTimeFormlLinks = new ArrayList<>();

    private TextView mTextViewHeadText;

    private String mStrFullTimeName;
    private String mStrFullTimeLink;
    private int mIntTimeForm = 0;

//    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("OnCreate", "UniversityPageActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mStrFullTimeName = bundle.getString(KEY_DEGREE_TITLE);
                mStrFullTimeLink = bundle.getString(KEY_DEGREE_LINK);
                mIntTimeForm = bundle.getInt(KEY_TIME_FORM);
            }
        }

        mListView = (ListView) findViewById(R.id.listViewUniversal);
        mTextViewHeadText = (TextView) findViewById(R.id.textViewHeadUniversalListView);
        mTextViewHeadText.setText(mStrFullTimeName);
        Log.d("My", "onCreate   mStrFullTimeName ->" + mStrFullTimeName);

        ParseTimeForm parseTimeForm = new ParseTimeForm();
        parseTimeForm.execute();
        mAdapter = new ArrayAdapter<>(TimeFormListActivity.this, android.R.layout.simple_list_item_1,
                mTimeFormArray);
        Log.d("My", "onCreate   mCitiesArray ->" + mTimeFormArray);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(TimeFormListActivity.this, SpecialtiesListActivity.class);
                intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, mTimeFormlLinks.get(position));
                startActivity(intent);
                Log.d("My", "position = " + position + " id = " + id);
                Log.d("My", "mTimeFormlLinks.get((int) id) = " +  mTimeFormlLinks.get(position));


//                Intent intent = null;
//                String errorMsg = getString(R.string.error);
//                switch (position){
//                    case 0:
//                        intent = createIntent(mStrFullTimeName, mStrFullTimeLink, 5);
//                        if (mIntTimeForm == INT_FULL_TIME_FORM) {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_BACHELOR_FULL_TIME);
//                        } else {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_BACHELOR_EXTERNAL);
//                        }
//                        break;
//                    case 1:
//                        intent = createIntent(mStrFullTimeName, mStrFullTimeLink, 6);
//                        if (mIntTimeForm == INT_FULL_TIME_FORM) {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_SPECIALIST_FULL_TIME);
//                        } else {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_SPECIALIST_EXTERNAL);
//                        }
//                        break;
//                    case 2:
//                        intent = createIntent(mStrFullTimeName, mStrFullTimeLink, 7);
//                        if (mIntTimeForm == INT_FULL_TIME_FORM) {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_MASTER_FULL_TIME);
//                        } else {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_MASTER_EXTERNAL);
//                        }
//                        break;
//                    case 3:
//                        intent = createIntent(mStrFullTimeName, mStrFullTimeLink, 8);
//                        if (mIntTimeForm == INT_FULL_TIME_FORM) {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_JUNIOR_SPECIALIST_FULL_TIME);
//                        } else {
//                            intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_CATEGORY, KEY_JUNIOR_SPECIALIST_EXTERNAL);
//                        }
//                        break;
//
//                }
//                if (intent != null){
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(TimeFormListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
//                }
//                startActivity(intent);
            }
        });
    }

//    @NonNull
//    public Intent createIntent(String timeName, String link, int degree) {
//        intent = new Intent(this, SpecialtiesListActivity.class);
//        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_TITLE, timeName);
//        intent.putExtra(SpecialtiesListActivity.KEY_SPECIALITIES_LINK, link + KEY_BACHELOR_FULL_TIME);
//        intent.putExtra(SpecialtiesListActivity.KEY_DEGREE, degree);
//        Log.d("My", "KEY_SPECIALITIES_TITLE, timeName); -> " + timeName);
//        Log.d("My", "KEY_SPECIALITIES_LINK, link); -> " + link);
//        Log.d("My", "KEY_DEGREE, degree -> " + degree);
//        return intent;
//    }

    public class ParseTimeForm extends AsyncTask<Void, Void, Void> {

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

                Log.d("My", "ParseTimeForm -> mTimeFormArray -> " + mTimeFormArray);
                Log.d("My", "ParseTimeForm -> linkList -> " + mTimeFormlLinks);
                Log.d("My", "ParseTimeForm -> mTimeFormArray -> " + mTimeFormArray.size());
                Log.d("My", "ParseTimeForm -> linkList -> " + mTimeFormlLinks.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void loopElementsParse(Elements elements) {
            for (Element element : elements) {
                mTimeFormArray.add(element.select("a").text());
                mTimeFormlLinks.add(element.select("a").attr("abs:href"));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDailog.dismiss();
            mListView.setAdapter(mAdapter);
        }
    }
}
