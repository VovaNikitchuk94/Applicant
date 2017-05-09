package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
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

    public SpecialitiesAdapter(ArrayList<SpecialtiesInfo> arrDetailUnivers) {
        mSpecialtiesInfos = arrDetailUnivers;
    }

    public void setOnClickSpecialityItem(OnClickSpecialityItem specialityItem){
        mOnClickSpecialityItem = specialityItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

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
        specialitiesInfoViewHolder.acceptedTextView.setText(specialtiesInfo.getStrAccepted());
        specialitiesInfoViewHolder.amountTextView.setText(specialtiesInfo.getStrAmount());
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
        TextView acceptedTextView;
        TextView amountTextView;

        public SpecialitiesInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
             specialtyTextView = (TextView) itemView.findViewById(R.id.textViewSpecialtySpecialtiesInfo);
             applicationsTextView = (TextView) itemView.findViewById(R.id.textViewApplicationsSpecialtiesInfo);
             acceptedTextView = (TextView) itemView.findViewById(R.id.textViewAcceptedSpecialtiesInfo);
             amountTextView = (TextView) itemView.findViewById(R.id.textViewAmountSpecialtiesInfo);
        }
    }

    public interface OnClickSpecialityItem{
        void onClickSpecialityItem(SpecialtiesInfo specialtiesInfo);
    }
}
