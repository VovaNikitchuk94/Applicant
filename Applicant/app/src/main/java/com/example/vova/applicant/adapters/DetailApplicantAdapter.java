package com.example.vova.applicant.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;

import java.util.ArrayList;

public class DetailApplicantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mFullApplicantInfoArray = new ArrayList<>();
    private ArrayList<String> mImportantInfoArray = new ArrayList<>();
    private String mStrColor;

    public DetailApplicantAdapter(String color, ArrayList<String> fullApplicantInfo, ArrayList<String> importantInfo ) {
        mStrColor = color;
        mFullApplicantInfoArray = fullApplicantInfo;
        mImportantInfoArray = importantInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewCitiesInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_detail_applicant, parent, false);
        viewHolder = new DetailApplicantInfoViewHolder(viewCitiesInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetailApplicantInfoViewHolder detailApplicantInfoViewHolder = (DetailApplicantInfoViewHolder) holder;

        detailApplicantInfoViewHolder.itemView.setBackgroundColor(Color.parseColor(mStrColor));
        String fullApplicationData = mFullApplicantInfoArray.get(position);
        String importantData = mImportantInfoArray.get(position);
        detailApplicantInfoViewHolder.conventionTextView.setText(importantData);
        detailApplicantInfoViewHolder.definitionTextView.setText(fullApplicationData);
    }

    @Override
    public int getItemCount() {
        return mFullApplicantInfoArray.size();
    }

    private class DetailApplicantInfoViewHolder extends RecyclerView.ViewHolder {

        TextView conventionTextView;
        TextView definitionTextView;

        DetailApplicantInfoViewHolder(View itemView) {
            super(itemView);

            conventionTextView = (TextView) itemView.findViewById(R.id.textViewConventionsDetailApplicant);
            definitionTextView = (TextView) itemView.findViewById(R.id.textViewDefinitionDetailApplicant);
        }
    }
}