package com.example.vova.applicant;

/**
 * Created by vovan on 10.04.2017.
 */

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

    public void setStrInfoType(String strInfoType) {
        mStrInfoType = strInfoType;
    }

    public void setStrInfoData(String strInfoData) {
        mStrInfoData = strInfoData;
    }
}
