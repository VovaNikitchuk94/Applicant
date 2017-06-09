package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ImportantApplicantInfoDBWrapper;

import java.util.ArrayList;

public class ImportantApplicantInfoEngine extends BaseEngine {

    public ImportantApplicantInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<ImportantInfo> getImportantInfoById(long nId) {
        Log.d("My", "ImportantApplicantInfoEngine ----------> getImportantInfoById");
        ImportantApplicantInfoDBWrapper importantApplicantInfoDBWrapper = new ImportantApplicantInfoDBWrapper(getContext());
        return importantApplicantInfoDBWrapper.getImportantInfoById(nId);
    }

    public void addImportantInfo(ImportantInfo importantInfo) {
        ImportantApplicantInfoDBWrapper importantApplicantInfoDBWrapper = new ImportantApplicantInfoDBWrapper(getContext());
        importantApplicantInfoDBWrapper.addImportantInfo(importantInfo);
    }

}
