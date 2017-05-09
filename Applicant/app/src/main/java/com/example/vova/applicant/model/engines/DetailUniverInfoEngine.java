package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.DetailUniverDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.UniversitiesInfoDBWrapper;

import java.util.ArrayList;

public class DetailUniverInfoEngine extends BaseEngine {

    public DetailUniverInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<DetailUniverInfo> getAllDetailUnivers() {
        DetailUniverDBWrapper detailUniverDBWrapper = new DetailUniverDBWrapper(getContext());
        return detailUniverDBWrapper.getAllDetailUnivers();
    }

    public ArrayList<DetailUniverInfo> getAllDetailUniversById(long nId) {
        DetailUniverDBWrapper detailUniverDBWrapper = new DetailUniverDBWrapper(getContext());
        return detailUniverDBWrapper.getAllDetailUniversById(nId);
    }

    public void addDetailUniver(DetailUniverInfo detailUniverInfo) {
        DetailUniverDBWrapper detailUniverDBWrapper = new DetailUniverDBWrapper(getContext());
        detailUniverDBWrapper.addDetailUniver(detailUniverInfo);
    }

    public DetailUniverInfo getDetailUniverById(long nId){
        DetailUniverDBWrapper detailUniverDBWrapper = new DetailUniverDBWrapper(getContext());
        return detailUniverDBWrapper.getDetailUniverById(nId);
    }

}
