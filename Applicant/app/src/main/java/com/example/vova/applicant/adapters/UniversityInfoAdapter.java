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
import com.example.vova.applicant.UniversityInfo;

import java.util.ArrayList;

/**
 * Created by vovan on 10.04.2017.
 */

public class UniversityInfoAdapter extends ArrayAdapter<UniversityInfo> {

    public UniversityInfoAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @NonNull ArrayList<UniversityInfo> universityInfos) {
        super(context, 0, universityInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_university_info, parent, false);
        }

        UniversityInfo universityInfo = getItem(position);

        TextView typeUniversityInfo = (TextView)listItemView.findViewById(R.id.textViewTypeUniversityInfo);
        typeUniversityInfo.setText(universityInfo.getStrInfoType());

        TextView dataUniversityInfo = (TextView)listItemView.findViewById(R.id.textViewDataUniversityInfo);
        dataUniversityInfo.setText(universityInfo.getStrInfoData());


        return listItemView;
    }
}
