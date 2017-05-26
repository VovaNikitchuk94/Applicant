package com.example.vova.applicant.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
        //set background
        applicationsInfoViewHolder.rootView.setBackgroundColor(Color.parseColor(applicationsInfo.getStrBackground()));
//        applicationsInfoViewHolder.numberTextView.setBackgroundColor(Color.parseColor(applicationsInfo.getStrBackground()));
//        applicationsInfoViewHolder.nameTextView.setBackgroundColor(Color.parseColor(applicationsInfo.getStrBackgroundSecond()));
//        applicationsInfoViewHolder.totalScoreTextView.setBackgroundColor(Color.parseColor(applicationsInfo.getStrBackground()));
        //set text
        applicationsInfoViewHolder.numberTextView.setText(applicationsInfo.getStrApplicantNumber());
        applicationsInfoViewHolder.nameTextView.setText(applicationsInfo.getStrApplicantName());
        applicationsInfoViewHolder.totalScoreTextView.setText(applicationsInfo.getStrApplicantTotalScores());
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
        TextView totalScoreTextView;

        ApplicationsInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            numberTextView = (TextView) itemView.findViewById(R.id.textViewSequenceNumberApplicantInfo);
            nameTextView = (TextView) itemView.findViewById(R.id.textViewNameApplicantsApplicantInfo);
            totalScoreTextView = (TextView) itemView.findViewById(R.id.textViewCompetitionScoresApplicantInfo);
        }
    }

    public interface OnClickApplicationItem {
        void onClickApplicationItem(ApplicationsInfo applicationsInfo);
    }
}
