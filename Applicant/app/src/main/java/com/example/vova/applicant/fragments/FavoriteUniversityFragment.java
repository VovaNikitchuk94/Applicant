package com.example.vova.applicant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.UniversitiesAdapter;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.engines.UniversitiesInfoEngine;

import java.util.ArrayList;

public class FavoriteUniversityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<UniversityInfo> mUniversityInfos = new ArrayList<>();
    private UniversitiesAdapter mUniversitiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_recycler_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.favoriteRecyclerView);
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
        UniversitiesInfoEngine universitiesInfoEngine = new UniversitiesInfoEngine(getContext());
        mUniversityInfos.clear();
        mUniversityInfos.addAll(universitiesInfoEngine.getAllFavoriteUniversities());
        mUniversitiesAdapter = new UniversitiesAdapter(mUniversityInfos);
        mUniversitiesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mUniversitiesAdapter);
    }
}
