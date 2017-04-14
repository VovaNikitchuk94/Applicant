package com.example.vova.applicant;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vova.applicant.adapters.UniversityInfoAdapter;

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

                switch (myPosition.getStrInfoType()){
                    case "Веб-сайт:":
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + myPosition.getStrInfoData()));
                        break;
                    case "Адреса:":
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + myPosition.getStrInfoData());
                        intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                        intent.setPackage("com.google.android.apps.maps");
                        break;
                    case "Телефони:":
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myPosition.getStrInfoData()));
                        break;
                    case "E-mail:":
                        intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + myPosition.getStrInfoData()));
                        break;
                }
                startActivity(intent);

            }
        });

    }

    public class ParseAboutUniversityList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String type = "";
            String data = "";
            String wrongDog = " [ at ] ";

            Document document;
            try {
                document = Jsoup.connect(mAboutUniversityLink).get();

                Element elementAboutUniversities = document.getElementById("about");
                Elements texts1 = elementAboutUniversities.getElementsByTag("tr");

                //add new element to array only when second text isn't empty
                for (Element link : texts1){
                    type = link.select("td").first().text();
                    data = link.select("td").last().text();

                    if (!data.isEmpty()){
                        data = data.replace(wrongDog, "@");
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

        }
    }
}
