package com.example.vova.applicant.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView textViewNameSpeciality = (TextView) view.findViewById(R.id.textSpecialityFragmentDetailApplicant);

        TextView textViewNumber = (TextView) view.findViewById(R.id.textNumberApplicantFragmentImportantInfoApplicant);
        TextView textViewFullName = (TextView) view.findViewById(R.id.textFullNameApplicantFragmentImportantInfoApplicant);
        TextView textViewPriority = (TextView) view.findViewById(R.id.textPriorityApplicantFragmentImportantInfoApplicant);
        TextView textViewTotalScore = (TextView) view.findViewById(R.id.textTotalScoreApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkDocument = (TextView) view.findViewById(R.id.textMarkDocumentApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkTest = (TextView) view.findViewById(R.id.textMarkTestApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkExam = (TextView) view.findViewById(R.id.textMarkExamApplicantFragmentImportantInfoApplicant);
        TextView textViewExtraPoint = (TextView) view.findViewById(R.id.textExtraPointsApplicantFragmentImportantInfoApplicant);
        TextView textViewOriginalDocoment = (TextView) view.findViewById(R.id.textOriginalDocumentApplicantFragmentImportantInfoApplicant);

        if (mApplicationsInfo != null) {
            ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getContext());
            ArrayList<ImportantInfo> importantInfos = importantApplicantInfoEngine.getImportantInfoById(mApplicationsInfo.getLongSpecialityId());
            Log.d("My", "importantInfos.size() -> " + importantInfos.size());

            textViewNameOfUniversity.setText(mApplicationsInfo.getStrUniversity());
            textViewNameSpeciality.setText(mApplicationsInfo.getStrSpeciality());

            if (!importantInfos.isEmpty()) {
                if (importantInfos.size() == 9) {
                    textViewNumber.setText(importantInfos.get(0).getStrName());
                    textViewFullName.setText(importantInfos.get(1).getStrName());
                    textViewPriority.setText(importantInfos.get(2).getStrName());
                    textViewTotalScore.setText(importantInfos.get(3).getStrName());
                    textViewMarkDocument.setText(importantInfos.get(4).getStrName());
                    textViewMarkTest.setText(importantInfos.get(5).getStrName());
                    textViewMarkExam.setText(importantInfos.get(6).getStrName());
                    textViewExtraPoint.setText(importantInfos.get(7).getStrName());
                    textViewOriginalDocoment.setText(importantInfos.get(8).getStrName());
                } else {
                    textViewNumber.setText(importantInfos.get(0).getStrName());
                    textViewFullName.setText(importantInfos.get(1).getStrName());
                    textViewTotalScore.setText(importantInfos.get(2).getStrName());
                    textViewMarkDocument.setText(importantInfos.get(3).getStrName());
                    textViewMarkTest.setText(importantInfos.get(4).getStrName());
                    textViewMarkExam.setText(importantInfos.get(5).getStrName());
                    textViewExtraPoint.setText(importantInfos.get(6).getStrName());
                    textViewOriginalDocoment.setText(importantInfos.get(7).getStrName());
                }


                Log.d("My", "textViewTotalScore -> " + textViewTotalScore.getText());
            }
        }

        return view;
    }
}
