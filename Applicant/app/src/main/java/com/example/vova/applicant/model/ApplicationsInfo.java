package com.example.vova.applicant.model;

/**
 * Created by vovan on 16.04.2017.
 */

public class ApplicationsInfo {

    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantScores;
    private String mStrApplicantBDO;
    private String mStrApplicantZNO;

    public ApplicationsInfo(String strApplicantNumber, String strApplicantName, String strApplicantScores, String strApplicantBDO, String strApplicantZNO) {
        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantScores = strApplicantScores;
        mStrApplicantBDO = strApplicantBDO;
        mStrApplicantZNO = strApplicantZNO;
    }

    public String getStrApplicantNumber() {
        return mStrApplicantNumber;
    }

    public String getStrApplicantName() {
        return mStrApplicantName;
    }

    public String getStrApplicantScores() {
        return mStrApplicantScores;
    }

    public String getStrApplicantBDO() {
        return mStrApplicantBDO;
    }

    public String getStrApplicantZNO() {
        return mStrApplicantZNO;
    }
}
