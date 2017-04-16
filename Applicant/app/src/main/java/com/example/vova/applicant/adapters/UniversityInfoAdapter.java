package com.example.vova.applicant.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.UniversityInfo;

import java.util.ArrayList;

public class UniversityInfoAdapter extends ArrayAdapter<UniversityInfo> {

    public UniversityInfoAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @NonNull ArrayList<UniversityInfo> universityInfos) {
        super(context, 0, universityInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_university_info, parent, false);
//            holder = new ViewHolder();
//            holder.typeUniversityInfoTextView = (TextView)convertView.findViewById(R.id.textViewTypeUniversityInfo);
//            holder.dataUniversityInfoTextView = (TextView)convertView.findViewById(R.id.textViewDataUniversityInfo);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
        }

        UniversityInfo universityInfo = getItem(position);

        TextView typeUniversityInfoTextView = (TextView)convertView.findViewById(R.id.textViewTypeUniversityInfo);
        TextView dataUniversityInfoTextView = (TextView) convertView.findViewById(R.id.textViewDataUniversityInfo);

        typeUniversityInfoTextView.setText(universityInfo.getStrInfoType());
        dataUniversityInfoTextView.setText(universityInfo.getStrInfoData());

//        holder.typeUniversityInfoTextView.setText(universityInfo.getStrInfoType());
//        holder.dataUniversityInfoTextView.setText(universityInfo.getStrInfoData());

        return convertView;
    }

//    private class ViewHolder {
//        TextView typeUniversityInfoTextView;
//        TextView dataUniversityInfoTextView;
//    }
}
