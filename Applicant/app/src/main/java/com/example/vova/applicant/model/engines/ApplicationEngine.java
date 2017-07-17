package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.ApplicationInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ApplicationDBWrapper;

import java.util.ArrayList;

public class ApplicationEngine extends BaseEngine {

    public ApplicationEngine(Context context) {
        super(context);
    }

    public void addAllApplication(ArrayList<ApplicationInfo> applicationItem) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        applicationDBWrapper.addAllItems(applicationItem);
    }

//    public void updateAllApplication(ArrayList<ApplicationInfo> applicationItem) {
//        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
//        applicationDBWrapper.updateAllItems(applicationItem);
//    }

    public ArrayList<ApplicationInfo> getAllApplicationById(long nId) {
        ApplicationDBWrapper applicationDBWrapper = new ApplicationDBWrapper(getContext());
        return applicationDBWrapper.getAllApplicationById(nId);
    }
}
