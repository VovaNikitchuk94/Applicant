package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.UniversityInfo;
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
        Log.d("My", "UniversitiesInfoEngine ----------> getAllUniversitiesById");
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversitiesById(nId);
    }

    public ArrayList<UniversityInfo> getAllUniversitiesByDegree(long nId, String category) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversitiesByDegree(nId, category);
    }

    public ArrayList<UniversityInfo> getAllDegree() {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllDegree();
    }

    public void updateUniversity(UniversityInfo universityInfo) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.updateUniversity(universityInfo);
    }

    public UniversityInfo getUniversityById(long nId) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getUniversityById(nId);
    }

    public ArrayList<UniversityInfo> getAllUniversitiesBySearchString(long nId, String category, String strSearch) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllUniversitiesBySearchString(nId, category, strSearch);
    }

    public void updateAllUniversities(ArrayList<UniversityInfo> universitiesItems) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.updateAllItems(universitiesItems);
    }

    public void addAllItems(ArrayList<UniversityInfo> universitiesItems) {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        universitiesInfoDBWrapper.addAllItems(universitiesItems);
    }

    public ArrayList<UniversityInfo> getAllFavoriteUniversities() {
        UniversitiesInfoDBWrapper universitiesInfoDBWrapper = new UniversitiesInfoDBWrapper(getContext());
        return universitiesInfoDBWrapper.getAllFavoriteUniversities();
    }

    }