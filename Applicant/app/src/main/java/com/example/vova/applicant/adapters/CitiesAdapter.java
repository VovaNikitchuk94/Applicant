package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.CitiesInfo;

import java.util.ArrayList;

public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CitiesInfo> mCitiesInfos = new ArrayList<>();
    private OnClickCityItem mOnClickCityItem = null;

    public CitiesAdapter(ArrayList<CitiesInfo> arrCities) {
        mCitiesInfos = arrCities;
    }

    public void setOnClickCityInfoItem(OnClickCityItem onClickCityInfoItem){
        mOnClickCityItem = onClickCityInfoItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewCitiesInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cities_info, parent, false);
        viewHolder = new CitiesInfoViewHolder(viewCitiesInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CitiesInfoViewHolder citiesInfoViewHolder = (CitiesInfoViewHolder) holder;
        final CitiesInfo citiesInfo = mCitiesInfos.get(position);
        citiesInfoViewHolder.nameTextView.setText(citiesInfo.getStrCityName());
        citiesInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickCityItem != null){
                    mOnClickCityItem.onClickCityItem(citiesInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCitiesInfos.size();
    }

    private class CitiesInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView nameTextView;

        CitiesInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.textViewCitiesInfoNameCitiesInfoItem);
        }
    }

    public interface OnClickCityItem{
        void onClickCityItem(CitiesInfo citiesInfo);
    }
}