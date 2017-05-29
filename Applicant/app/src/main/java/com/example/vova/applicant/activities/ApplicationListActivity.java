package com.example.vova.applicant.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.ApplicationAdapter;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.engines.ApplicationInfoEngine;
import com.example.vova.applicant.model.engines.LegendEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ApplicationListActivity extends BaseActivity implements ApplicationAdapter.OnClickApplicationItem{

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private static final long LEGEND_YEAR_ID_2015 = 2015;
    private static final long LEGEND_YEAR_ID_2016 = 2016;
    private static final long LEGEND_YEAR_ID_2017 = 2017;

    private static final int ITEM_ID_APPLICANT_INFO = 112;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    private SpecialtiesInfo mSpecialtiesInfo;

    private long mLongSpecialityId = 0;

    @Override
    protected void iniActivity() {


        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSpecialtiesInfo = (SpecialtiesInfo) bundle.get(INTENT_KEY_APPLICANT_ACTIVITY);
            }
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_applicant_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                parseData();
                Log.d("My","SwipeRefreshLayout -> parseData -> is start");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewApplicationListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applcation_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set applicantItem icon
        MenuItem itemApplicantInfo = menu.add(0, ITEM_ID_APPLICANT_INFO, 0, R.string.textInfoForApplicant);
        Drawable infoDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_black_24dp);
        infoDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        itemApplicantInfo.setIcon(infoDrawable);
        itemApplicantInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ITEM_ID_APPLICANT_INFO:
                Toast.makeText(this, "applicant info selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        mLongSpecialityId = mSpecialtiesInfo.getId();
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
           parseData();
        } else {
            getData(applicationInfoEngine);
        }
    }

    private void getData(ApplicationInfoEngine applicationInfoEngine) {
        ArrayList<ApplicationsInfo> applicationsInfos = applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId);
        ApplicationAdapter adapter = new ApplicationAdapter(applicationsInfos);
        adapter.setOnClickApplicationItem(ApplicationListActivity.this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickApplicationItem(ApplicationsInfo applicationInfo) {
        Intent intent = new Intent(this, DetailApplicantPagerActivity.class);
        intent.putExtra(DetailApplicantPagerActivity.INTENT_KEY_APPLICANT_INFO, applicationInfo);
        startActivity(intent);
    }

    private void parseData() {

        new Thread(new Runnable() {

            final ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
            String color;

            @Override
            public void run() {

                if (Utils.connectToData(mSpecialtiesInfo.getStrLink()) && mLongSpecialityId != 0) {
                    parse(applicationInfoEngine);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        getData(applicationInfoEngine);
                    }
                });
            }

            private void parse(ApplicationInfoEngine applicationInfoEngine) {
                String html;
                String university;
                String speciality;
                String applicantInfo;
                String number;
                String name;
                String score;
                String someLink;

                Document document;
                try {
                    html = mSpecialtiesInfo.getStrLink();
                    document = Jsoup.connect(html).get();
                    Log.d("My", "html -------> " + html);

                    //Data of applicants
                    Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                    Elements elements = links.select("tbody");
                    Elements selectTr = elements.select("tr");

                    //Data of University information
                    Elements detailElements = document.getElementsByClass("title-page");
                    university = detailElements.select(".title-description").text();
                    speciality = detailElements.select("p").text();

                    //Data of legend
                    Element legendElementById = document.getElementById("legend");
                    Elements selectLegendElements = legendElementById.select("tr");
                    LegendEngine legendEngine = new LegendEngine(getApplication());

                    if (legendEngine.getLegendById(LEGEND_YEAR_ID_2015) == null){
                        parseLegendData(selectLegendElements, legendEngine, LEGEND_YEAR_ID_2015);
                    } else if (legendEngine.getLegendById(LEGEND_YEAR_ID_2016) == null) {
                        parseLegendData(selectLegendElements, legendEngine, LEGEND_YEAR_ID_2016);
                    }

                    if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
                        for (Element link : selectTr) {
                            Elements tds = link.select("td");

//                        Log.d("My", "color 3->" + color);
                            getBackgroundApplicant(tds);
//                        Log.d("My", "color 4->" + color);
//                        String[] colors = getBackgroundApplicant(tds);

                            number = tds.get(0).text();
                            name = tds.get(1).text();
                            score = tds.get(3).text();

                            someLink = tds.attr("abs:href");

                            applicantInfo = link.text();

                            applicationInfoEngine.addApplication(new ApplicationsInfo(mLongSpecialityId, university,
                                    speciality, applicantInfo, number, name, score, someLink, color));
                        }
                    } else {
                        for (Element link : selectTr) {
                            Elements tds = link.select("td");

                            getBackgroundApplicant(tds);
//                        String[] colors = getBackgroundApplicant(tds);

                            number = tds.get(0).text();
                            name = tds.get(1).text();
                            score = tds.get(3).text();

                            someLink = tds.attr("abs:href");
                            applicantInfo = link.text();

                            applicationInfoEngine.updateApplicant(new ApplicationsInfo(mLongSpecialityId, university,
                                    speciality, applicantInfo, number, name, score, someLink, color));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void getBackgroundApplicant(Elements tds) {

                String backgroundFirst;
                String backgroundSecond;
                String getStyle = "*[style*='background']";

                Log.d("My", "tds.size() -> " + tds.size());
                if ((tds.get(0).select(getStyle).size()) > 0) {

                    backgroundFirst = tds.get(0).select(getStyle).toString();
                    backgroundSecond = tds.get(1).select(getStyle).toString();

                    backgroundFirst = backgroundFirst.substring(backgroundFirst.indexOf("#"), backgroundFirst.indexOf(">") - 1);
                    backgroundSecond = backgroundSecond.substring(backgroundSecond.indexOf("#"), backgroundSecond.indexOf(">") - 1);

                    if (!backgroundFirst.equals(backgroundSecond)){
                        color = "#FFFF99";
                    } else {
                        color = backgroundFirst;
                    }
                } else {
                    color = "#FFFFFF";
                }
            }

            private void parseLegendData(Elements selectLegendElements, LegendEngine legendEngine, long yearId) {
                for (Element legend: selectLegendElements){
                    Elements detailLegend = legend.select("td");
                    String legendName = "";
                    String legendDetail = "";
                    String legendBackgrond = "";

                    if ((detailLegend.get(0).select("*[style*='background']").size()) > 0) {
                        String style = detailLegend.get(0).select("*[style*='background']").toString();

                        if (style.length() > 132) {
                            String yelowColor = style.substring(style.indexOf("#", 133));
                            legendBackgrond = yelowColor.substring(0, yelowColor.indexOf(";"));
                        } else {
                            legendBackgrond = style.substring(style.indexOf("#"), style.indexOf(";"));
                        }

                    } else {
                        legendName = detailLegend.get(0).text();
                    }

                    if (detailLegend.size() > 1) {
                        legendDetail = detailLegend.get(1).text();
                    }
                    legendEngine.addLegend(new LegendInfo(yearId, legendName, legendDetail, legendBackgrond));
                }
            }

        }).start();
    }
}
