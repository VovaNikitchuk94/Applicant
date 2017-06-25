package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

//        View searchplate = (View)searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
//        searchplate.setBackgroundResource(R.drawable.texfield_searchview_holo_light);

        //clear button
        ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        searchCloseIcon.setImageResource(R.drawable.ic_clear_search);

//        ImageView voiceIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search);
//        voiceIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        voiceIcon.setImageResource(R.drawable.ic_search);

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
        mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        if (mUniversitiesAdapter != null) {
            mUniversitiesAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void onBackPressed() {
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        int sizeArrNow = mUniversityInfos.size();
        int sizeMustBe = universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory).size();

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
