package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.model.SpecialtiesInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ApplicationDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.SpecialityDBWrapper;

import java.util.ArrayList;

public class ApplicationInfoEngine extends BaseEngine {

    public ApplicationInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<ApplicationsInfo> getAllApplicantions() {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicantions();
    }

    public void addApplication(ApplicationsInfo applicationsInfo) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.addApplication(applicationsInfo);
    }

    public ApplicationsInfo getApplicationById(long nId){
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        ApplicationsInfo applicationsInfo = applicationDBWrapper.getApplicationById(nId);
        return applicationsInfo;
    }

}
