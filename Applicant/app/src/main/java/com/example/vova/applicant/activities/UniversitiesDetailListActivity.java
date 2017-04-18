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
import android.widget.Toast;

import com.example.vova.applicant.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UniversitiesDetailListActivity extends AppCompatActivity {

    public static final String KEY_UNIVERSITY_TITLE = "KEY_UNIVERSITY_TITLE";
    public static final String KEY_UNIVERSITY_LINK = "KEY_UNIVERSITY_LINK";

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mUniversitiesDetailArray = new ArrayList<>();
    private ArrayList<String> mUniversitiesDetailLinks = new ArrayList<>();

    private TextView mTextViewHeadText;

    private String universityName;
    private String universityDetailLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_list_view);

        Log.d("OnCreate", "UniversityPageActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){

                universityName = bundle.getString(KEY_UNIVERSITY_TITLE);
                universityDetailLink = bundle.getString(KEY_UNIVERSITY_LINK);
            }
        }

        mListView = (ListView)findViewById(R.id.listViewUniversal);
        mTextViewHeadText = (TextView)findViewById(R.id.textViewHeadUniversalListView);
        mTextViewHeadText.setText(universityName);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = null;
                String errorMsg = getString(R.string.error);
                switch (position){
                    case 0:
                        intent = new Intent(UniversitiesDetailListActivity.this, AboutUniversityActivity.class);
                        intent.putExtra(AboutUniversityActivity.KEY_ABOUT_UNIVERSITY_ACTIVITY,
                                universityDetailLink);
                        Log.d("My", "UniversityPageActivity -> onClick -> universityDetailLink" + universityDetailLink);
                        break;
                    case 1:
                        if (mUniversitiesDetailArray.get(position).contains("(0)")){
                            errorMsg = mUniversitiesDetailArray.get(position) + getString(R.string.textMsgIsEmpty);
                        } else {
                            intent = new Intent(UniversitiesDetailListActivity.this, TimeFormListActivity.class);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                                    universityName);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                                    universityDetailLink);
                            intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                                    TimeFormListActivity.INT_FULL_TIME_FORM);
                        }
                        break;
                    case 2:
                        if (mUniversitiesDetailArray.get(position).contains("(0)")){
                            errorMsg = mUniversitiesDetailArray.get(position) + getString(R.string.textMsgIsEmpty);
                        } else {
                            intent = new Intent(UniversitiesDetailListActivity.this, TimeFormListActivity.class);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                                    universityName);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                                    universityDetailLink);
                            intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                                    TimeFormListActivity.INT_EXTERNAL_FORM);
                            Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity " +
                                    "-> universityDetailLink" + universityDetailLink);
                            Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity" +
                                    " ->universityDetailLink" + universityDetailLink);
                        }
                        break;
                    case 3:
                        if (mUniversitiesDetailArray.get(position).contains("(0)")){
                            errorMsg = mUniversitiesDetailArray.get(position) + getString(R.string.textMsgIsEmpty);
                        } else {
                            intent = new Intent(UniversitiesDetailListActivity.this, TimeFormListActivity.class);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_TITLE,
                                    universityName);
                            intent.putExtra(TimeFormListActivity.KEY_DEGREE_LINK,
                                    universityDetailLink);
                            intent.putExtra(TimeFormListActivity.KEY_TIME_FORM,
                                    TimeFormListActivity.INT_DISTANCE_FORM);
                            Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity " +
                                    "-> universityDetailLink" + universityDetailLink);
                            Log.d("My", "UniversityPageActivity -> onClick -> buttonExternalFormUniversityPageActivity" +
                                    " ->universityDetailLink" + universityDetailLink);
                        }
                        break;
                }
                if (intent != null){
                    startActivity(intent);
                }else {
                    Toast.makeText(UniversitiesDetailListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });

        new ParseUniversitiesDetail().execute();
        mAdapter = new ArrayAdapter<>(UniversitiesDetailListActivity.this, android.R.layout.simple_list_item_1,
                mUniversitiesDetailArray);
        Log.d("My", "onCreate   mCitiesArray ->" + mUniversitiesDetailArray);
    }

    public class ParseUniversitiesDetail extends AsyncTask<Void, Void, Void> {

        ProgressDialog progDailog = new ProgressDialog(UniversitiesDetailListActivity.this);

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

            Log.d("My", "ParseUniversitiesDetail -> universityDetailLink -> " + universityDetailLink);

            Document document;
            try {
                document = Jsoup.connect(universityDetailLink).get();
                Log.d("My", "ParseUniversitiesDetail -> universityDetailLink -> " + universityDetailLink);

                Elements elementsByClass = document.getElementsByClass("accordion-heading togglize");
                Elements elementsText = elementsByClass.select("a");
                for (Element element: elementsText){
                    mUniversitiesDetailArray.add(element.text());
                    mUniversitiesDetailLinks.add(element.attr("abs:href"));
                }
//                Elements elements1 = document.getElementsByClass("nav nav-tabs");
//                Elements elements = elements1.select("a");
////                Element elements = document.getElementById("myTab");
////                Elements elements1 = elements.getElementsByTag("a");
//
//                for (Element element : elements){
//
////                    Elements elements1 = element.select("a");
//                    strings.add(element.text());
//                    linkList.add(element.attr("abs:href"));
//                }
//                Log.d("My", "ParseTimeForm -> strings -> " + strings);
                Log.d("My", "ParseTimeForm -> mUniversitiesDetailArray -> " + mUniversitiesDetailArray);
                Log.d("My", "ParseTimeForm -> mUniversitiesDetailLinks -> " + mUniversitiesDetailLinks);
                Log.d("My", "ParseTimeForm -> mUniversitiesDetailArray -> " + mUniversitiesDetailArray.size());
                Log.d("My", "ParseTimeForm -> mUniversitiesDetailLinks -> " + mUniversitiesDetailLinks.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDailog.dismiss();
            mListView.setAdapter(mAdapter);
        }
    }
}
