package com.example.vova.applicant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversityInfoAdapter;
import com.example.vova.applicant.model.UniversityInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AboutUniversityActivity extends AppCompatActivity {

    public static final String KEY_ABOUT_UNIVERSITY_ACTIVITY =
            "KEY_ABOUT_UNIVERSITY_ACTIVITY";

    private String mAboutUniversityLink;

    private ListView mListView;

    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();
    private UniversityInfoAdapter mUniversityInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_university);

        Log.d("OnCreate", "AboutUniversityActivity -> OnCreate");

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mAboutUniversityLink = bundle.getString(KEY_ABOUT_UNIVERSITY_ACTIVITY);
            }
        }

        mListView = (ListView) findViewById(R.id.listViewAboutUniversityActivity);

        new ParseAboutUniversityList().execute();
        // TODO modified listView style

        mUniversityInfoAdapter = new UniversityInfoAdapter(AboutUniversityActivity.this,
                R.layout.list_item_university_info, mUniversityInfos);

        Log.d("My", "onCreate   link -> mAboutUniversityLink" + mAboutUniversityLink);
        Log.d("My", "onCreate   link -> mUniversityInfos" + mUniversityInfos);

        mListView.setAdapter(mUniversityInfoAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                UniversityInfo myPosition = mUniversityInfos.get(position);
                Intent intent = null;
                String dataLink = myPosition.getStrInfoData();

                switch (myPosition.getStrInfoType()){
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
        });

    }

    public class ParseAboutUniversityList extends AsyncTask<String, Void, String> {

        ProgressDialog progDailog = new ProgressDialog(AboutUniversityActivity.this);

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

            String type;
            String data;
            String wrongDog = " [ at ] ";
            String correctDog = "@";
            Document document;
            try {
                document = Jsoup.connect(mAboutUniversityLink).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements elements = elementAboutUniversities.getElementsByTag("tr");

                //add new element to array only when second text isn't empty
                for (Element link : elements){
                    type = link.select("td").first().text();
                    data = link.select("td").last().text();

                    if (!data.isEmpty()){
                        data = data.replace(wrongDog, correctDog);
                        mUniversityInfos.add(new UniversityInfo(
                                type, data));
                        Log.d("My", "doInBackground   link.select(\"td\").first().text() ->" + link.select("td").first().text());
                        Log.d("My", "doInBackground    link.select(\"td\").last().text()) ->" +  link.select("td").last().text());
                    }

                }

                Log.d("My", "doInBackground   mUniversityInfos ->" + mUniversityInfos);
                Log.d("My", "doInBackground   mUniversityInfos ->" + mUniversityInfos.size());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String srt) {
            mListView.setAdapter(mUniversityInfoAdapter);
            progDailog.dismiss();

        }
    }
}
