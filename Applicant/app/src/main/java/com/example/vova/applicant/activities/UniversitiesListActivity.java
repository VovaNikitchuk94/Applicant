package com.example.vova.applicant.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.vova.applicant.R;
import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;
import com.example.vova.applicant.toolsAndConstans.Constans;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Favorite;

import java.util.ArrayList;

public class UniversitiesListActivity extends BaseActivity {

    public static final String KEY_CATEGORY_UNIVERSITY_LINK = "KEY_CATEGORY_UNIVERSITY_LINK";

    private RecyclerView mRecyclerView;
    private MultiSelector mMultiSelector = new MultiSelector();

    private CategoryUniversInfo mCategoryUniversInfo;
    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();
    private UniversitiesInfoAdapter mUniversitiesInfoAdapter;
    private SearchView mSearchView = null;

    private long nLongCityId = -1;
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
        mSearchView.setQueryHint(getString(R.string.textHintSearchUniversity));

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

    //TODO сравнить с ситилист активити
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentYear() {
        return mCategoryUniversInfo.getStrCategoryLink().contains(Constans.URL_VSTUP_INFO_2017);
    }

    private void updateInfo(){
        mUniversityInfos.clear();
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        if (mUniversitiesInfoAdapter != null) {
            mUniversitiesInfoAdapter.notifyDataSetChanged();
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
        if (mUniversitiesInfoAdapter != null) {
            mUniversitiesInfoAdapter.notifyDataSetChanged();
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
        updateInfo();
        super.onStop();
    }

    private void getData() {
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        mUniversityInfos.addAll(universityInfoEngine.getAllUniversitiesByDegree(nLongCityId, mStringCategory));
        mUniversitiesInfoAdapter = new UniversitiesInfoAdapter(mUniversityInfos);
        mUniversitiesInfoAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mUniversitiesInfoAdapter);
    }

    private class UniversitiesInfoAdapter extends RecyclerView.Adapter<UniversitiesInfoAdapter.UniversityInfoHolder> {

        public UniversitiesInfoAdapter(ArrayList<UniversityInfo> universityInfos) {
            mUniversityInfos = universityInfos;
        }

        @Override
        public UniversityInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_university_info, parent, false);
            return new UniversityInfoHolder(view);
        }

        @Override
        public void onBindViewHolder(UniversityInfoHolder holder, int position) {
            UniversityInfo universityInfo = mUniversityInfos.get(position);
            holder.bindCity(universityInfo);
        }

        @Override
        public int getItemCount() {
            return mUniversityInfos.size();
        }

        public class UniversityInfoHolder extends SwappingHolder
                implements View.OnLongClickListener, View.OnClickListener {

            private TextView nameTextView;
            private UniversityInfo mUniversityInfo;


            public UniversityInfoHolder(View itemView) {
                super(itemView, mMultiSelector);

                nameTextView = (TextView) itemView.findViewById(R.id.textViewTypeUniversityInfo);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);
            }

            public void bindCity(UniversityInfo universityInfo) {
                mUniversityInfo = universityInfo;
                nameTextView.setText(universityInfo.getStrUniversityName());

                setSelectable(mMultiSelector.isSelectable());
                setActivated(mMultiSelector.isSelected(getAdapterPosition(), getItemId()));
            }

            @Override
            public void onClick(View v) {
                Log.d("My", "onClick work getId -> " + v.getId());
                if (mUniversityInfo == null) {
                    return;
                }
                if (!mMultiSelector.tapSelection(UniversityInfoHolder.this)) {
                    selectUniversity(mUniversityInfo);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                Log.d("My", "onLongClick work -> ");
                Log.d("My", "onLongClick v.getId() -> " + v.getId());

                if (!mMultiSelector.isSelectable() && isCurrentYear()) {
                    startSupportActionMode(mActionModeCallBack);
                    Log.d("My", "onLongClick mMultiSelector.tapSelection(this) -> " + true);
                    mMultiSelector.setSelectable(true);
                    mMultiSelector.setSelected(UniversityInfoHolder.this, true);
                    return true;
                } else {
                    Log.d("My", "onLongClick else -> ");
                }
                return false;

            }
        }
    }

    private void selectUniversity(UniversityInfo universityInfo) {
        if (mSearchView != null) {
            mSearchView.onActionViewCollapsed();
        }
        Intent intent = new Intent(UniversitiesListActivity.this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        startActivity(intent);
    }

    private ModalMultiSelectorCallback mActionModeCallBack = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Log.d("My", "onCreateActionMode start ->");
            getMenuInflater().inflate(R.menu.menu_add_to_favorite, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.d("My", "onActionItemClicked start ->");

            switch (item.getItemId()) {
                case R.id.menu_item_add_to_favorite:

                    mode.finish();
                    UniversitiesInfoEngine engine = new UniversitiesInfoEngine(getApplicationContext());

                    for (int i = mUniversityInfos.size()-1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            UniversityInfo universityInfo = mUniversityInfos.get(i);

                            universityInfo.setIsFavorite(Favorite.FAVORITE);
                            engine.updateUniversity(universityInfo);
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
