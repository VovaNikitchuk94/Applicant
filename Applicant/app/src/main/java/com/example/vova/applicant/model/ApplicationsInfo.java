package com.example.vova.applicant.model;

/**
 * Created by vovan on 16.04.2017.
 */

public class ApplicationsInfo {

    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantTotalScores;
//    private String mStrApplicantBDO;
//    private String mStrApplicantZNO;

//    public ApplicationsInfo(String strApplicantNumber, String strApplicantName,
//                            String strApplicantTotalScores, String strApplicantBDO, String strApplicantZNO) {
//        mStrApplicantNumber = strApplicantNumber;
//        mStrApplicantName = strApplicantName;
//        mStrApplicantTotalScores = strApplicantTotalScores;
//        mStrApplicantBDO = strApplicantBDO;
//        mStrApplicantZNO = strApplicantZNO;
//    }


    public ApplicationsInfo(String strApplicantNumber, String strApplicantName, String strApplicantTotalScores) {
        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantTotalScores = strApplicantTotalScores;
    }

    public String getStrApplicantNumber() {
        return mStrApplicantNumber;
    }

    public String getStrApplicantName() {
        return mStrApplicantName;
    }

    public String getStrApplicantTotalScores() {
        return mStrApplicantTotalScores;
    }

//    public String getStrApplicantBDO() {
//        return mStrApplicantBDO;
//    }
//
//    public String getStrApplicantZNO() {
//        return mStrApplicantZNO;
//    }
}
