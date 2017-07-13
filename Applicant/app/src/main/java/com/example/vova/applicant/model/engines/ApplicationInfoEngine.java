package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ApplicationDBWrapper;

import java.util.ArrayList;

public class ApplicationInfoEngine extends BaseEngine {

    public ApplicationInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<ApplicationsInfo> getAllApplicantions() {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplications();
    }

    public ArrayList<ApplicationsInfo> getAllApplicantionsById(long nId) {
        Log.d("My", "ApplicationInfoEngine ----------> getAllApplicationsById");
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicationsById(nId);
    }

    public void updateApplicant(ApplicationsInfo applicationsInfo) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.updateApplicant(applicationsInfo);
    }

    public void addApplication(ApplicationsInfo applicationsInfo) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.addItem(applicationsInfo);
    }

    public ApplicationsInfo getApplicationById(long nId) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getApplicationById(nId);
    }

    public ArrayList<ApplicationsInfo> getAllApplicationsBySearchString(long nId, String strSearch) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicationsBySearchString(nId, strSearch);
    }

    public void addAllApplications(ArrayList<ApplicationsInfo> applicationsItems) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.addAllItems(applicationsItems);
    }

    public void updateAllApplications(ArrayList<ApplicationsInfo> applicationsItems) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.updateAllItems(applicationsItems);
    }
}
