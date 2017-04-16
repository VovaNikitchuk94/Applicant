package com.example.vova.applicant.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.ApplicationsInfo;

import java.util.List;

/**
 * Created by vovan on 16.04.2017.
 */

public class ApplicationInfoAdapter extends ArrayAdapter<ApplicationsInfo> {

    public ApplicationInfoAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List<ApplicationsInfo> applicationsInfos) {
        super(context, resource, applicationsInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_application_info, parent, false);
        }

        ApplicationsInfo applicationsInfo = getItem(position);

        TextView applicantNumberTextView = (TextView)convertView.findViewById(R.id.textViewSequenceNumberApplicantInfo);
        TextView applicantNameTextView = (TextView)convertView.findViewById(R.id.textViewNameApplicantsApplicantInfo);
        TextView applicantScoresTextView = (TextView)convertView.findViewById(R.id.textViewCompetitionScoresApplicantInfo);
        TextView applicantBDOTextView = (TextView)convertView.findViewById(R.id.textViewBDOScoreApplicantsApplicantInfo);
        TextView applicantZNOTextView = (TextView)convertView.findViewById(R.id.textViewZNOScoreApplicantsApplicantInfo);

        try {
            applicantNumberTextView.setText(applicationsInfo.getStrApplicantNumber());
            applicantNameTextView.setText(applicationsInfo.getStrApplicantName());
            applicantScoresTextView.setText(applicationsInfo.getStrApplicantScores());
            applicantBDOTextView.setText(applicationsInfo.getStrApplicantBDO());
            applicantZNOTextView.setText(applicationsInfo.getStrApplicantZNO());
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return convertView;
    }
}
