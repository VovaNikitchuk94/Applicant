package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ApplicationListActivity extends BaseActivity implements ApplicationAdapter.OnClickApplicationItem {

    public static final String INTENT_KEY_APPLICANT_ACTIVITY = "INTENT_KEY_APPLICANT_ACTIVITY";

    private static final int ITEM_ID_APPLICANT_INFO = 112;
    private static final int MENU_ITEM_LEGEND = 113;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView numberTextView, nameTextView, competitionScoreTextView, BDOScoreTextView, ZNOScoreTextView;
    private SearchView mSearchView = null;

    private SpecialtiesInfo mSpecialtiesInfo;
    private ArrayList<ApplicationsInfo> mApplicationsInfos = new ArrayList<>();
    private ApplicationAdapter mApplicationAdapter;
    private Calendar mCalendar;

    private long mLongSpecialityId = -1;
    private String mStrApplicantCodeLink = "";

    @Override
    protected void initActivity() {

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSpecialtiesInfo = (SpecialtiesInfo) bundle.get(INTENT_KEY_APPLICANT_ACTIVITY);
                if (mSpecialtiesInfo != null) {
                    mLongSpecialityId = mSpecialtiesInfo.getId();
                    mStrApplicantCodeLink = mSpecialtiesInfo.getStrLink();
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //TODO при обновлении и последующем открытии активи пропадает данные и ImportantInfo
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_applicant_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(DBConstants.Update.NEED_AN_UPDATE);
                }
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
    }

    private void setTextImportantTextView() {
        ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getApplicationContext());
        ImportantInfo importantInfos = importantApplicantInfoEngine.getImportantInfoById(mSpecialtiesInfo.getId());
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
        bottomSheetBehavior.setHideable(true);

        LegendEngine legendEngine = new LegendEngine(getApplication());
        ArrayList<LegendInfo> legendInfos = legendEngine.getLegendsById(mLongSpecialityId);
        Log.d("My", "setLegendList start legendInfos size- >" + legendInfos.size());
        LegendAdapter legendAdapter = new LegendAdapter(legendInfos);
        legendRecyclerView.setAdapter(legendAdapter);

        Log.d("My", "setLegendList  bottomSheetBehavior.setState - >" + bottomSheetBehavior.getState());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applcation_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        setToolbarSearchView(menu);

        MenuItem itemHelp = menu.add(3, MENU_ITEM_LEGEND,3, R.string.textLegend);
        itemHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    private void setToolbarSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //customized mSearchView from stackOverflow help
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.primary_light));
        searchAutoComplete.setTextColor(Color.WHITE);
        mSearchView.setQueryHint(getString(R.string.textHintSearchEnrollee));

        //clear button
        ImageView searchCloseIcon = (ImageView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchCloseIcon.setImageResource(R.drawable.ic_clear_search);

        //top button search icon
        ImageView searchIcon = (ImageView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchIcon.setImageResource(R.drawable.ic_search);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                updateInfo(query);
                if (!mSearchView.isIconified()) {
                    mSearchView.setIconified(true);
                }
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    updateInfo(newText);
                }
                return false;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mSearchView.onActionViewCollapsed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        int sizeArrNow = mApplicationsInfos.size();
        int sizeMustBe = applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).size();

        if ((sizeArrNow != sizeMustBe) && isDrawerClosed()) {
            updateInfo();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        updateInfo();
        super.onStop();
    }

    private void updateInfo() {
        mApplicationsInfos.clear();
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        mApplicationsInfos.addAll(applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId));
        if (mApplicationAdapter != null) {
            mApplicationAdapter.notifyDataSetChanged();
        }
    }

    private void updateInfo(String search) {
        mApplicationsInfos.clear();
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        if (search.isEmpty()) {
            mApplicationsInfos.addAll(applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId));
        } else {
            mApplicationsInfos.addAll(applicationInfoEngine.getAllApplicationsBySearchString(mLongSpecialityId, search));
        }
        if (mApplicationAdapter != null) {
            mApplicationAdapter.notifyDataSetChanged();
        }
    }

    //TODO пока не трогать
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //set applicantItem icon
//        MenuItem itemApplicantInfo = menu.add(0, ITEM_ID_APPLICANT_INFO, 0, R.string.textInfoForApplicant);
//        Drawable infoDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_black_24dp);
//        infoDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        itemApplicantInfo.setIcon(infoDrawable);
//        itemApplicantInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//
//
//        return super.onCreateOptionsMenu(menu);
//    }

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

        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        if (applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                parseData(DBConstants.Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(DBConstants.Update.NEED_AN_UPDATE);
                }

                //set textViews text importantInfo
                setTextImportantTextView();

                //set recyclerView with legend data
                setLegendList();
            }
        }
    }

    private void getData() {
        ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());
        mApplicationsInfos.clear();
        mApplicationsInfos.addAll(applicationInfoEngine.getAllApplicantionsById(mLongSpecialityId));
        mApplicationAdapter = new ApplicationAdapter(mApplicationsInfos);
        mApplicationAdapter.notifyDataSetChanged();
        mApplicationAdapter.setOnClickApplicationItem(ApplicationListActivity.this);
        mRecyclerView.setAdapter(mApplicationAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickApplicationItem(ApplicationsInfo applicationInfo) {
        if (mSearchView != null) {
            mSearchView.onActionViewCollapsed();
        }
        Intent intent = new Intent(this, DetailApplicantPagerActivity.class);
        intent.putExtra(DetailApplicantPagerActivity.INTENT_KEY_APPLICANT_INFO, applicationInfo);
        startActivity(intent);
    }

    private Boolean isDateComparison() {

        Calendar calendarCurrentTime = Calendar.getInstance();
        if (Utils.isNeedToEqualsTime()) {

            mCalendar.after(calendarCurrentTime);
            return true;
        } else {
            mCalendar = Utils.getModDeviceTime();
            return false;
        }
    }

    private void parseData(final boolean isNeedUpdate) {

        new Thread(new Runnable() {

            final ApplicationInfoEngine applicationInfoEngine = new ApplicationInfoEngine(getApplication());

            @Override
            public void run() {

                if (Utils.connectToData(mStrApplicantCodeLink) && mLongSpecialityId > -1) {
                    parse(mLongSpecialityId, applicationInfoEngine, isNeedUpdate);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //set textViews text importantInfo
                            setTextImportantTextView();

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                getData();
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                mProgressBar.setVisibility(View.VISIBLE);
                                getData();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void parse(long specialityId, ApplicationInfoEngine applicationInfoEngine, boolean isNeedUpdate) {
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

                String color;
                String someLink;

                Document document;
                ArrayList<ApplicationsInfo> applicationsInfos = new ArrayList<>();
                try {
                    document = Jsoup.connect(mStrApplicantCodeLink).get();

                    //get time and date update page
                    String strLastUpdatePage = document.select("div.title-page > p > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3] + "@" + arrayTimeDate[5];

                    //Data of applicants
                    Elements links = document.getElementsByClass("tablesaw tablesaw-stack tablesaw-sortable");
                    Elements elementsBody = links.select("tbody");
                    Elements selectTrApplicant = elementsBody.select("tr");

                    //Data of legend
                    Element legendElementById = document.getElementById("legend");
                    Elements selectLegendElements = legendElementById.select("tr");
                    LegendEngine legendEngine = new LegendEngine(getApplication());
                    if (legendEngine.getLegendsById(specialityId).isEmpty()) {
                        parseLegendData(selectLegendElements, legendEngine, specialityId);
                    }

                    //Data of importantInfo & Data of University information
                    Elements detailElements = document.getElementsByClass("title-page");
                    Elements elementsThead = links.select("thead");
                    Elements selectTrMarking = elementsThead.select("th");

                    ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getApplication());
                    if (importantApplicantInfoEngine.getImportantInfoById(specialityId) == null) {
                        Log.d("My", "getImportantInfoById == null");
                        parseImportantInfo(detailElements, selectTrMarking, specialityId);
                    }

                    for (Element link : selectTrApplicant) {
                        Elements tdElements = link.select("td");

                        //background
                        color = parseBackgroundApplicant(tdElements);

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

                        applicationsInfos.add(new ApplicationsInfo(specialityId,
                                number, name, priority, totalScore, markDocument, markTest, markExam,
                                extraPoints, originalDocument, someLink, color, dateUpdate));
                    }

                    if (isNeedUpdate) {
                        applicationInfoEngine.updateAllApplications(applicationsInfos);
                    } else {
                        applicationInfoEngine.addAllApplications(applicationsInfos);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //get legend data
    //todo грузить легеннду для каждого списка абитуры
    //todo учесть что там есть текст жирным шрифтом, доделать внешний вид легенды
    private void parseLegendData(Elements selectLegendElements, LegendEngine legendEngine, long specialityId) {
        final String getStyle = "*[style*='background']";

        for (Element legend : selectLegendElements) {
            Elements detailLegend = legend.select("td");
            String legendName = "";
            String legendDetail = "";
            String legendBackground = "";


            if (detailLegend.get(0).select(getStyle).size() > 0) {
                String style = detailLegend.get(0).select(getStyle).toString();
                if (style.length() > 132) {
                    legendBackground = "#FFFF99";
                } else {
                    legendBackground = style.substring(style.indexOf("#"), style.indexOf(";"));
                }
            } else {
                legendBackground = "#ffffff";
                legendName = detailLegend.get(0).text();
            }

            if (detailLegend.size() > 1) {
                legendDetail = detailLegend.get(1).text().trim();
            }

            if (detailLegend.size() == 1) {
                legendName = "";
                legendDetail = detailLegend.get(0).text().trim();
                legendBackground = "#e0e0e0";
            }
            legendEngine.addLegend(new LegendInfo(specialityId, legendName, legendDetail, legendBackground));
        }
    }

    private void parseImportantInfo(Elements detailElements, Elements selectTrMarking, long specialityId) {
        String universityName = "";
        String speciality = "";
        String specialization = "";
        String faculty = "";
        String timeForm = "";
        String lastTimeUpdate = "";

        String number ="";
        String name = "";
        String priority = "";
        String totalScore = "";
        String markDocument = "";
        String markTest = "";
        String markExam = "";
        String extraPoints = "";
        String originalDocument = "";

        ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getApplicationContext());
        if (importantApplicantInfoEngine.getImportantInfoById(specialityId) == null) {

            universityName = detailElements.select(".title-description").text();
            String universityInfos = "";
            if (detailElements.select("p").size() > 1) {
                universityInfos = (detailElements.select("p").get(1).toString()).replaceAll("(?i)<br[^>]*>", "/");
            } else {
                universityInfos = (detailElements.select("p").get(0).toString()).replaceAll("(?i)<br[^>]*>", "/");
            }

            universityInfos = universityInfos.substring(4, universityInfos.indexOf("<small"));
            String[] arrSpecialities = universityInfos.split("[/]");
            if (arrSpecialities.length == 4) {
                speciality = arrSpecialities[0];
                specialization = arrSpecialities[1];
                faculty = arrSpecialities[2];
                timeForm = arrSpecialities[3];
            }

            lastTimeUpdate = detailElements.select("p > small").text();

            //applicant data
            if (selectTrMarking.size() == 8) {
                number = selectTrMarking.get(0).text();
                name = selectTrMarking.get(1).text();
                totalScore = selectTrMarking.get(2).text();
                markDocument = selectTrMarking.get(3).text();
                markTest = selectTrMarking.get(4).text();
                markExam = selectTrMarking.get(5).text();
                extraPoints = selectTrMarking.get(6).text();
                originalDocument = selectTrMarking.get(7).text();

            } else {
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

            importantApplicantInfoEngine.addImportantInfo(new ImportantInfo(specialityId, universityName,
                    speciality, specialization, faculty, timeForm, lastTimeUpdate, number, name, priority, totalScore,
                    markDocument, markTest, markExam, extraPoints, originalDocument));
        }
    }


    //get background color applicant item
    //TODO get all backgrounds обработать все возможные трехзначные цвета
    //TODO даже при том что я вітаскиваю все цвета нужно правильно их ковертировать нужные мне цвета
    private String parseBackgroundApplicant(Elements tdElements) {
        final String getStyle = "*[style*='background']";
        String backgroundFirst;
        String backgroundSecond;
        String color;

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
        return color;
    }

}
