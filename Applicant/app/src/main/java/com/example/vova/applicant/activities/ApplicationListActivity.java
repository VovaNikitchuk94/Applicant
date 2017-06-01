package com.example.vova.applicant.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.Utils;
import com.example.vova.applicant.adapters.ApplicationAdapter;
import com.example.vova.applicant.adapters.LegendAdapter;
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

public class ApplicationListActivity extends BaseActivity implements ApplicationAdapter.OnClickApplicationItem {

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private static final long LEGEND_YEAR_ID_2015 = 2015;
    private static final long LEGEND_YEAR_ID_2016 = 2016;
    private static final long LEGEND_YEAR_ID_2017 = 2017;

    private static final int ITEM_ID_APPLICANT_INFO = 112;
    private static final int MENU_ITEM_LEGEND = 113;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private BottomSheetBehavior bottomSheetBehavior;

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

        //modified toolbar
        Drawable menuRightIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_more_vert_black_24dp);
        menuRightIconDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getToolbar().setOverflowIcon(menuRightIconDrawable);

        setData();

        //set recyclerView with legend data
        setLegendList();
    }

    private void setLegendList() {
        Log.d("My", "setLegendList start - >" + true);
        RecyclerView legendRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewLegendInfoListActivity);
        LinearLayoutManager legendLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        legendRecyclerView.setLayoutManager(legendLayoutManager);
        DividerItemDecoration legendDividerItemDecoration = new DividerItemDecoration(legendRecyclerView.getContext(),
                legendLayoutManager.getOrientation());
        legendRecyclerView.addItemDecoration(legendDividerItemDecoration);

        LegendEngine legendEngine = new LegendEngine(getApplication());
//        mLongSpecialityId = mSpecialtiesInfo.getId();
        ArrayList<LegendInfo> legendInfos = legendEngine.getLegendsById(LEGEND_YEAR_ID_2016);
        Log.d("My", "setLegendList start legendInfos size- >" + legendInfos.size());
        LegendAdapter legendAdapter = new LegendAdapter(legendInfos);
        legendRecyclerView.setAdapter(legendAdapter);

        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // настройка поведения нижнего экрана
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // настройка максимальной высоты
//        bottomSheetBehavior.setPeekHeight(340);
//        bottomSheetBehavior.


        // настройка возможности скрыть элемент при свайпе вниз
        bottomSheetBehavior.setHideable(true);
        Log.d("My", "setLegendList  bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());

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

        MenuItem itemHelp = menu.add(3, MENU_ITEM_LEGEND,3, R.string.textLegend);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ITEM_ID_APPLICANT_INFO:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://drive.google.com/open?id=0B4__5KtwLylAazV3TEtmWmNYMjQ"));
//                startActivity(browserIntent);

                Toast.makeText(this, "applicant info selected", Toast.LENGTH_SHORT).show();
                break;
            case MENU_ITEM_LEGEND:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " if bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.d("My", " else bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                }

                Log.d("My", " bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
                Toast.makeText(this, "MENU_ITEM_LEGEND selected", Toast.LENGTH_SHORT).show();
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
            final String getStyle = "*[style*='background']";

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

//                    if (legendEngine.getLegendsById(LEGEND_YEAR_ID_2015) == null){
//                        parseLegendData(selectLegendElements, legendEngine, LEGEND_YEAR_ID_2015);
//                    } else
//                        if (legendEngine.getLegendsById(LEGEND_YEAR_ID_2016).isEmpty()) {
                        parseLegendData(selectLegendElements, legendEngine, LEGEND_YEAR_ID_2016);
//                    }

                    if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()){
                        for (Element link : selectTr) {
                            Elements tdElements = link.select("td");

                            getBackgroundApplicant(tdElements);

                            number = tdElements.get(0).text();
                            name = tdElements.get(1).text();
                            score = tdElements.get(3).text();

                            someLink = tdElements.attr("abs:href");

                            applicantInfo = link.text();

                            applicationInfoEngine.addApplication(new ApplicationsInfo(mLongSpecialityId, university,
                                    speciality, applicantInfo, number, name, score, someLink, color));
                        }
                    } else {

                        for (Element link : selectTr) {
                            Elements tdElements = link.select("td");

                            getBackgroundApplicant(tdElements);
//                        String[] colors = getBackgroundApplicant(tdElements);

                            number = tdElements.get(0).text();
                            name = tdElements.get(1).text();
                            score = tdElements.get(3).text();

                            someLink = tdElements.attr("abs:href");
                            applicantInfo = link.text();

                            applicationInfoEngine.updateApplicant(new ApplicationsInfo(mLongSpecialityId, university,
                                    speciality, applicantInfo, number, name, score, someLink, color));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO get all backgrounds
            private void getBackgroundApplicant(Elements tdElements) {

                String backgroundFirst;
                String backgroundSecond;


                Log.d("My", "tdElements.size() -> " + tdElements.size());
                if ((tdElements.get(0).select(getStyle).size()) > 0) {

                    backgroundFirst = tdElements.get(0).select(getStyle).toString();
                    backgroundSecond = tdElements.get(1).select(getStyle).toString();

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

            //get legend data
            private void parseLegendData(Elements selectLegendElements, LegendEngine legendEngine, long yearId) {
                for (Element legend: selectLegendElements){
                    Elements detailLegend = legend.select("td");
                    String legendName = "";
                    String legendDetail = "";
                    String legendBackgrond = "";

                    if ((detailLegend.get(0).select(getStyle).size()) > 0) {
                        String style = detailLegend.get(0).select(getStyle).toString();

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
                        legendDetail = detailLegend.get(1).text().trim();
                    }
                    legendEngine.addLegend(new LegendInfo(yearId, legendName, legendDetail, legendBackgrond));
                    Log.d("My", "legendEngine new LegendInfo -> " + yearId + "\n" + legendName);
                }
            }

        }).start();
    }
}
