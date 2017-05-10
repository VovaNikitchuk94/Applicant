package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.AboutUniversityAdapter;
import com.example.vova.applicant.model.AboutUniversityInfo;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.AboutUniversityEngine;
import com.example.vova.applicant.model.engines.DetailUniverInfoEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutUniversityActivity extends AppCompatActivity implements
        AboutUniversityAdapter.OnClickAboutUniversityItem{

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY =
            "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private DetailUniverInfo mDetailUniverInfo;

    private ArrayList<AboutUniversityInfo> mAboutUniversityInfos = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AboutUniversityAdapter mUniversityInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_university_list);

        Log.d("My", "AboutUniversityActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDetailUniverInfo = (DetailUniverInfo) bundle.get(KEY_ABOUT_UNIVERSITY_ACTIVITY);
            }
        }
        Log.d("My", "AboutUniversityActivity mAboutUniversityLink-> " + mDetailUniverInfo);

        TextView titleTextView = (TextView)findViewById(R.id.textViewHeadAboutUniversityActivity);
        titleTextView.setText(mDetailUniverInfo.getStrDetailText());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAboutUniversityActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new ParseAboutUniversityList().execute();
    }

    @Override
    public void onClickAboutUniversityItem(long nIdItem) {

        AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
        AboutUniversityInfo myPosition = aboutUniversityEngine.getAboutUniversityById(nIdItem);
        Intent intent = null;
                String dataLink = myPosition.getStrAboutUniversData();

                switch (myPosition.getStrAboutUniversType()){
                    case "Веб-сайт:":
                        if ((!dataLink.contains("http://"))){
                            dataLink = "http://" + dataLink;
                        }
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataLink));
                        break;
                    case "Адреса:":
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + dataLink);
                        intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        //open for google Maps
//                        intent.setPackage("com.google.android.apps.maps");
                        break;
                    case "Телефони:":
//                        TODO обработка нескольких номером и правильность написания номера
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataLink));
                        break;
                    case "E-mail:":
                        //TODO обработать несколько имейлов
                        intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dataLink));
                        break;
                }
                startActivity(intent);
    }

    private class ParseAboutUniversityList extends AsyncTask<String, Void, String> {

        ProgressDialog progDailog = new ProgressDialog(AboutUniversityActivity.this);
        long mLongDetailUNVId = mDetailUniverInfo.getId();

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
        protected String doInBackground(String... params) {

            AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplicationContext());

                if (aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId).isEmpty()){
                    parse(mLongDetailUNVId, aboutUniversityEngine);
                }
            return null;
        }

        private void parse(long longDetailUNVId, AboutUniversityEngine aboutUniversityEngine) {
            String html;
            html = mDetailUniverInfo.getStrDetailLink();
            Log.d("My", "ParseAboutUniversityList doInBackground  html ->" + html);
            String wrongDog = " [ at ] ";
            String correctDog = "@";
            Document document;
            try {
                document = Jsoup.connect(html).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements elements = elementAboutUniversities.getElementsByTag("tr");

                //add new element to array only when text with data isn't empty
                for (Element link : elements){
                    String type = link.select("td").first().text();
                    String data = link.select("td").last().text();

                    if (!data.isEmpty()){
                        data = data.replace(wrongDog, correctDog);
                        aboutUniversityEngine.addAboutUniversity(new AboutUniversityInfo(longDetailUNVId, type, data));
                        Log.d("My", "doInBackground   link.select(\"td\").first().text() ->" + link.select("td").first().text());
                        Log.d("My", "doInBackground    link.select(\"td\").last().text()) ->" +  link.select("td").last().text());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String srt) {
            AboutUniversityEngine aboutUniversityEngine = new AboutUniversityEngine(getApplication());
            mAboutUniversityInfos = aboutUniversityEngine.getAboutAllUnivesitiesById(mLongDetailUNVId);
            mUniversityInfoAdapter = new AboutUniversityAdapter(mAboutUniversityInfos);
            mUniversityInfoAdapter.setOnClickListenerAdapter(AboutUniversityActivity.this);
            mRecyclerView.setAdapter(mUniversityInfoAdapter);
            progDailog.dismiss();
        }
    }
}
