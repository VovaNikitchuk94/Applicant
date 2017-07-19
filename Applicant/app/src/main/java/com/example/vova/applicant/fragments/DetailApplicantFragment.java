package com.example.vova.applicant.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vova.applicant.R;
import com.example.vova.applicant.adapters.DetailApplicantAdapter;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.engines.ApplicationsEngine;
import com.example.vova.applicant.model.engines.ImportantApplicantInfoEngine;

import java.util.ArrayList;

public class DetailApplicantFragment extends Fragment {

    public static final String ARG_APPLICANT_ID = "ARG_APPLICANT_ID";
    private long mDetailApplicantId;
    private ApplicationsInfo mApplicationsInfo;
    private RecyclerView mRecyclerView;
    private DetailApplicantAdapter mDetailApplicantAdapter;

    private ArrayList<String> mFullApplicantInfoArray = new ArrayList<>();
    private ArrayList<String> mImportantInfoArray = new ArrayList<>();

    public static DetailApplicantFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_APPLICANT_ID, id);

        DetailApplicantFragment fragment = new DetailApplicantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailApplicantId = (long) getArguments().getSerializable(ARG_APPLICANT_ID);
        ApplicationsEngine applicationsEngine = new ApplicationsEngine(getContext());
        mApplicationsInfo = applicationsEngine.getApplicationById(mDetailApplicantId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_applicant, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.universalRecyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mApplicationsInfo != null) {

            //set legend textViews
            ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getActivity());
            ImportantInfo importantInfo = importantApplicantInfoEngine.getImportantInfoById(mApplicationsInfo.getLongSpecialityId());

            String[] fullDataArrayApplicant = mApplicationsInfo.getStrApplicantFullData().split("[/]");
            String[] fullDataArrayImportantInfo = importantInfo.getStrFullData().split("[/]");

            if (fullDataArrayApplicant.length > 0) {
                for (String s : fullDataArrayApplicant) {
                    mFullApplicantInfoArray.add(s);
                }
            }

            if (fullDataArrayImportantInfo.length > 0) {
                for (String s : fullDataArrayImportantInfo) {
                    mImportantInfoArray.add(s);
                }
            }

            String color = mApplicationsInfo.getStrBackground();

            mDetailApplicantAdapter = new DetailApplicantAdapter(color, mFullApplicantInfoArray, mImportantInfoArray);
            mDetailApplicantAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mDetailApplicantAdapter);
        }

        return view;
    }
}
