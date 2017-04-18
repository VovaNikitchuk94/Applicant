package com.example.vova.applicant.model;

/**
 * Created by vovan on 17.04.2017.
 */

public class SpecialtiesInfo {

    private String mStrSpecialty;
    private String mStrApplications;
    private String mStrAccepted;
    private String mStrAmount;

    public SpecialtiesInfo(String strSpecialty, String strApplications,
                           String strAccepted, String strAmount) {

        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrAccepted = strAccepted;
        mStrAmount = strAmount;
    }

    public String getStrSpecialty() {
        return mStrSpecialty;
    }

    public String getStrApplications() {
        return mStrApplications;
    }

    public String getStrAccepted() {
        return mStrAccepted;
    }

    public String getStrAmount() {
        return mStrAmount;
    }
}
