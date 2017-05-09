package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.AboutUniversityInfo;

import java.util.ArrayList;

public class AboutUniversityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AboutUniversityInfo> mAboutUniversityInfos = new ArrayList<>();
    private  OnClickAboutUniversityItem mUniversityItem = null;

    public AboutUniversityAdapter(ArrayList<AboutUniversityInfo> arrDetailUnivers) {
        mAboutUniversityInfos = arrDetailUnivers;
    }

    public void setOnClickListenerAdapter(OnClickAboutUniversityItem onClickListenerAdapter) {
        mUniversityItem = onClickListenerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewAboutUniversity = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_about_university, parent, false);
        viewHolder = new AboutUniversityViewHolder(viewAboutUniversity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AboutUniversityViewHolder aboutUniversityViewHolder = (AboutUniversityViewHolder) holder;
        final AboutUniversityInfo universityInfo = mAboutUniversityInfos.get(position);
        aboutUniversityViewHolder.typeTextView.setText(universityInfo.getStrAboutUniversType());
        aboutUniversityViewHolder.dataTextView.setText(universityInfo.getStrAboutUniversData());
        aboutUniversityViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUniversityItem != null){
                    mUniversityItem.onClickAboutUniversityItem(universityInfo.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("My", "AboutUniversityAdapter mAboutUniversityInfos.size() -> " + mAboutUniversityInfos.size());
        return mAboutUniversityInfos.size();
    }

    private class AboutUniversityViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView typeTextView;
        TextView dataTextView;

        AboutUniversityViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            typeTextView = (TextView) itemView.findViewById(R.id.textViewTextTypeAboutUniversity);
            dataTextView = (TextView) itemView.findViewById(R.id.textViewTextDataAboutUniversity);
        }
    }

    public interface OnClickAboutUniversityItem {
        void onClickAboutUniversityItem(long nIdItem);
    }
}
