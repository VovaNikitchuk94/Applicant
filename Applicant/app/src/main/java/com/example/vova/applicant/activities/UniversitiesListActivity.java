package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversitiesAdapter;
import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;

import java.util.ArrayList;

public class UniversitiesListActivity extends BaseActivity implements
        UniversitiesAdapter.OnClickUniversityItem {

    public static final String KEY_CATEGORY_UNIVERSITY_LINK = "KEY_CATEGORY_UNIVERSITY_LINK";

    private RecyclerView mRecyclerView;
    private SearchView searchView;

    private CategoryUniversInfo mCategoryUniversInfo;
    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();
    private UniversitiesAdapter mUniversitiesAdapter;

    private long nLongCityId = 0;
    private String mStringCategory;
    private String mStrCitySearch = "";

    @Override
    protected void initActivity() {
        Log.d("My", "UniversitiesListActivity --------> initActivity");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCategoryUniversInfo = (CategoryUniversInfo) bundle.get(KEY_CATEGORY_UNIVERSITY_LINK);
                if (mCategoryUniversInfo != null) {
                    nLongCityId = mCategoryUniversInfo.getLongCityId();
                    mStringCategory = mCategoryUniversInfo.getStrCategoryName();
                }
            }
        }

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        textViewHeadText.setText(mCategoryUniversInfo.getStrCategoryName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUniversitiesListActivity);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        getData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_university_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//
//                return true;
//            }
//        });
//        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                Log.d("My", "onMenuItemActionExpand");
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Log.d("My", "onMenuItemActionCollapse");
//                updateInfo("");
//                return true;
//            }
//        });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                updateInfo(query);
//                if(!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
                searchView.setIconified(false);
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
                Log.d("My", "setOnQueryTextFocusChangeListener hasFocus -> "  + hasFocus);
                if (!hasFocus) {
                    searchView.onActionViewCollapsed();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("My", "onOptionsItemSelected item -> " + item);
        return super.onOptionsItemSelected(item);
    }


    private void updateInfo(){
        mUniversityInfos.clear();
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        if(mStrCitySearch.isEmpty()){
            mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        } else {
            mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesBySearchString(nLongCityId, mStringCategory, mStrCitySearch));
        }
        mUniversitiesAdapter.notifyDataSetChanged();
    }

    private void updateInfo(String search){
        mUniversityInfos.clear();
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        if(search.isEmpty()){
            mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        } else {
            mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesBySearchString(nLongCityId, mStringCategory, search));
        }

        if (mUniversitiesAdapter != null) {
            mUniversitiesAdapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        mUniversitiesAdapter = new UniversitiesAdapter(mUniversityInfos);
        mUniversitiesAdapter.notifyDataSetChanged();
        mUniversitiesAdapter.setOnClickUniversityItem(UniversitiesListActivity.this);
        mRecyclerView.setAdapter(mUniversitiesAdapter);
    }

    @Override
    public void onClickCategoryUniversItem(UniversityInfo universityInfo) {
        Intent intent = new Intent(UniversitiesListActivity.this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        startActivity(intent);

    }
}
