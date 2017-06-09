package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversitiesAdapter;
import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;

import java.util.ArrayList;

public class UniversitiesListActivity extends BaseActivity implements
        UniversitiesAdapter.OnClickUniversityItem {

    public static final String KEY_DETAIL_UNIVERSITY_LINK = "KEY_DETAIL_UNIVERSITY_LINK";

    private CategoryUniversInfo mCategoryUniversInfo;
    private long nLongCityId = -1;
    private String mStringDegree;

    private RecyclerView mRecyclerView;

    @Override
    protected void iniActivity() {
        Log.d("My", "UniversitiesListActivity --------> iniActivity");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCategoryUniversInfo = (CategoryUniversInfo) bundle.get(KEY_DETAIL_UNIVERSITY_LINK);
            }
        }

        TextView textViewHeadText = (TextView) findViewById(R.id.textViewChooseUniversityDetailUniversityActivity);
        textViewHeadText.setText(mCategoryUniversInfo.getStrCategoryUniversName());

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
    public void setDrawer() {
        super.setDrawer();
    }

    private void getData() {
        UniversitiesInfoEngine universityInfoEngine = new UniversitiesInfoEngine(getApplication());
        nLongCityId = mCategoryUniversInfo.getLongCityId();
        mStringDegree = mCategoryUniversInfo.getStrCategoryUniversName();

        Log.d("My","nLongCityId -> " + nLongCityId);
        Log.d("My","mStringDegree -> " + mStringDegree);
        ArrayList<UniversityInfo> universityInfos = universityInfoEngine
                .getAllUniversitiesByDegree(nLongCityId, mStringDegree);
        UniversitiesAdapter universitiesAdapter = new UniversitiesAdapter(universityInfos);
        universitiesAdapter.setOnClickUniversityItem(UniversitiesListActivity.this);
        mRecyclerView.setAdapter(universitiesAdapter);
    }

    @Override
    public void onClickCategoryUniversItem(UniversityInfo universityInfo) {
        Intent intent = new Intent(UniversitiesListActivity.this, DetailUniversListActivity.class);
        intent.putExtra(DetailUniversListActivity.KEY_DETAIL_UNIVERSITY_LINK, universityInfo);
        startActivity(intent);

    }
}
