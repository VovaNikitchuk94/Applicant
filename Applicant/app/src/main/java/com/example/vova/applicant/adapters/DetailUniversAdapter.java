package com.example.vova.applicant.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

    private ArrayList<DetailUniverInfo> mDetailUniverInfos = new ArrayList<>();
    private OnClickDetailUniversItem mOnClickDetailUniversItem = null;
    private Context mContext;

    public DetailUniversAdapter(ArrayList<DetailUniverInfo> arrDetailUnivers) {
        mDetailUniverInfos = arrDetailUnivers;
    }

    public void setOnClickDetailUniversItem(OnClickDetailUniversItem onClickDetailUniversItem){
        mOnClickDetailUniversItem = onClickDetailUniversItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        mContext = parent.getContext();

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
        if (detailUniverInfo.getStrDetailText().contains("(0)") && mContext != null) {
            detailUniversInfoViewHolder.nameTextView.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_400));
        }
//        citiesInfoViewHolder.linkTextView.setText(citiesInfo.getStrTimeFormLink());
        detailUniversInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickDetailUniversItem != null){
                    mOnClickDetailUniversItem.onClickDetailUniversItem(detailUniverInfo);
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
        void onClickDetailUniversItem(DetailUniverInfo detailUniverInfo);
    }
}
