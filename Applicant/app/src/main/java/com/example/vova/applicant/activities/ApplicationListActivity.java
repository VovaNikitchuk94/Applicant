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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.ApplicationAdapter;
import com.example.vova.applicant.adapters.LegendAdapter;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.engines.ApplicationInfoEngine;
import com.example.vova.applicant.model.engines.ImportantApplicantInfoEngine;
import com.example.vova.applicant.model.engines.LegendEngine;
import com.example.vova.applicant.utils.Utils;

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

    private TextView numberTextView, nameTextView, competitionScoreTextView, BDOScoreTextView, ZNOScoreTextView;

    private SpecialtiesInfo mSpecialtiesInfo;

    private long mLongSpecialityId = 0;

//    ImportantApplicantInfoEngine importantApplicantInfoEngine;

    @Override
    protected void iniActivity() {

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSpecialtiesInfo = (SpecialtiesInfo) bundle.get(INTENT_KEY_APPLICANT_ACTIVITY);
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_applicant_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                parseData();
                Log.d("My", "SwipeRefreshLayout -> parseData -> is start");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //get textView from important info
        numberTextView = (TextView) findViewById(R.id.textViewLegendSequenceNumberApplicantInfo);
        nameTextView = (TextView) findViewById(R.id.textViewLegendNameApplicantsApplicantInfo);
        competitionScoreTextView = (TextView) findViewById(R.id.textViewLegendCompetitionScoresApplicantInfo);
        BDOScoreTextView = (TextView) findViewById(R.id.textViewLegendBDOScoreApplicantsApplicantInfo);
        ZNOScoreTextView = (TextView) findViewById(R.id.textViewLegendZNOScoreApplicantsApplicantInfo);

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

    private void setTextImportantTextView() {
        ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getApplicationContext());
            Log.d("My", "setTextImportantTextView importantApplicantInfoEngine.getImportantInfoById(mLongSpecialityId) - > "
                    + importantApplicantInfoEngine.getImportantInfoById(mSpecialtiesInfo.getId()));
            Log.d("My", "setTextImportantTextView start");
            ImportantInfo importantInfos = importantApplicantInfoEngine.getImportantInfoById(mSpecialtiesInfo.getId());
            Log.d("My", "setTextImportantTextView start importantInfos - > " + importantInfos);

            numberTextView.setText(importantInfos.getStrNumber());
            nameTextView.setText(importantInfos.getStrName());
            competitionScoreTextView.setText(importantInfos.getStrTotalScores());
            BDOScoreTextView.setText(importantInfos.getStrMarkDocument());
            ZNOScoreTextView.setText(importantInfos.getStrMarkTest());
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

        LegendEngine legendEngine = new LegendEngine(getApplication());
//        mLongSpecialityId = mSpecialtiesInfo.getId();
        ArrayList<LegendInfo> legendInfos = legendEngine.getLegendsById(LEGEND_YEAR_ID_2016);
        Log.d("My", "setLegendList start legendInfos size- >" + legendInfos.size());
        LegendAdapter legendAdapter = new LegendAdapter(legendInfos);
        legendRecyclerView.setAdapter(legendAdapter);
//        legendAdapter.notifyDataSetChanged();

        Log.d("My", "setLegendList  bottomSheetBehavior.setState - >" + bottomSheetBehavior.getState());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applcation_list;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //set applicantItem icon
//        MenuItem itemApplicantInfo = menu.add(0, ITEM_ID_APPLICANT_INFO, 0, R.string.textInfoForApplicant);
//        Drawable infoDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_black_24dp);
//        infoDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        itemApplicantInfo.setIcon(infoDrawable);
//        itemApplicantInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//        MenuItem itemHelp = menu.add(3, MENU_ITEM_LEGEND,3, R.string.textLegend);
//        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case ITEM_ID_APPLICANT_INFO:
////                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
////                        Uri.parse("https://drive.google.com/open?id=0B4__5KtwLylAazV3TEtmWmNYMjQ"));
////                startActivity(browserIntent);
//
//                Toast.makeText(this, "applicant info selected", Toast.LENGTH_SHORT).show();
//                break;
//            case MENU_ITEM_LEGEND:
//                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    Log.d("My", " if bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
//                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    Log.d("My", " else bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
//                }
//
//                Log.d("My", " bottomSheetBehavior.setState - >" +  bottomSheetBehavior.getState());
//                Toast.makeText(this, "MENU_ITEM_LEGEND selected", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setData() {
        mLongSpecialityId = mSpecialtiesInfo.getId();
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()) {
            parseData();
        } else {
            getData(applicationInfoEngine);

            //set textViews text importantInfo
            setTextImportantTextView();
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

                        //set textViews text importantInfo
                        setTextImportantTextView();
                    }
                });
            }

            private void parse(ApplicationInfoEngine applicationInfoEngine) {
                String html;
                String applicantInfo;

                //applicant data
                String number;
                String name;
                String priority = "";
                String totalScore;
                String markDocument;
                String markTest;
                String markExam;
                String extraPoints;
                String originalDocument;

                String someLink;

                Document document;
                try {
                    html = mSpecialtiesInfo.getStrLink();
                    document = Jsoup.connect(html).get();
                    Log.d("My", "html -------> " + html);

                    //TODO parse the date in SQLite
                    //get time and date update page
                    String strLastUpdatePage = document.select("div.title-page > p > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String date = arrayTimeDate[3];
                    String time = arrayTimeDate[5];
                    Log.d("My", "date -> " + date + "\ntime -> " + time);

                    //Data of applicants
                    Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                    Elements elementsBody = links.select("tbody");
                    Elements selectTr = elementsBody.select("tr");

                    //Data of legend
                    Element legendElementById = document.getElementById("legend");
                    Elements selectLegendElements = legendElementById.select("tr");
                    LegendEngine legendEngine = new LegendEngine(getApplication());
                    if (legendEngine.getLegendsById(LEGEND_YEAR_ID_2016).isEmpty()) {
                        parseLegendData(selectLegendElements, legendEngine, LEGEND_YEAR_ID_2016);
                    }

                    //Data of importantInfo & Data of University information
                    Elements detailElements = document.getElementsByClass("title-page");
                    Elements elementsThead = links.select("thead");
                    Elements selectTrMarking = elementsThead.select("th");
                    Log.d("My", "selectTrMarking ->" + selectTrMarking);
                    parseImportantInfo(detailElements, selectTrMarking);

                    if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()) {
                        for (Element link : selectTr) {
                            Elements tdElements = link.select("td");

                            //background
                            getBackgroundApplicant(tdElements);

                            Log.d("My", "tdElements.size(); - > " + tdElements.size());
                            if (tdElements.size() == 8) {
                                number = tdElements.get(0).text();
                                name = tdElements.get(1).text();
                                totalScore = tdElements.get(2).text();
                                markDocument = tdElements.get(3).text();
                                markTest = tdElements.get(4).text();
                                markExam = tdElements.get(5).text();
                                extraPoints = tdElements.get(6).text();
                                originalDocument = tdElements.get(7).text();
                            } else {
                                number = tdElements.get(0).text();
                                name = tdElements.get(1).text();
                                priority = tdElements.get(2).text();
                                totalScore = tdElements.get(3).text();
                                markDocument = tdElements.get(4).text();
                                markTest = tdElements.get(5).text();
                                markExam = tdElements.get(6).text();
                                extraPoints = tdElements.get(7).text();
                                originalDocument = tdElements.get(8).text();
                            }

                            Log.d("My", "number + name + priority + totalScore + markDocument +\n" +
                                    "markTest + markExam + extraPoints + originalDocument" +
                                    number + name + priority + totalScore + markDocument +
                                    markTest + markExam + extraPoints + originalDocument);

                            someLink = tdElements.attr("abs:href");

                            applicantInfo = link.text();

                            applicationInfoEngine.addApplication(new ApplicationsInfo(mLongSpecialityId,
                                    applicantInfo, number, name, priority, totalScore, markDocument,
                                    markTest, markExam, extraPoints, originalDocument, someLink, color));
                        }
                    } else {

                        for (Element link : selectTr) {
                            Elements tdElements = link.select("td");

                            getBackgroundApplicant(tdElements);

                            if (tdElements.size() == 8) {
                                number = tdElements.get(0).text();
                                name = tdElements.get(1).text();
                                totalScore = tdElements.get(2).text();
                                markDocument = tdElements.get(3).text();
                                markTest = tdElements.get(4).text();
                                markExam = tdElements.get(5).text();
                                extraPoints = tdElements.get(6).text();
                                originalDocument = tdElements.get(7).text();
                            } else {
                                number = tdElements.get(0).text();
                                name = tdElements.get(1).text();
                                priority = tdElements.get(2).text();
                                totalScore = tdElements.get(3).text();
                                markDocument = tdElements.get(4).text();
                                markTest = tdElements.get(5).text();
                                markExam = tdElements.get(6).text();
                                extraPoints = tdElements.get(7).text();
                                originalDocument = tdElements.get(8).text();
                            }

                            someLink = tdElements.attr("abs:href");
                            applicantInfo = link.text();

                            applicationInfoEngine.updateApplicant(new ApplicationsInfo(mLongSpecialityId,
                                   applicantInfo, number, name, priority, totalScore, markDocument,
                                    markTest, markExam, extraPoints, originalDocument, someLink, color));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void parseImportantInfo(Elements detailElements, Elements selectTrMarking) {
                String universityName = null;
                String speciality = null;
                String specialization = null;
                String faculty = null;
                String timeForm = null;
                String lastTimeUpdate = null;

                String number = null;
                String name = null;
                String priority = null;
                String totalScore = null;
                String markDocument = null;
                String markTest = null;
                String markExam = null;
                String extraPoints = null;
                String originalDocument = null;

                ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getApplicationContext());
                Log.d("My", "parseImportantInfo start -> " + importantApplicantInfoEngine.getImportantInfoById(mLongSpecialityId));
                if (importantApplicantInfoEngine.getImportantInfoById(mLongSpecialityId) == null) {

                    universityName = detailElements.select(".title-description").text();
                    String universityInfos = null;
                    if (detailElements.select("p").size() > 1) {
                         universityInfos = (detailElements.select("p").get(1).toString()).replaceAll("(?i)<br[^>]*>", "/");
                    } else {
                        universityInfos = (detailElements.select("p").get(0).toString()).replaceAll("(?i)<br[^>]*>", "/");
                    }

                    universityInfos = universityInfos.substring(4, universityInfos.indexOf("<small"));
                    Log.d("My", "detailElements speciality -> " + universityInfos);
                    String[] arrSpecialities = universityInfos.split("[/]");
                    Log.d("My", "detailElements arrSpecialities.length -> " + arrSpecialities.length);
                    if (arrSpecialities.length == 4) {
                        speciality = arrSpecialities[0];
                        specialization = arrSpecialities[1];
                        faculty = arrSpecialities[2];
                        timeForm = arrSpecialities[3];
                    }

                    lastTimeUpdate = detailElements.select("p > small").text();
                    Log.d("My", "detailElements lastTimeUpdate -> " + lastTimeUpdate);

                    Log.d("My", "parseImportantInfo start");

                    //applicant data
                    if (selectTrMarking.size() == 8) {
                        Log.d("My", "parseImportantInfo start selectTrMarking.size() == 8");
                        number = selectTrMarking.get(0).text();
                        name = selectTrMarking.get(1).text();
                        totalScore = selectTrMarking.get(2).text();
                        markDocument = selectTrMarking.get(3).text();
                        markTest = selectTrMarking.get(4).text();
                        markExam = selectTrMarking.get(5).text();
                        extraPoints = selectTrMarking.get(6).text();
                        originalDocument = selectTrMarking.get(7).text();

                    } else {
                        Log.d("My", "parseImportantInfo start selectTrMarking.size() == 9");
                        number = selectTrMarking.get(0).text();
                        name = selectTrMarking.get(1).text();
                        priority = selectTrMarking.get(2).text();
                        totalScore = selectTrMarking.get(3).text();
                        markDocument = selectTrMarking.get(4).text();
                        markTest = selectTrMarking.get(5).text();
                        markExam = selectTrMarking.get(6).text();
                        extraPoints = selectTrMarking.get(7).text();
                        originalDocument = selectTrMarking.get(8).text();
                    }

                    importantApplicantInfoEngine.addImportantInfo(new ImportantInfo(mLongSpecialityId, universityName,
                            speciality, specialization, faculty, timeForm, lastTimeUpdate, number, name, priority, totalScore,
                            markDocument, markTest, markExam, extraPoints, originalDocument));

                    Log.d("My", "parseImportantInfo; universityName ->" + universityName);
                    Log.d("My", "parseImportantInfo; speciality ->" + speciality);
                    Log.d("My", "parseImportantInfo; specialization ->" + specialization);
                    Log.d("My", "parseImportantInfo; faculty ->" + faculty);
                    Log.d("My", "parseImportantInfo; timeForm ->" + timeForm);
                    Log.d("My", "parseImportantInfo; name ->" + name);

                }
            }

            //get background color applicant item
            //TODO get all backgrounds обработать все возможные трехзначные цвета
            //TODO даже при том что я вітаскиваю все цвета нужно правильно их ковертировать нужные мне цвета
            private void getBackgroundApplicant(Elements tdElements) {

                String backgroundFirst;
                String backgroundSecond;

                Log.d("My", "tdElements.size() -> " + tdElements.size());
                if ((tdElements.get(0).select(getStyle).size()) > 0) {

                    backgroundFirst = tdElements.get(0).select(getStyle).toString();
                    backgroundSecond = tdElements.get(1).select(getStyle).toString();

                    backgroundFirst = backgroundFirst.substring(backgroundFirst.indexOf("#"), backgroundFirst.indexOf(">") - 1);
                    backgroundSecond = backgroundSecond.substring(backgroundSecond.indexOf("#"), backgroundSecond.indexOf(">") - 1);

                    if (!backgroundFirst.equals(backgroundSecond)) {
                        color = "#FFFF99";
                    } else {
                        color = backgroundFirst;
                    }
                } else {
                    color = "#FFFFFF";
                }
            }

            //get legend data
            //todo грузить легеннду для каждого списка абитуры
            //todo учесть что там есть текст жирным шрифтом, доделать внешний вид легенды
            private void parseLegendData(Elements selectLegendElements, LegendEngine legendEngine, long yearId) {

//                for (int i = 1; i < selectLegendElements.size(); i++ ) {
                for (Element legend : selectLegendElements) {
//                    Elements detailLegend = selectLegendElements.get(i).select("td");
                    Elements detailLegend = legend.select("td");
                    String legendName = "";
                    String legendDetail = "";
                    String legendBackground = "";


                    if ((detailLegend.get(0).select(getStyle).size()) > 0) {
                        String style = detailLegend.get(0).select(getStyle).toString();
                        if (style.length() > 132) {
                            legendBackground = "#FFFF99";
                        } else {
                            legendBackground = style.substring(style.indexOf("#"), style.indexOf(";"));
                        }
//                        Log.d("My", "legendEngine new legendBackground getStyle).size()) > 0 -> " + legendBackground);
                    } else {
                        legendBackground = "#ffffff";
                        legendName = detailLegend.get(0).text();
//                        Log.d("My", "legendEngine new legendBackground getStyle).size()) < 0 -> " + legendBackground);
                    }

                    if (detailLegend.size() > 1) {
                        legendDetail = detailLegend.get(1).text().trim();
                    }

                    if (detailLegend.size() == 1) {
                        Log.d("My", "legendEngine true -> ((");
                        legendName = "";
                        legendDetail = detailLegend.get(0).text().trim();
                    }
                    legendEngine.addLegend(new LegendInfo(yearId, legendName, legendDetail, legendBackground));
                    Log.d("My", "legendEngine new legendBackground legendName-> " + legendBackground);
                    Log.d("My", "legendEngine new legendBackground legendDetail-> " + legendDetail);
                    Log.d("My", "legendEngine new legendBackground legendBackground-> " + legendBackground);
                }
            }
        }).start();
    }
}
