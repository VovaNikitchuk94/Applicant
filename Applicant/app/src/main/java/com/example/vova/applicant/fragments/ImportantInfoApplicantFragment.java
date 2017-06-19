package com.example.vova.applicant.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.activities.DetailApplicantPagerActivity;
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.engines.ImportantApplicantInfoEngine;

import java.util.ArrayList;

public class ImportantInfoApplicantFragment extends Fragment {

    private ApplicationsInfo mApplicationsInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationsInfo = DetailApplicantPagerActivity.mInfo;
        Log.d("My", "mApplicationsInfo ->" + mApplicationsInfo);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_important_info_applicant, container, false);
        Log.d("My", "onCreateView ->");

        TextView textViewNameOfUniversity = (TextView) view.findViewById(R.id.textNameOfUniversityFragmentDetailApplicant);
        TextView textViewSpeciality = (TextView) view.findViewById(R.id.textSpecialityFragmentDetailApplicant);
        TextView textViewSpecialization = (TextView) view.findViewById(R.id.textSpecializationFragmentDetailApplicant);
        TextView textViewFaculty = (TextView) view.findViewById(R.id.textFacultyFragmentDetailApplicant);
        TextView textViewTimeForm = (TextView) view.findViewById(R.id.textTimeFormFragmentDetailApplicant);
        TextView textViewLastTimeUpdate = (TextView) view.findViewById(R.id.textLastTimeUpdateFragmentDetailApplicant);

        //TODO распарсить и правильно отобразить техтвьюшки с важной информацией

        if (mApplicationsInfo != null) {

            ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getContext());
            ImportantInfo importantInfo = importantApplicantInfoEngine.getImportantInfoById(mApplicationsInfo.getLongSpecialityId());

            if (importantInfo != null) {
                textViewNameOfUniversity.setText(importantInfo.getStrUniversityName());
                textViewSpeciality.setText(importantInfo.getStrSpeciality());
                textViewSpecialization.setText(importantInfo.getStrSpecialization());
                textViewFaculty.setText(importantInfo.getStrFaculty());
                textViewTimeForm.setText(importantInfo.getStrTimeForm());
                textViewLastTimeUpdate.setText(importantInfo.getStrLastTimeUpdate());
            }
        }

        return view;
    }
}
