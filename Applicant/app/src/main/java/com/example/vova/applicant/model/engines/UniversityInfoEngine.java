package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.CitiesInfoDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.UniversitiesInfoDBWrapper;

import java.util.ArrayList;

public class UniversityInfoEngine extends BaseEngine {

    public UniversityInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<UniversityInfo> getAllUniversities() {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversities();
    }

    public void addUniversity(UniversityInfo universityInfo) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.addUniversity(universityInfo);
    }

    public UniversityInfo getUniversityById(long nId){
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        UniversityInfo universityInfo = universitiesInfoDBWrapper.getUniversityById(nId);
        return universityInfo;
    }

}
