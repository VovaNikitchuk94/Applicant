package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.vova.applicant.R;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.engines.SpecialityInfoEngine;
import com.example.vova.applicant.toolsAndConstans.Constans;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Favorite;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Update;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SpecialtiesListActivity extends BaseActivity {

    public static final String KEY_SPECIALITIES_LINK = "KEY_SPECIALITIES_LINK";
    public static final String KEY_SPECIALITIES_TITLE_STRING = "KEY_SPECIALITIES_TITLE_STRING";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView = null;

    private TimeFormInfo mTimeFormInfo;
    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();
    private SpecialitiesInfoAdapter mSpecialitiesInfoAdapter;
    private MultiSelector mMultiSelector = new MultiSelector();
    private Calendar mCalendar;

    private long mLongTimeFormId = -1;
    private long mLongDegree = -1;
    private String mTimeFormCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "SpecialtiesListActivity --------> initActivity");
        String strTitle = "";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // TODO rename all fields
                mTimeFormInfo = (TimeFormInfo) bundle.get(KEY_SPECIALITIES_LINK);
                strTitle = bundle.getString(KEY_SPECIALITIES_TITLE_STRING, "");
                if (mTimeFormInfo != null) {
                    mLongTimeFormId = mTimeFormInfo.getId();
                    mTimeFormCodeLink = mTimeFormInfo.getStrTimeFormLink();
                    mLongDegree = Long.parseLong(mTimeFormInfo.getStrTimeFormLink()
                            .substring(mTimeFormInfo.getStrTimeFormLink().length() - 1));
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_speciality_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setVisibility(View.GONE);
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        });

        TextView textView = (TextView) findViewById(R.id.textViewHeadAboutUniversityActivity);
        if (!strTitle.equals("")) {
            strTitle = strTitle.substring(0, strTitle.indexOf(" ("));
            textView.setText(strTitle + ", " + mTimeFormInfo.getStrTimeFormName());
        } else {
            textView.setText(mTimeFormInfo.getStrTimeFormName());
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSpecialityListActivity);
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
        return R.layout.activity_speciality_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        setToolbarSearchView(menu);
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
        mSearchView.setQueryHint("Test");

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
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        int sizeArrNow = mSpecialtiesInfos.size();
        int sizeMustBe = specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree).size();

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

    private boolean isCurrentYear() {
        return mTimeFormCodeLink.contains(Constans.URL_VSTUP_INFO_2017);
    }

    private void setData() {

        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        if (specialityInfoEngine.getAllSpecialitiesById(mLongTimeFormId).isEmpty()) {
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                parseData(Update.NO_NEED_TO_UPDATE);
            }
        } else {
            if (isDateComparison()) {
                getData();
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void updateInfo() {
        mSpecialtiesInfos.clear();
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        if (mSpecialitiesInfoAdapter != null) {
            mSpecialitiesInfoAdapter.notifyDataSetChanged();
        }
    }

    private void updateInfo(String search) {
        mSpecialtiesInfos.clear();
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        if (search.isEmpty()) {
            mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        } else {
            mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesBySearchString(mLongTimeFormId, mLongDegree, search));
        }
        if (mSpecialitiesInfoAdapter != null) {
            mSpecialitiesInfoAdapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());
        mSpecialtiesInfos.clear();
        mSpecialtiesInfos.addAll(specialityInfoEngine.getAllSpecialitiesByIdAndDegree(mLongTimeFormId, mLongDegree));
        mSpecialitiesInfoAdapter = new SpecialitiesInfoAdapter(mSpecialtiesInfos);
        mRecyclerView.setAdapter(mSpecialitiesInfoAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
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

            final SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplication());

            @Override
            public void run() {
                if (Utils.connectToData(mTimeFormInfo.getStrTimeFormLink()) && mLongTimeFormId != 0) {
                    parse(mLongTimeFormId, specialityInfoEngine, isNeedUpdate);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

            private void parse(long timeFormId, SpecialityInfoEngine specialityInfoEngine, boolean isNeedUpdate) {
                Document document;

                ArrayList<SpecialtiesInfo> specialtiesInfos = new ArrayList<>();
                String strCategory = mTimeFormCodeLink.substring(mTimeFormCodeLink.length() - 5, mTimeFormCodeLink.length());

                try {
                    String specialty;
                    String applications;
                    String accepted = "";
                    String recommended = "";
                    String licenseOrder = "";
                    String volumeOrder = "";
                    String exam = "";
                    String newLink = "";
                    String dateUpdate = "";

                    document = Jsoup.connect(mTimeFormCodeLink).get();

                    //get date update
                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    dateUpdate = arrayTimeDate[3] + "@" + arrayTimeDate[5];

                    Element form = document.getElementById(strCategory);
                    Elements links = form.select("tbody > tr");
                    //TODO правильно обработать всю инфу для 2017
                    //TODO правильно обработать загрузку данных
//                    //TODO загружает данные всех бакалавров, например Гетьмана веч форма -> бакалавр показало всех бакалавров, нужно открывать не только по специальности а еще и по timeForm
//                    //TODO пустые данные о (рекомендовано, зараховано) получают  поля с прошлых не пустых данных
                    for (Element link : links) {
                        Elements elements = link.getElementsByClass("button button-mini");
                        Elements tdElements = link.select("td");

                        specialty = (tdElements.get(0).toString()).replaceAll("(?i)<td[^>]*>", "")
                                .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");

                        // get more data applications
                        applications = tdElements.get(1).select("span").text();
                        if (tdElements.get(1).select("nobr").size() == 2) {
                            accepted = tdElements.get(1).select("nobr").get(0).text();
                            recommended = tdElements.get(1).select("nobr").get(1).text();
                        } else if (tdElements.get(1).select("nobr").size() == 1) {
                            recommended = tdElements.get(1).select("nobr").get(0).text();
                        } else {
                            recommended = null;
                        }

                        // get more data amount
                        if (tdElements.get(2).select("nobr").size() == 2) {
                            licenseOrder = tdElements.get(2).select("nobr").get(0).text();
                            volumeOrder = tdElements.get(2).select("nobr").get(1).text();
                        } else if (tdElements.get(2).select("nobr").size() == 1) {
                            volumeOrder = tdElements.get(2).select("nobr").get(0).text();
                        } else {
                            volumeOrder = null;
                        }

                        //attempt to get more data from exams
                        exam = (tdElements.get(3).toString()).replaceAll("(?i)<td[^>]*>", "")
                                .replaceAll("(?i)<[/]td[^>]*>", "").replaceAll("(?i)<br[^>]*>", "\n");

                        newLink = elements.attr("abs:href");

                        specialtiesInfos.add(new SpecialtiesInfo(timeFormId, mLongDegree,
                                specialty, applications, accepted, recommended, licenseOrder, volumeOrder,
                                exam, newLink, dateUpdate, Favorite.NOT_A_FAVORITE));
                    }

                    if (isNeedUpdate) {
                        specialityInfoEngine.updateAllSpecialities(specialtiesInfos);
                    } else {
                        specialityInfoEngine.addAllSpecialities(specialtiesInfos);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class SpecialitiesInfoAdapter extends RecyclerView.Adapter<SpecialitiesInfoAdapter.SpecialitiesInfoHolder> {

        private Context mContext = getApplicationContext();

        public SpecialitiesInfoAdapter(ArrayList<SpecialtiesInfo> specialtiesInfos) {
            mSpecialtiesInfos = specialtiesInfos;
        }

        @Override
        public SpecialitiesInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_specialties_info, parent, false);
            return new SpecialitiesInfoHolder(view);
        }

        @Override
        public void onBindViewHolder(SpecialitiesInfoHolder holder, int position) {
            SpecialtiesInfo specialtiesInfo = mSpecialtiesInfos.get(position);
            holder.bindCity(specialtiesInfo);
        }

        @Override
        public int getItemCount() {
            return mSpecialtiesInfos.size();
        }

        public class SpecialitiesInfoHolder extends SwappingHolder
                implements View.OnLongClickListener, View.OnClickListener {

            private TextView specialtyTextView;
            private TextView applicationsTextView;
            private TextView acceptedTextView;
            private TextView recommendedTextView;
            private TextView licenseOrderTextView;
            private TextView volumeOrderTextView;
            private SpecialtiesInfo mSpecialtiesInfo;


            public SpecialitiesInfoHolder(View itemView) {
                super(itemView, mMultiSelector);

                specialtyTextView = (TextView) itemView.findViewById(R.id.textViewSpecialtySpecialtiesInfo);
                applicationsTextView = (TextView) itemView.findViewById(R.id.textViewApplicationsSpecialtiesInfo);
                acceptedTextView = (TextView) itemView.findViewById(R.id.textViewmStrAcceptedSpecialtiesInfo);
                recommendedTextView = (TextView) itemView.findViewById(R.id.textViewmStrRecommendedSpecialtiesInfo);
                licenseOrderTextView = (TextView) itemView.findViewById(R.id.textViewmmStrLicensedOrderSpecialtiesInfo);
                volumeOrderTextView = (TextView) itemView.findViewById(R.id.textViewmmStrVolumeOrderSpecialtiesInfo);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);
            }

            public void bindCity(SpecialtiesInfo specialtiesInfo) {
                mSpecialtiesInfo = specialtiesInfo;
                specialtyTextView.setText(specialtiesInfo.getStrSpecialty());
                applicationsTextView.setText(specialtiesInfo.getStrApplications());
                acceptedTextView.setText(specialtiesInfo.getStrAccepted());
                recommendedTextView.setText(specialtiesInfo.getStrRecommended());
                licenseOrderTextView.setText(specialtiesInfo.getStrLicensedOrder());
                volumeOrderTextView.setText(specialtiesInfo.getStrVolumeOrder());
                //TODO обработать ошибку когда не пустые items определяються как пустые
                //TODO обработать ошибку когда в пустые списки дублируються записи из предыдущего item
                Log.d("My", "SpecialitiesAdapter Link ~ applications -> " + specialtiesInfo.getStrLink() + " ~ " +
                        specialtiesInfo.getStrApplications().startsWith("заяв: 0") + " ~ " + specialtiesInfo.getStrApplications());
//                if (specialtiesInfo.getStrLink().equals("") & specialtiesInfo.getStrApplications().startsWith("заяв: 0")) {
                if (specialtiesInfo.getStrLink().isEmpty()) {
                    Log.d("My", "SpecialitiesAdapter Link ~ applications -> " + specialtiesInfo.getStrLink());
                    int emptyColor = ContextCompat.getColor(mContext, R.color.md_grey_500);
                    specialtyTextView.setTextColor(emptyColor);
                    applicationsTextView.setTextColor(emptyColor);
                    acceptedTextView.setTextColor(emptyColor);
                    recommendedTextView.setTextColor(emptyColor);
                    licenseOrderTextView.setTextColor(emptyColor);
                    volumeOrderTextView.setTextColor(emptyColor);
                } else {
                    int normalTextColor = ContextCompat.getColor(mContext, R.color.primary_text);
                    specialtyTextView.setTextColor(normalTextColor);
                    applicationsTextView.setTextColor(normalTextColor);
                    acceptedTextView.setTextColor(normalTextColor);
                    recommendedTextView.setTextColor(normalTextColor);
                    licenseOrderTextView.setTextColor(normalTextColor);
                    volumeOrderTextView.setTextColor(normalTextColor);
                }

                setSelectable(mMultiSelector.isSelectable());
                setActivated(mMultiSelector.isSelected(getAdapterPosition(), getItemId()));
            }

            @Override
            public void onClick(View v) {
                if (mSpecialtiesInfo == null) {
                    return;
                }
                if (!mMultiSelector.tapSelection(SpecialitiesInfoHolder.this)) {
                    selectSpeciality(mSpecialtiesInfo);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                if (!mMultiSelector.isSelectable() && isCurrentYear()) {
                    startSupportActionMode(mActionModeCallBack);
                    mMultiSelector.setSelectable(true);
                    mMultiSelector.setSelected(SpecialitiesInfoHolder.this, true);
                    return true;
                }
                return false;
            }
        }
    }

    private void selectSpeciality(SpecialtiesInfo specialtiesInfo) {
        if (!specialtiesInfo.getStrLink().isEmpty()) {
            if (mSearchView != null) {
                mSearchView.onActionViewCollapsed();
            }
            Intent intent = new Intent(SpecialtiesListActivity.this, ApplicationListActivity.class);
            intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY, specialtiesInfo);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private ModalMultiSelectorCallback mActionModeCallBack = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_to_favorite, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_item_add_to_favorite:

                    mode.finish();
                    SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getApplicationContext());

                    for (int i = mSpecialtiesInfos.size() - 1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            SpecialtiesInfo specialtiesInfo = mSpecialtiesInfos.get(i);
                            specialtiesInfo.setIsFavorite(Favorite.FAVORITE);
                            specialityInfoEngine.updateSpeciality(specialtiesInfo);
                        }
                    }
                    return true;

                default:
                    break;
            }
            return false;
        }
    };
}
