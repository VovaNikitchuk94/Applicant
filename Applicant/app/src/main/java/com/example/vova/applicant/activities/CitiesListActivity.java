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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.CitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;
import com.example.vova.applicant.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CitiesListActivity extends BaseActivity implements CitiesInfoAdapter.OnClickCityItem {

    public static final String KEY_YEARS_CITIES_LIST_ACTIVITY = "KEY_YEARS_CITIES_LIST_ACTIVITY";
    private static final int NOT_FAVORITE = 0;
    private static final int MENU_ITEM_ADD_TO_FAVORITE = 123;

    private static final boolean NEED_UPDATE = true;
    private static final boolean DONT_NEED_UPDATE = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();
    private CitiesInfoAdapter mCitiesInfoAdapter;

    private long mLongYearId = 0;
    private String mYearsCodeLink = "";

    @Override
    protected void initActivity() {
        Log.d("My", "CitiesListActivity --------> initActivity");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mYearsCodeLink = bundle.getString(KEY_YEARS_CITIES_LIST_ACTIVITY);
                if (mYearsCodeLink != null) {
                    mLongYearId = Long.parseLong(mYearsCodeLink.
                            substring(mYearsCodeLink.length() - 2, mYearsCodeLink.length() - 1));
                }
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
//                TODO запускать только когда можно обновить данные
                Log.d("My", "SwipeRefreshLayout -> updateData -> is start");
                if (!isOnline(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData() is started;");
                    parseData(NEED_UPDATE);
                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData(); is finished");
                }
                Log.d("My", "SwipeRefreshLayout -> updateData -> is finish");
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        TextView textView = (TextView) findViewById(R.id.textViewСhoiceCityCitiesActivity);
        textView.setText(getText(R.string.chooseCityMainActivity));

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

        MenuItem itemAddToFavorite = menu.add(3, MENU_ITEM_ADD_TO_FAVORITE, 3, R.string.textAddToFavorite);
        itemAddToFavorite.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    private void setToolbarSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //customized searchView from stackOverflow help
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.material_drawer_hint_text));
        searchAutoComplete.setTextColor(Color.WHITE);
        searchView.setQueryHint("Test");

        //clear button
        ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchCloseIcon.setImageResource(R.drawable.ic_clear_search);

        //top button search icon
        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchIcon.setImageResource(R.drawable.ic_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                updateInfo(query);
                if(!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
//                searchView.setIconified(false);
                searchView.clearFocus();

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

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("My", "setOnQueryTextFocusChangeListener onFocusChange");
                Log.d("My", "setOnQueryTextFocusChangeListener hasFocus -> " + hasFocus);
                if (!hasFocus) {
                    searchView.onActionViewCollapsed();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ITEM_ADD_TO_FAVORITE:
                Toast.makeText(this, "MENU_ITEM_ADD_TO_FAVORITE selected", Toast.LENGTH_SHORT).show();
                break;
        }
        Log.d("My", "onOptionsItemSelected item -> " + item);
        return super.onOptionsItemSelected(item);
    }

    //TODO обработать все варианты нажатий
    @Override
    public void onBackPressed() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        int sizeArrNow = mCitiesInfos.size();
        int sizeMustBe = citiesInfoEngine.getAllCitiesById(mLongYearId).size();

//        Log.d("My", "onBackPressed mCitiesInfos.size(); -> " + mCitiesInfos.size());
//        Log.d("My", "onBackPressed citiesInfoEngine.getAllCitiesById(mLongYearId).size() -> " + citiesInfoEngine.getAllCitiesById(mLongYearId).size());
        if ((sizeArrNow != sizeMustBe) && isDrawerClosed()) {
            updateInfo();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cities_list;
    }


    //TODO modified method
    private void setData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        if (citiesInfoEngine.getAllCitiesById(mLongYearId).isEmpty()) {

            //TODO проверять наличие интернета и уведомлять пользователя если его нет
            //TODO upgrade, check data in recyclerView
            if (!isOnline(this)) {
                Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                finish();
            }
//            Log.d("My", "CitiesListActivity -> parseData");
            parseData(DONT_NEED_UPDATE);

        } else {

            if (isDateComparison()) {
//                Log.d("My", "CitiesListActivity -> isDateComparison  getData(citiesInfoEngine); ");
                getData(citiesInfoEngine);
            } else {
                if (!isOnline(this)) {
                    Toast.makeText(this, R.string.textNOInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
//                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData() is started;");
                    parseData(NEED_UPDATE);
//                    parseData();
//                    Log.d("My", "CitiesListActivity -> (isDateComparison()) updateData(); is finished");
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

    //try use Kostya's method
    private void getData(CitiesInfoEngine citiesInfoEngine) {
        mCitiesInfos.clear();
        mCitiesInfos.addAll(citiesInfoEngine.getAllCitiesById(mLongYearId));
        mCitiesInfoAdapter = new CitiesInfoAdapter(mCitiesInfos);
        mCitiesInfoAdapter.notifyDataSetChanged();
        mCitiesInfoAdapter.setOnClickCityInfoItem(CitiesListActivity.this);
        mRecyclerView.setAdapter(mCitiesInfoAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private Boolean isDateComparison() {

        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());

                String parseDate = "";
                parseDate = parseDateAndTime();

                String dateAndTime = citiesInfoEngine.getCityById(mLongYearId).getStrDateLastUpdate();

//                Log.d("My", "isDateComparison dateAndTimeCities -> " + dateAndTime);

                if (parseDate.equals(dateAndTime)) {
//                    Log.d("My", " isDateComparison parseDate.equals(dateAndTime) -> " + true);
                    return true;
                }
                return false;
            }
        };

        FutureTask<Boolean> task = new FutureTask<>(callable);
        Thread t = new Thread(task);
        t.start();

        try {
//            Log.d("My", " isDateComparison task.get() -> " + task.get());
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String parseDateAndTime() {
        Document document;
        String dateUpdateAndTime = null;
        try {
            document = Jsoup.connect(mYearsCodeLink).get();

            //TODO при обновлении нужно затирать всю цепочку связаных данных в БД

            //get timeUpdate and dateUpdate update page
            String strLastUpdatePage = document.select("div.title-page > small").text();
//            Log.d("My", "strLastUpdatePage -> " + strLastUpdatePage);
            String[] arrayTimeDate = strLastUpdatePage.split(" ");

            dateUpdateAndTime = arrayTimeDate[3] + "@" + arrayTimeDate[5];
//            Log.d("My", "dateUpdateAndTime -> " + dateUpdateAndTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dateUpdateAndTime;
    }

    @Override
    public void onClickCityItem(CitiesInfo citiesInfo) {

        Intent intent = new Intent(this, CategoryUniversListActivity.class);
        intent.putExtra(CategoryUniversListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, citiesInfo);
        startActivity(intent);
    }

    private void parseData(final boolean isNeedUpdate) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
//                boolean isNeedUpdate = isDateComparison();
                if (Utils.connectToData(mYearsCodeLink) && mLongYearId != 0) {
                    parse(mLongYearId, citiesInfoEngine, isNeedUpdate);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
                            getData(citiesInfoEngine);
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

            private void parse(long yearsId, CitiesInfoEngine citiesInfoEngine, boolean isNeedUpdate) {
                Document document;
                ArrayList<CitiesInfo> citiesInfos = new ArrayList<>();
                try {
                    document = Jsoup.connect(mYearsCodeLink).get();

                    String dateUpdate = parseDateAndTime();

                    Element elementRegion = document.getElementById("region");
                    Elements linksByTag = elementRegion.getElementsByTag("a");

                    for (Element link : linksByTag) {

                        String citiesName = link.text();
                        String citiesLink = link.attr("abs:href");

                        citiesInfos.add(new CitiesInfo(yearsId, citiesName, citiesLink, dateUpdate, NOT_FAVORITE));

//                        citiesInfoEngine.addItem(new CitiesInfo(yearsId, citiesName, citiesLink, dateUpdate, NOT_FAVORITE));

                        Log.d("My", "parse yearsId -> " + yearsId);
                        Log.d("My", "parse citiesName -> " + citiesName);
                        Log.d("My", "parse citiesLink -> " + citiesLink);
                        Log.d("My", "parse dateUpdate -> " + dateUpdate);
                    }
                    Log.d("My", "citiesInfos.size() -> " + citiesInfos.size());

                    if (!isNeedUpdate) {
                        citiesInfoEngine.addAllCities(citiesInfos);
                    } else {
//                        citiesInfoEngine.updateCity();
                        citiesInfoEngine.updateAllCitiies(citiesInfos);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //try use single method for update data
//    private void updateData() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
//                if (Utils.connectToData(mYearsCodeLink) && mLongYearId != 0) {
//                    update(mLongYearId, citiesInfoEngine);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressBar.setVisibility(View.VISIBLE);
//                            final CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
//                            getData(citiesInfoEngine);
//                        }
//                    });
//
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), R.string.textBadInternetConnection, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            private void update(long yearsId, CitiesInfoEngine citiesInfoEngine) {
//                Document document;
//                try {
//                    document = Jsoup.connect(mYearsCodeLink).get();
//
//                    String dateUpdate = parseDateAndTime();
////                    String dateUpdate = "20/02/17@23:45";
//
//                    Element elementRegion = document.getElementById("region");
//                    Elements linksByTag = elementRegion.getElementsByTag("a");
//
//                    for (Element link : linksByTag) {
//
//                        String citiesName = link.text();
//                        String citiesLink = link.attr("abs:href");
//
//                        citiesInfoEngine.updateCity(new CitiesInfo(yearsId, citiesName, citiesLink, dateUpdate, NOT_FAVORITE));
////                        Log.d("My", "update yearsId -> " + yearsId);
////                        Log.d("My", "update citiesName -> " + citiesName);
////                        Log.d("My", "update citiesLink -> " + citiesLink);
////                        Log.d("My", "update dateUpdate -> " + dateUpdate);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}


