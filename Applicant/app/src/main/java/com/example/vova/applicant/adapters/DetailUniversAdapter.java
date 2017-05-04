package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;

import java.util.ArrayList;

public class DetailUniversAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DetailUniverInfo> mDetailUniverInfos;
    private OnClickDetailUniversItem mOnClickDetailUniversItem = null;

    public DetailUniversAdapter(ArrayList<DetailUniverInfo> arrDetailUnivers) {
        mDetailUniverInfos = arrDetailUnivers;
    }

    public void setOnClickDetailUniversItem(OnClickDetailUniversItem OnClickDetailUniversItem){
        mOnClickDetailUniversItem = OnClickDetailUniversItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewDetailUniversityInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_detail_univers_info, parent, false);
        viewHolder = new DetailUniversInfoViewHolder(viewDetailUniversityInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetailUniversInfoViewHolder detailUniversInfoViewHolder = (DetailUniversInfoViewHolder) holder;
        final DetailUniverInfo detailUniverInfo = mDetailUniverInfos.get(position);
        detailUniversInfoViewHolder.nameTextView.setText(detailUniverInfo.getStrDetailText());
//        citiesInfoViewHolder.linkTextView.setText(citiesInfo.getStrCityLink());
        detailUniversInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickDetailUniversItem != null){
                    mOnClickDetailUniversItem.onClickDetailUniversItem(detailUniverInfo.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailUniverInfos.size();
    }

    public class DetailUniversInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView nameTextView;
//        TextView linkTextView;

        public DetailUniversInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.textViewTextDetailUniversInfo);
//            linkTextView = (TextView) itemView.findViewById(R.id.textViewCitiesInfoLinkCitiesInfoItem);
        }
    }

    public interface OnClickDetailUniversItem{
        void onClickDetailUniversItem(long nIddetailUnivers);
    }
}
