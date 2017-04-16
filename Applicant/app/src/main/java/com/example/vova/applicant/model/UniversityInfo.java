package com.example.vova.applicant.model;

public class UniversityInfo {

    private String mStrInfoType;
    private String mStrInfoData;

    public UniversityInfo(String strInfoType, String strInfoData) {
        mStrInfoType = strInfoType;
        mStrInfoData = strInfoData;
    }

    public String getStrInfoType() {
        return mStrInfoType;
    }

    public String getStrInfoData() {
        return mStrInfoData;
    }
}
