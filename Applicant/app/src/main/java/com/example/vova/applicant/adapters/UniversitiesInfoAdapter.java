package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.UniversityInfo;

import java.util.ArrayList;

public class UniversitiesInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<UniversityInfo> mUniversityInfos;
    private OnClickUniversityItem mOnClickUniversityItem = null;

    public UniversitiesInfoAdapter(ArrayList<UniversityInfo> arrUniversities) {
        mUniversityInfos = arrUniversities;
    }

    public void setOnClickUniversityItem(OnClickUniversityItem onClickUniversityItem){
        mOnClickUniversityItem = onClickUniversityItem;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewUniversityInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_university_info, parent, false);
        viewHolder = new UniversitiesInfoViewHolder(viewUniversityInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UniversitiesInfoViewHolder universitiesInfoViewHolder = (UniversitiesInfoViewHolder) holder;
        final UniversityInfo universityInfo = mUniversityInfos.get(position);
        universitiesInfoViewHolder.nameTextView.setText(universityInfo.getStrUniversityName());
//        citiesInfoViewHolder.linkTextView.setText(citiesInfo.getStrTimeFormLink());
        universitiesInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickUniversityItem != null){
                    mOnClickUniversityItem.onClickUniversityItem(universityInfo.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUniversityInfos.size();
    }

    public class UniversitiesInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView nameTextView;
//        TextView linkTextView;

        public UniversitiesInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.textViewTypeUniversityInfo);
//            linkTextView = (TextView) itemView.findViewById(R.id.textViewCitiesInfoLinkCitiesInfoItem);
        }
    }

    public interface OnClickUniversityItem{
        void onClickUniversityItem(long nIdUniversity);
    }
}
