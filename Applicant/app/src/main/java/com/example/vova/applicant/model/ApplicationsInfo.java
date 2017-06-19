package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationTable;

public class ApplicationsInfo extends BaseEntity implements Parcelable {

    private long mLongSpecialityId;
//    private String mStrUniversity;
//    private String mStrSpeciality;
    private String mStrApplicantInfo;

    //Detail applicant info
    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantPriority;
    private String mStrApplicantTotalScores;
    private String mStrApplicantMarkDocument;
    private String mStrApplicantMarkTest;
    private String mStrApplicantMarkExam;
    private String mStrApplicantExtraPoints;
    private String mStrApplicantOriginalDocument;

    private String mStrApplicantLink;
    private String mStrBackground;

    public ApplicationsInfo(long longSpecialityId,
                            String strApplicantInfo, String strApplicantNumber,
                            String strApplicantName, String strApplicantPriority,
                            String strApplicantTotalScores, String strApplicantMarkDocument,
                            String strApplicantMarkTest, String strApplicantMarkExam,
                            String strApplicantExtraPoints, String strApplicantOriginalDocument,
                            String strApplicantLink, String strBackground) {

        mLongSpecialityId = longSpecialityId;
//        mStrUniversity = strUniversity;
//        mStrSpeciality = strSpeciality;
        mStrApplicantInfo = strApplicantInfo;

        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantPriority = strApplicantPriority;
        mStrApplicantTotalScores = strApplicantTotalScores;
        mStrApplicantMarkDocument = strApplicantMarkDocument;
        mStrApplicantMarkTest = strApplicantMarkTest;
        mStrApplicantMarkExam = strApplicantMarkExam;
        mStrApplicantExtraPoints = strApplicantExtraPoints;
        mStrApplicantOriginalDocument = strApplicantOriginalDocument;

        mStrApplicantLink = strApplicantLink;
        mStrBackground = strBackground;
    }

    public ApplicationsInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID));
//        mStrUniversity = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_UNIVERSITY));
//        mStrSpeciality = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY));
        mStrApplicantInfo = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_FIELD_INFO));

        mStrApplicantNumber = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER));
        mStrApplicantName = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME));
        mStrApplicantPriority = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_PRIORITY));
        mStrApplicantTotalScores = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE));
        mStrApplicantMarkDocument = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_DOCUMENT));
        mStrApplicantMarkTest = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_TEST));
        mStrApplicantMarkExam = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_EXAM));
        mStrApplicantExtraPoints = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_EXTRA_POINTS));
        mStrApplicantOriginalDocument = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ORIGINAL_DOCUMENT));

        mStrApplicantLink = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK));
        mStrBackground = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_BACKGROUND));
    }

    public ApplicationsInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongSpecialityId = parcel.readLong();
//        mStrUniversity = parcel.readString();
//        mStrSpeciality = parcel.readString();
        mStrApplicantInfo = parcel.readString();

        mStrApplicantNumber = parcel.readString();
        mStrApplicantName = parcel.readString();
        mStrApplicantPriority = parcel.readString();
        mStrApplicantTotalScores = parcel.readString();
        mStrApplicantMarkDocument = parcel.readString();
        mStrApplicantMarkTest = parcel.readString();
        mStrApplicantMarkExam = parcel.readString();
        mStrApplicantExtraPoints = parcel.readString();
        mStrApplicantOriginalDocument = parcel.readString();

        mStrApplicantLink = parcel.readString();
        mStrBackground = parcel.readString();
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

    public String getStrApplicantLink() {
        return mStrApplicantLink;
    }

    public long getLongSpecialityId() {
        return mLongSpecialityId;
    }

//    public String getStrUniversity() {
//        return mStrUniversity;
//    }

//    public String getStrSpeciality() {
//        return mStrSpeciality;
//    }

    public String getStrApplicantInfo() {
        return mStrApplicantInfo;
    }

    public String getStrBackground() {
        return mStrBackground;
    }

    public String getStrApplicantPriority() {
        return mStrApplicantPriority;
    }

    public String getStrApplicantMarkDocument() {
        return mStrApplicantMarkDocument;
    }

    public String getStrApplicantMarkTest() {
        return mStrApplicantMarkTest;
    }

    public String getStrApplicantMarkExam() {
        return mStrApplicantMarkExam;
    }

    public String getStrApplicantExtraPoints() {
        return mStrApplicantExtraPoints;
    }

    public String getStrApplicantOriginalDocument() {
        return mStrApplicantOriginalDocument;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());
//        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_UNIVERSITY, getStrUniversity());
//        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY, getStrSpeciality());
        values.put(ApplicationTable.Cols.APPLICATION_FIELD_INFO, getStrApplicantInfo());

        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER, getStrApplicantNumber());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME, getStrApplicantName());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_PRIORITY, getStrApplicantPriority());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE, getStrApplicantTotalScores());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_DOCUMENT, getStrApplicantMarkDocument());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_TEST, getStrApplicantMarkTest());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_MARK_EXAM, getStrApplicantMarkExam());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_EXTRA_POINTS, getStrApplicantExtraPoints());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ORIGINAL_DOCUMENT, getStrApplicantOriginalDocument());

        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK, getStrApplicantLink());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_BACKGROUND, getStrBackground());

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongSpecialityId);
//        dest.writeString(mStrUniversity);
//        dest.writeString(mStrSpeciality);
        dest.writeString(mStrApplicantInfo);

        dest.writeString(mStrApplicantNumber);
        dest.writeString(mStrApplicantName);
        dest.writeString(mStrApplicantPriority);
        dest.writeString(mStrApplicantTotalScores);
        dest.writeString(mStrApplicantMarkDocument);
        dest.writeString(mStrApplicantMarkTest);
        dest.writeString(mStrApplicantMarkExam);
        dest.writeString(mStrApplicantExtraPoints);
        dest.writeString(mStrApplicantOriginalDocument);

        dest.writeString(mStrApplicantLink);
        dest.writeString(mStrBackground);
    }

    public static final Parcelable.Creator<ApplicationsInfo> CREATOR
            = new Parcelable.Creator<ApplicationsInfo>() {
        public ApplicationsInfo createFromParcel(Parcel in) {
            return new ApplicationsInfo(in);
        }

        public ApplicationsInfo[] newArray(int size) {
            return new ApplicationsInfo[size];
        }
    };
}
