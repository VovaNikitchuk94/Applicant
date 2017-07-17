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
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.activities.ApplicationListActivity;
import com.example.vova.applicant.adapters.SpecialitiesAdapter;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.engines.SpecialityInfoEngine;

import java.util.ArrayList;

public class FavoriteSpecialitiesFragment extends Fragment implements SpecialitiesAdapter.OnClickSpecialityItem{

    private RecyclerView mRecyclerView;
    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();
    private SpecialitiesAdapter mSpecialitiesAdapter;

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
        SpecialityInfoEngine specialityInfoEngine = new SpecialityInfoEngine(getContext());
        mSpecialtiesInfos.clear();
        mSpecialtiesInfos.addAll(specialityInfoEngine.getAllFavoriteSpecialities());
        mSpecialitiesAdapter = new SpecialitiesAdapter(mSpecialtiesInfos);
        mSpecialitiesAdapter.notifyDataSetChanged();
        mSpecialitiesAdapter.setOnClickSpecialityItem(this);
        mRecyclerView.setAdapter(mSpecialitiesAdapter);
    }

    @Override
    public void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo) {
        if (!specialtiesInfo.getStrLink().isEmpty()) {
            Intent intent = new Intent(getContext(), ApplicationListActivity.class);
            intent.putExtra(ApplicationListActivity.INTENT_KEY_APPLICANT_ACTIVITY, specialtiesInfo);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.textEmptyData), Toast.LENGTH_SHORT).show();
        }
    }
}
