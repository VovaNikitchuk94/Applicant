package com.example.vova.applicant.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.ApplicationsInfo;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ApplicationsInfo> mApplicationsInfos = new ArrayList<>();
    private OnClickApplicationItem mOnClickApplicationItem = null;

    public ApplicationAdapter(ArrayList<ApplicationsInfo> arrayList) {
        mApplicationsInfos = arrayList;
    }

    public void setOnClickApplicationItem(OnClickApplicationItem applicationItem){
        mOnClickApplicationItem = applicationItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewApplicationInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_application_info, parent, false);
        viewHolder = new ApplicationsInfoViewHolder(viewApplicationInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ApplicationsInfoViewHolder applicationsInfoViewHolder = (ApplicationsInfoViewHolder) holder;
        final ApplicationsInfo applicationsInfo = mApplicationsInfos.get(position);
        applicationsInfoViewHolder.rootView.setBackgroundColor(Color.parseColor(applicationsInfo.getStrBackground()));
        applicationsInfoViewHolder.numberTextView.setText(applicationsInfo.getStrApplicantNumber());
        applicationsInfoViewHolder.nameTextView.setText(applicationsInfo.getStrApplicantName());
        if (applicationsInfo.getStrApplicantMarkDocument().isEmpty()) {
            if (applicationsInfo.getStrPriority().isEmpty()) {
                applicationsInfoViewHolder.markDocumentTotalScoreTextView.setText(applicationsInfo.getStrApplicantTotalScores());
            } else {
                applicationsInfoViewHolder.totalScoreOrPriorityTextView.setText(applicationsInfo.getStrPriority());
                applicationsInfoViewHolder.markDocumentTotalScoreTextView.setText(applicationsInfo.getStrApplicantTotalScores());
            }
        } else {
            applicationsInfoViewHolder.totalScoreOrPriorityTextView.setText(applicationsInfo.getStrApplicantTotalScores());
            applicationsInfoViewHolder.markDocumentTotalScoreTextView.setText(applicationsInfo.getStrApplicantMarkDocument());
        }
        //set clickListener
        applicationsInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickApplicationItem != null){
                    mOnClickApplicationItem.onClickApplicationItem(applicationsInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApplicationsInfos.size();
    }

    private class ApplicationsInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView numberTextView;
        TextView nameTextView;
        TextView totalScoreOrPriorityTextView;
        TextView markDocumentTotalScoreTextView;
        TextView ZNOScoreOrOrigDocumentTextView;

        ApplicationsInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            numberTextView = (TextView) itemView.findViewById(R.id.textViewSequenceNumberApplicantInfo);
            nameTextView = (TextView) itemView.findViewById(R.id.textViewNameApplicantsApplicantInfo);
            totalScoreOrPriorityTextView = (TextView) itemView.findViewById(R.id.textViewCompetitionScoresApplicantInfo);
            markDocumentTotalScoreTextView = (TextView) itemView.findViewById(R.id.textViewBDOScoreApplicantsApplicantInfo);
        }
    }

    public interface OnClickApplicationItem {
        void onClickApplicationItem(ApplicationsInfo applicationsInfo);
    }
}
