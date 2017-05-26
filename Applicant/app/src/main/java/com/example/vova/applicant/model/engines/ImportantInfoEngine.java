package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.ImportantInfoDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.TimeFormDBWrapper;

import java.util.ArrayList;

public class ImportantInfoEngine extends BaseEngine {

    public ImportantInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<ImportantInfo> getAllInfos() {
        ImportantInfoDBWrapper importantInfoDBWrapper = new ImportantInfoDBWrapper(getContext());
        return importantInfoDBWrapper.getAllInfos();
    }

    public void addInfos(ImportantInfo importantInfo) {
        ImportantInfoDBWrapper importantInfoDBWrapper = new ImportantInfoDBWrapper(getContext());
        importantInfoDBWrapper.addInfos(importantInfo);
    }

}
