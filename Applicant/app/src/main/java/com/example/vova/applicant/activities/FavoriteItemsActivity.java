package com.example.vova.applicant.activities;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.CitiesInfoAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;

import java.util.ArrayList;

public class FavoriteItemsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mTextView;

    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();
    private CitiesInfoAdapter mCitiesInfoAdapter;

    private static final int FAVORITE = 1;

    @Override
    protected void initActivity() {

        mTextView = (TextView) findViewById(R.id.textViewFavoriteItemsList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewFavoriteItemsListActivity);
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
        return R.layout.activity_favorite_items_list;
    }

    private void getData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getApplication());
        mCitiesInfos.clear();
        if (!citiesInfoEngine.getAllFavoriteCities(FAVORITE).isEmpty()) {
            mCitiesInfos.addAll(citiesInfoEngine.getAllFavoriteCities(FAVORITE));
            mCitiesInfoAdapter = new CitiesInfoAdapter(mCitiesInfos);
            mCitiesInfoAdapter.notifyDataSetChanged();
//        mCitiesInfoAdapter.setOnClickCityInfoItem(FavoriteItemsActivity.this);
            mRecyclerView.setAdapter(mCitiesInfoAdapter);
        } else {
            mTextView.setText("Favorite cities is empty");
        }

    }
}
