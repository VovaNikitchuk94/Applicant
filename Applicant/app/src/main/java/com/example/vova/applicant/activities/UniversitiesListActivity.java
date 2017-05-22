package com.example.vova.applicant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        Log.d("My", "UniversitiesListActivity onCreate");

        drawerAndToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCategoryUniversInfo = (CategoryUniversInfo) bundle.get(KEY_DETAIL_UNIVERSITY_LINK);
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_university_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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
    public void drawerAndToolbar() {
        super.drawerAndToolbar();
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
