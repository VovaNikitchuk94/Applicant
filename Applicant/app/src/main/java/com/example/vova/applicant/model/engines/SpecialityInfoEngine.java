package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.SpecialityDBWrapper;

import java.util.ArrayList;

public class SpecialityInfoEngine extends BaseEngine {

    public SpecialityInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<SpecialtiesInfo> getAllSpecialities() {
        SpecialityDBWrapper specialityDBWrapper = new SpecialityDBWrapper(getContext());
        return specialityDBWrapper.getAllSpecialities();
    }

    public ArrayList<SpecialtiesInfo> getAllSpecialitiesById(long nId) {
        SpecialityDBWrapper specialityDBWrapper = new SpecialityDBWrapper(getContext());
        return specialityDBWrapper.getAllSpecialitiesById(nId);
    }

    public void addSpeciality(SpecialtiesInfo specialtiesInfo) {
        SpecialityDBWrapper specialityDBWrapper = new SpecialityDBWrapper(getContext());
        specialityDBWrapper.addSpeciality(specialtiesInfo);
    }

    public SpecialtiesInfo getSpecialityById(long nId){
        SpecialityDBWrapper specialityDBWrapper = new SpecialityDBWrapper(getContext());
        return specialityDBWrapper.getSpecialityById(nId);
    }

}
