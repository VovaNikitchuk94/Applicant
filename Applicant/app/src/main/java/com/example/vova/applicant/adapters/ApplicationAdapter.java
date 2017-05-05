package com.example.vova.applicant.adapters;

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
//    private OnClickSpecialityItem mOnClickSpecialityItem = null;

    public ApplicationAdapter(ArrayList<ApplicationsInfo> arrayList) {
        mApplicationsInfos = arrayList;
    }

//    public void setOnClickSpecialityItem(OnClickSpecialityItem specialityItem){
//        mOnClickSpecialityItem = specialityItem;
//    }

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
        applicationsInfoViewHolder.numberTextView.setText(applicationsInfo.getStrApplicantNumber());
        applicationsInfoViewHolder.nameTextView.setText(applicationsInfo.getStrApplicantName());
        applicationsInfoViewHolder.totalScoreTextView.setText(applicationsInfo.getStrApplicantTotalScores());
//        applicationsInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnClickSpecialityItem != null){
//                    mOnClickSpecialityItem.onClickSpecialityItem(specialtiesInfo.getId());
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mApplicationsInfos.size();
    }

    public class ApplicationsInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView numberTextView;
        TextView nameTextView;
        TextView totalScoreTextView;

        public ApplicationsInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            numberTextView = (TextView) itemView.findViewById(R.id.textViewSequenceNumberApplicantInfo);
            nameTextView = (TextView) itemView.findViewById(R.id.textViewNameApplicantsApplicantInfo);
            totalScoreTextView = (TextView) itemView.findViewById(R.id.textViewCompetitionScoresApplicantInfo);
        }
    }

//    public interface OnClickSpecialityItem{
//        void onClickSpecialityItem(long nIdSpeciality);
//    }
}
