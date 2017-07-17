package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ApplicationsDBWrapper;

import java.util.ArrayList;

public class ApplicationsEngine extends BaseEngine {

    public ApplicationsEngine(Context context) {
        super(context);
    }

//    public ArrayList<ApplicationsInfo> getAllApplicantions() {
//        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
//        return applicationsDBWrapper.getAllApplications();
//    }

    public ArrayList<ApplicationsInfo> getAllApplicationsById(long nId) {
        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
        return applicationsDBWrapper.getAllApplicationsById(nId);
    }

//    public void updateApplicant(ApplicationsInfo applicationsInfo) {
//        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
//        applicationsDBWrapper.updateApplicant(applicationsInfo);
//    }
//
//    public void addApplication(ApplicationsInfo applicationsInfo) {
//        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
//        applicationsDBWrapper.addItem(applicationsInfo);
//    }

    public ApplicationsInfo getApplicationById(long nId) {
        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
        return applicationsDBWrapper.getApplicationById(nId);
    }

    public ArrayList<ApplicationsInfo> getAllApplicationsBySearchString(long nId, String strSearch) {
        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
        return applicationsDBWrapper.getAllApplicationsBySearchString(nId, strSearch);
    }

    public void addAllApplications(ArrayList<ApplicationsInfo> applicationsItems) {
        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
        applicationsDBWrapper.addAllItems(applicationsItems);
    }

    public void updateAllApplications(ArrayList<ApplicationsInfo> applicationsItems) {
        ApplicationsDBWrapper applicationsDBWrapper = new ApplicationsDBWrapper(getContext());
        applicationsDBWrapper.updateAllItems(applicationsItems);
    }
}
