package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ApplicationDBWrapper;

import java.util.ArrayList;

public class ApplicationInfoEngine extends BaseEngine {

    public ApplicationInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<ApplicationsInfo> getAllApplicantions() {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicantions();
    }

    public ArrayList<ApplicationsInfo> getAllApplicantionsById(long nId) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicantionsById(nId);
    }

    public void addApplication(ApplicationsInfo applicationsInfo) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.addItem(applicationsInfo);
    }

    public ApplicationsInfo getApplicationById(long nId){
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getApplicationById(nId);
    }

}
