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
import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.engines.ApplicationInfoEngine;
import com.example.vova.applicant.model.engines.ImportantApplicantInfoEngine;

import java.util.ArrayList;

public class DetailApplicantFragment extends Fragment {

    public static final String ARG_APPLICANT_ID = "ARG_APPLICANT_ID";

    private ApplicationsInfo mApplicationsInfo;

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

        long detailApplicantId = (long) getArguments().getSerializable(ARG_APPLICANT_ID);
        ApplicationInfoEngine engine = new ApplicationInfoEngine(getContext());
        mApplicationsInfo = engine.getApplicationById(detailApplicantId);

        Log.d("My", "DetailApplicantFragment onCreate detailApplicantId -> " + detailApplicantId);
        Log.d("My", "DetailApplicantFragment onCreate mApplicationsInfo -> " + mApplicationsInfo.toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_applicant, container, false);

        //TODO правильно отобразить данные абитуры: что б совпадали столбцы + красивый внешний вид

//        TextView textViewNameOfUniversity = (TextView) view.findViewById(R.id.textNameOfUniversityFragmentDetailApplicant);
//        TextView textViewNameSpeciality = (TextView) view.findViewById(R.id.textSpecialityFragmentDetailApplicant);
//        TextView textViewDateOfLastUpdate = (TextView) view.findViewById(R.id.textDateOfLastUpdateFragmentDetailApplicant);
//        TextView textViewDetailAboutApplicant = (TextView) view.findViewById(R.id.textDetailAboutApplicantFragmentDetailApplicant);

        //set legend textViews
        ImportantApplicantInfoEngine importantApplicantInfoEngine = new ImportantApplicantInfoEngine(getContext());
        ImportantInfo importantInfos = importantApplicantInfoEngine.getImportantInfoById(mApplicationsInfo.getLongSpecialityId());
        Log.d("My", "importantInfos.getId() -> " + importantInfos.getId());

        TextView textViewNumber = (TextView) view.findViewById(R.id.textNumberApplicantFragmentImportantInfoApplicant);
        TextView textViewFullName = (TextView) view.findViewById(R.id.textFullNameApplicantFragmentImportantInfoApplicant);
        TextView textViewPriority = (TextView) view.findViewById(R.id.textPriorityApplicantFragmentImportantInfoApplicant);
        TextView textViewTotalScore = (TextView) view.findViewById(R.id.textTotalScoreApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkDocument = (TextView) view.findViewById(R.id.textMarkDocumentApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkTest = (TextView) view.findViewById(R.id.textMarkTestApplicantFragmentImportantInfoApplicant);
        TextView textViewMarkExam = (TextView) view.findViewById(R.id.textMarkExamApplicantFragmentImportantInfoApplicant);
        TextView textViewExtraPoint = (TextView) view.findViewById(R.id.textExtraPointsApplicantFragmentImportantInfoApplicant);
        TextView textViewOriginalDocument = (TextView) view.findViewById(R.id.textOriginalDocumentApplicantFragmentImportantInfoApplicant);

        //find textView
        TextView numberTextView = (TextView) view.findViewById(R.id.textNumberApplicantFragmentDetailApplicant);
        TextView nameTextView = (TextView) view.findViewById(R.id.textFullNameApplicantFragmentDetailApplicant);
        TextView priorityTextView = (TextView) view.findViewById(R.id.textPriorityApplicantFragmentDetailApplicant);
        TextView totalScoreTextView = (TextView) view.findViewById(R.id.textTotalScoreApplicantFragmentDetailApplicant);
        TextView markDocumentTextView = (TextView) view.findViewById(R.id.textMarkDocumentApplicantFragmentDetailApplicant);
        TextView markTestTextView = (TextView) view.findViewById(R.id.textMarkTestApplicantFragmentDetailApplicant);
        TextView markExamTextView = (TextView) view.findViewById(R.id.textMarkExamApplicantFragmentDetailApplicant);
        TextView extraPointsTextView = (TextView) view.findViewById(R.id.textExtraPointsApplicantFragmentDetailApplicant);
        TextView originalDocumentTextView = (TextView) view.findViewById(R.id.textOriginalDocumentApplicantFragmentDetailApplicant);




        if (mApplicationsInfo != null){
//            textViewNameOfUniversity.setText(mApplicationsInfo.getStrUniversity());
//            textViewNameSpeciality.setText(mApplicationsInfo.getStrSpeciality());
//            textViewDetailAboutApplicant.setText(mApplicationsInfo.getStrApplicantInfo());

            //set text in textView
            numberTextView.setText(mApplicationsInfo.getStrApplicantNumber());
            nameTextView.setText(mApplicationsInfo.getStrApplicantName());
            priorityTextView.setText(mApplicationsInfo.getStrApplicantPriority());
            totalScoreTextView.setText(mApplicationsInfo.getStrApplicantTotalScores());
            markDocumentTextView.setText(mApplicationsInfo.getStrApplicantMarkDocument());
            markTestTextView.setText(mApplicationsInfo.getStrApplicantMarkTest());
            markExamTextView.setText(mApplicationsInfo.getStrApplicantMarkExam());
            extraPointsTextView.setText(mApplicationsInfo.getStrApplicantExtraPoints());
            originalDocumentTextView.setText(mApplicationsInfo.getStrApplicantOriginalDocument());

            //set legend text in textView
            textViewNumber.setText(importantInfos.getStrNumber());
            textViewFullName.setText(importantInfos.getStrName());
            textViewPriority.setText(importantInfos.getStrPriority());
            textViewTotalScore.setText(importantInfos.getStrTotalScores());
            textViewMarkDocument.setText(importantInfos.getStrMarkDocument());
            textViewMarkTest.setText(importantInfos.getStrMarkTest());
            textViewMarkExam.setText(importantInfos.getStrMarkExam());
            textViewExtraPoint.setText(importantInfos.getStrExtraPoints());
            textViewOriginalDocument.setText(importantInfos.getStrOriginalDocument());
//            textViewOriginalDocument.setText(importantInfos.get(8).getStrName());

//            Log.d("My", "DetailApplicantFragment onCreateView" + mApplicationsInfo.getStrUniversity());
//            Log.d("My", "DetailApplicantFragment mApplicationsInfo.getStrSpeciality()" + mApplicationsInfo.getStrSpeciality());
//            Log.d("My", "DetailApplicantFragment mApplicationsInfo.getStrApplicantInfo()" +mApplicationsInfo.getStrApplicantInfo());
        }

        Log.d("My", "DetailApplicantFragment onCreateView");
        return view;
    }
}
