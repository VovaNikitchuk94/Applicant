package com.example.vova.applicant.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.SpecialtiesInfo;

import java.util.ArrayList;

public class SpecialitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SpecialtiesInfo> mSpecialtiesInfos = new ArrayList<>();
    private OnClickSpecialityItem mOnClickSpecialityItem = null;
    private Context mContext;

    public SpecialitiesAdapter(ArrayList<SpecialtiesInfo> arrDetailUnivers) {
        mSpecialtiesInfos = arrDetailUnivers;
    }

    public void setOnClickSpecialityItem(OnClickSpecialityItem specialityItem){
        mOnClickSpecialityItem = specialityItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        mContext = parent.getContext();

        View viewSpecialityInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_specialties_info, parent, false);
        viewHolder = new SpecialitiesInfoViewHolder(viewSpecialityInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SpecialitiesInfoViewHolder specialitiesInfoViewHolder = (SpecialitiesInfoViewHolder) holder;
        final SpecialtiesInfo specialtiesInfo = mSpecialtiesInfos.get(position);
        specialitiesInfoViewHolder.specialtyTextView.setText(specialtiesInfo.getStrSpecialty());
        specialitiesInfoViewHolder.applicationsTextView.setText(specialtiesInfo.getStrApplications());
//        specialitiesInfoViewHolder.acceptedTextView.setText(specialtiesInfo.getStrAccepted());
//        specialitiesInfoViewHolder.recommendedTextView.setText(specialtiesInfo.getStrRecommended());
//        specialitiesInfoViewHolder.licenseOrderTextView.setText(specialtiesInfo.getStrLicensedOrder());
//        specialitiesInfoViewHolder.volumeOrderTextView.setText(specialtiesInfo.getStrVolumeOrder());
        specialitiesInfoViewHolder.orderTextView.setText(specialtiesInfo.getStrOrder());
        //TODO обработать ошибку когда не пустые items определяються как пустые
        //TODO обработать ошибку когда в пустые списки дублируються записи из предыдущего item
        if (specialtiesInfo.getStrLink().isEmpty()) {
            int emptyColor = ContextCompat.getColor(mContext, R.color.md_grey_400);
            specialitiesInfoViewHolder.specialtyTextView.setTextColor(emptyColor);
            specialitiesInfoViewHolder.applicationsTextView.setTextColor(emptyColor);
//            specialitiesInfoViewHolder.acceptedTextView.setTextColor(emptyColor);
//            specialitiesInfoViewHolder.recommendedTextView.setTextColor(emptyColor);
//            specialitiesInfoViewHolder.licenseOrderTextView.setTextColor(emptyColor);
//            specialitiesInfoViewHolder.volumeOrderTextView.setTextColor(emptyColor);
            specialitiesInfoViewHolder.orderTextView.setTextColor(emptyColor);
        }


        specialitiesInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickSpecialityItem != null){
                    mOnClickSpecialityItem.onClickSpecialityItem(specialtiesInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSpecialtiesInfos.size();
    }

    public class SpecialitiesInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView specialtyTextView;
        TextView applicationsTextView;
//        TextView acceptedTextView;
//        TextView recommendedTextView;
//        TextView licenseOrderTextView;
//        TextView volumeOrderTextView;
        TextView orderTextView;

        public SpecialitiesInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            specialtyTextView = (TextView) itemView.findViewById(R.id.textViewSpecialtySpecialtiesInfo);
            applicationsTextView = (TextView) itemView.findViewById(R.id.textViewApplicationsSpecialtiesInfo);
//            acceptedTextView = (TextView) itemView.findViewById(R.id.textViewmStrAcceptedSpecialtiesInfo);
//            recommendedTextView = (TextView) itemView.findViewById(R.id.textViewmStrRecommendedSpecialtiesInfo);
//            licenseOrderTextView = (TextView) itemView.findViewById(R.id.textViewmmStrLicensedOrderSpecialtiesInfo);
//            volumeOrderTextView = (TextView) itemView.findViewById(R.id.textViewmmStrVolumeOrderSpecialtiesInfo);
            orderTextView = (TextView) itemView.findViewById(R.id.textViewOrderSpecialtiesInfo);
        }
    }

    public interface OnClickSpecialityItem{
        void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo);
    }
}
