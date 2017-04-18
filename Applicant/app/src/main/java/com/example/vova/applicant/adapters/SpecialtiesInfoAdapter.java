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
import com.example.vova.applicant.model.SpecialtiesInfo;

import java.util.List;

/**
 * Created by vovan on 17.04.2017.
 */

public class SpecialtiesInfoAdapter extends ArrayAdapter<SpecialtiesInfo> {
    public SpecialtiesInfoAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List<SpecialtiesInfo> specialtiesInfos) {
        super(context, resource, specialtiesInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_specialties_info, parent, false);
        }

        SpecialtiesInfo specialtiesInfo = getItem(position);

        TextView specialtyTextView = (TextView)convertView.findViewById(R.id.textViewSpecialtySpecialtiesInfo);
        TextView applicationsTextView = (TextView)convertView.findViewById(R.id.textViewApplicationsSpecialtiesInfo);
        TextView acceptedTextView = (TextView)convertView.findViewById(R.id.textViewAcceptedSpecialtiesInfo);
        TextView amountTextView = (TextView)convertView.findViewById(R.id.textViewAmountSpecialtiesInfo);

        try {
            specialtyTextView.setText(specialtiesInfo.getStrSpecialty());
            applicationsTextView.setText(specialtiesInfo.getStrApplications());
            acceptedTextView.setText(specialtiesInfo.getStrAccepted());
            amountTextView.setText(specialtiesInfo.getStrAmount());
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return convertView;
    }
}
