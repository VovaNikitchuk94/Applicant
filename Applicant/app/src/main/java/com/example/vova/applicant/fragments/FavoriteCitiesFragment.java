package com.example.vova.applicant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vova.applicant.R;
import com.example.vova.applicant.activities.CategoryUniversListActivity;
import com.example.vova.applicant.adapters.CitiesAdapter;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.engines.CitiesInfoEngine;

import java.util.ArrayList;

public class FavoriteCitiesFragment extends Fragment implements CitiesAdapter.OnClickCityItem{

    private RecyclerView mRecyclerView;
    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();
    private CitiesAdapter mCitiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.universalRecyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        getData();

        return view;
    }

    private void getData() {
        CitiesInfoEngine citiesInfoEngine = new CitiesInfoEngine(getContext());
        mCitiesInfos.clear();
        mCitiesInfos.addAll(citiesInfoEngine.getAllFavoriteCities());
        mCitiesAdapter = new CitiesAdapter(mCitiesInfos);
        mCitiesAdapter.notifyDataSetChanged();
        mCitiesAdapter.setOnClickCityInfoItem(this);
        mRecyclerView.setAdapter(mCitiesAdapter);
    }

    @Override
    public void onClickCityItem(CitiesInfo citiesInfo) {
        Intent intent = new Intent(getContext(), CategoryUniversListActivity.class);
        intent.putExtra(CategoryUniversListActivity.INTENT_KEY_UNIVERSITY_ACTIVITY, citiesInfo);
        startActivity(intent);
    }
}
