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
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;
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

public class CitiesListActivity extends BaseActivity {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";
    private static final String DATE_KEY_INDEX = "date_index";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView = null;

    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();
    private MultiSelector mMultiSelector = new MultiSelector();

    private CitiesInfoAdapter mCitiesInfoAdapter;
    private Calendar mCalendar;

    private long mLongYearId = -1;
    private String mYearsCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "CitiesListActivity --------> initActivity");

        Utils.setNeedToEqualsTime(true);
        mCalendar = Utils.getModDeviceTime();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mYearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
                if (mYearsCodeLink != null) {
                    mLongYearId = Long.parseLong(mYearsCodeLink.
                            substring(mYearsCodeLink.length() - 2, mYearsCodeLink.length() - 1));
                }
            } else {
                mYearsCodeLink = Constans.URL_VSTUP_INFO_2017;
                mLongYearId = Long.parseLong(mYearsCodeLink.
                        substring(mYearsCodeLink.length() - 2, mYearsCodeLink.length() - 1));
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_cities_swipe_refresh_layout);
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

//        TextView textView = (TextView) findViewById(R.id.textViewСhoiceCityCitiesActivity);
//        textView.setText(getText(R.string.chooseCityMainActivity));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewCitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setData();
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
        mSearchView.setQueryHint(getString(R.string.textHintSearchCities));

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

    //TODO обработать все варианты нажатий
    @Override
    public void onBackPressed() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        int sizeArrNow = mCitiesInfos.size();
        int sizeArrMustBe = citiesInfoEngine.getAllCitiesById(mLongYearId).size();

        if ((sizeArrNow != sizeArrMustBe) && isDrawerClosed()) {
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

    //TODO слхранять календать
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("My", "onSaveInstanceState  -> ");
        outState.putSerializable(DATE_KEY_INDEX, mCalendar);
        outState.putSerializable("KEY_URL", mYearsCodeLink);
        outState.putSerializable("KEY_YEAR_ID", mLongYearId);
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        setFirstStart(false);
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cities_list;
    }


    //TODO modified method
    private void setData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {

            //TODO upgrade, check data in recyclerView
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
                } else {
                    parseData(Update.NEED_AN_UPDATE);
                }
            }
        }
    }

    private void updateInfo() {
        mCitiesInfos.clear();
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        mCitiesInfos.addAll(citiesInfoEngine.getAllCitiesById(mLongYearId));
        if (mCitiesInfoAdapter != null) {
            mCitiesInfoAdapter.notifyDataSetChanged();
        }
    }

    private void updateInfo(String search) {
        mCitiesInfos.clear();
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        if (search.isEmpty()) {
            mCitiesInfos.addAll(citiesInfoEngine.getAllCitiesById(mLongYearId));
        } else {
            mCitiesInfos.addAll(citiesInfoEngine.getAllCitiesBySearchString(mLongYearId, search));
        }
        if (mCitiesInfoAdapter != null) {
            mCitiesInfoAdapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        mCitiesInfos.clear();
        mCitiesInfos.addAll(citiesInfoEngine.getAllCitiesById(mLongYearId));
        mCitiesInfoAdapter = new CitiesInfoAdapter(mCitiesInfos);
        mCitiesInfoAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mCitiesInfoAdapter);
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
            @Override
            public void run() {

                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                if (Utils.connectToData(mYearsCodeLink) && mLongYearId > -1) {
                    parse(mLongYearId, citiesInfoEngine, isNeedUpdate);

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
                        }
                    });
                }
            }

            //TODO обработать парсинг страницы с рекламой!
            private void parse(long yearsId, CitiesInfoEngine citiesInfoEngine, boolean isNeedUpdate) {
                Document document;
                ArrayList<CitiesInfo> citiesInfos = new ArrayList<>();
                try {
                    document = Jsoup.connect(mYearsCodeLink).get();

                    String strLastUpdatePage = document.select("div.title-page > small").text();
                    String[] arrayTimeDate = strLastUpdatePage.split(" ");
                    String dateUpdate = arrayTimeDate[3] + "@" + arrayTimeDate[5];

                    Element elementRegion = document.getElementById("region");

                    Elements trElements = elementRegion.select("tr");
                    for (Element link : trElements) {
                        Elements tdElements = link.select("td");
                        Elements aElements = tdElements.get(0).getElementsByTag("a");

                        String citiesName = tdElements.get(0).text();
                        String citiesLink = aElements.attr("abs:href");

                        citiesInfos.add(new CitiesInfo(yearsId, citiesName, citiesLink, dateUpdate, Favorite.NOT_A_FAVORITE));
                    }

                    if (isNeedUpdate) {
                        citiesInfoEngine.updateAllCitiies(citiesInfos);
                    } else {
                        citiesInfoEngine.addAllCities(citiesInfos);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class CitiesInfoAdapter extends RecyclerView.Adapter<CitiesInfoAdapter.CitiesInfoHolder> {

        public CitiesInfoAdapter(ArrayList<CitiesInfo> citiesInfos) {
            mCitiesInfos = citiesInfos;
        }

        @Override
        public CitiesInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_cities_info, parent, false);
            return new CitiesInfoHolder(view);
        }

        @Override
        public void onBindViewHolder(CitiesInfoHolder holder, int position) {
            CitiesInfo citiesInfo = mCitiesInfos.get(position);
            holder.bindCity(citiesInfo);
        }

        @Override
        public int getItemCount() {
            return mCitiesInfos.size();
        }

        public class CitiesInfoHolder extends SwappingHolder
                implements View.OnLongClickListener, View.OnClickListener {

            private TextView nameTextView;
            private CitiesInfo mCitiesInfo;


            public CitiesInfoHolder(View itemView) {
                super(itemView, mMultiSelector);

                nameTextView = (TextView) itemView.findViewById(R.id.textViewCitiesInfoNameCitiesInfoItem);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);
            }

            public void bindCity(CitiesInfo citiesInfo) {
                mCitiesInfo = citiesInfo;
                nameTextView.setText(citiesInfo.getStrCityName());

                setSelectable(mMultiSelector.isSelectable());
                setActivated(mMultiSelector.isSelected(getAdapterPosition(), getItemId()));
            }

            @Override
            public void onClick(View v) {
                Log.d("My", "onClick work getId -> " + v.getId());
                if (mCitiesInfo == null) {
                    return;
                }
                if (!mMultiSelector.tapSelection(CitiesInfoHolder.this)) {
                    selectCity(mCitiesInfo);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                Log.d("My", "onLongClick work -> ");
                Log.d("My", "onLongClick v.getId() -> " + v.getId());

                if (!mMultiSelector.isSelectable()) {
                    startSupportActionMode(mActionModeCallBack);
                    Log.d("My", "onLongClick mMultiSelector.tapSelection(this) -> " + true);
                    mMultiSelector.setSelectable(true);
                    mMultiSelector.setSelected(CitiesInfoHolder.this, true);
                    return true;
                }
                return false;
            }
        }
    }

    private void selectCity(CitiesInfo citiesInfo) {
        if (mSearchView != null) {
            mSearchView.onActionViewCollapsed();
        }
        Intent intent = new Intent(this, CategoryUniversListActivity.class);
        intent.putExtra(CategoryUniversListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, citiesInfo);
        startActivity(intent);
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
                    CitiesInfoEngine engine = new CitiesInfoEngine(getApplicationContext());

                    for (int i = mCitiesInfos.size()-1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            CitiesInfo citiesInfo = mCitiesInfos.get(i);
                            citiesInfo.setFavorite(1);
                            engine.updateCity(citiesInfo);
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

