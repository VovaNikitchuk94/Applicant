package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.CitiesInfoDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.UniversitiesInfoDBWrapper;

import java.util.ArrayList;

public class UniversitiesInfoEngine extends BaseEngine {

    public UniversitiesInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<UniversityInfo> getAllUniversities() {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversities();
    }

    public ArrayList<UniversityInfo> getAllUniversitiesById(long nId) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversitiesById(nId);
    }

    public void updateUniversity(UniversityInfo universityInfo) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.updateUniversity(universityInfo);
    }

    public void addUniversity(UniversityInfo universityInfo) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.addUniversity(universityInfo);
    }

    public UniversityInfo getUniversityById(long nId){
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getUniversityById(nId);
    }

}
