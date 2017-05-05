package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.TimeFormInfo;

import java.util.ArrayList;

public class TimeFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TimeFormInfo> mTimeFormInfos = new ArrayList<>();
    private OnClickTimeFormItem mOnClickTimeFormItem = null;

    public TimeFormAdapter(ArrayList<TimeFormInfo> formInfos) {
        mTimeFormInfos = formInfos;
    }

    public void setOnClickTimeFormItem(OnClickTimeFormItem formItem){
        mOnClickTimeFormItem = formItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View viewCitiesInfo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_time_forrm_info, parent, false);
        viewHolder = new TimeFormInfoViewHolder(viewCitiesInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimeFormInfoViewHolder timeFormInfoViewHolder = (TimeFormInfoViewHolder) holder;
        final TimeFormInfo timeFormInfo = mTimeFormInfos.get(position);
        timeFormInfoViewHolder.nameTextView.setText(timeFormInfo.getStrTimeFormName());
        timeFormInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickTimeFormItem != null){
                    mOnClickTimeFormItem.onClickTimeFormItem(timeFormInfo.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeFormInfos.size();
    }

    private class TimeFormInfoViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView nameTextView;

        public TimeFormInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.textViewTimeFormNameTimeFormInfoItem);
        }
    }

    public interface OnClickTimeFormItem{
        void onClickTimeFormItem(long nIdTimeForm);
    }
}
