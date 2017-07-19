package com.example.vova.applicant.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.LegendInfo;

import java.util.ArrayList;

public class LegendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<LegendInfo> mLegendInfos = new ArrayList<>();

    public LegendAdapter(ArrayList<LegendInfo> legendInfos) {
        mLegendInfos = legendInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewLegendInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_legend_info, parent, false);
        viewHolder = new LegendInfoViewHolder(viewLegendInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LegendInfoViewHolder legendInfoViewHolder = (LegendInfoViewHolder) holder;
        final LegendInfo legendInfo = mLegendInfos.get(position);
        Log.d("My", "LegendAdapter onBindViewHolder legendInfo.getStrNameLegend() -> " + legendInfo.getStrNameLegend());
        Log.d("My", "LegendAdapter onBindViewHolder legendInfo.getStrBackgroundLegend() -> " + legendInfo.getStrBackgroundLegend() + " position >>>> " + position);

        legendInfoViewHolder.mTextViewLegendName.setBackgroundColor(Color.parseColor(legendInfo.getStrBackgroundLegend()));
        legendInfoViewHolder.mTextViewLegendName.setText(legendInfo.getStrNameLegend());
        legendInfoViewHolder.mTextViewLegendDetail.setText(legendInfo.getStrDetailLegend());
    }

    @Override
    public int getItemCount() {
        Log.d("My", "mLegendInfos.size() ->" + mLegendInfos.size());
        return mLegendInfos.size();
    }

    public class LegendInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView mTextViewLegendName;
        TextView mTextViewLegendDetail;

        public LegendInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            mTextViewLegendName = (TextView) rootView.findViewById(R.id.textViewTextLegendItemName);
            mTextViewLegendDetail = (TextView) rootView.findViewById(R.id.textViewTextLegendItemDetail);
        }
    }
}
