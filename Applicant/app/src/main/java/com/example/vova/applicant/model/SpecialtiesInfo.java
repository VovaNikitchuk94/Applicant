package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.SpecialitiesTable;

public class SpecialtiesInfo extends BaseEntity implements Parcelable {

    private long mLongTimeFormId;
    private long mLongDegree;
    private String mStrSpecialty;
    private String mStrApplications;
//    private String mStrAccepted;
//    private String mStrRecommended;
//    private String mStrLicensedOrder;
//    private String mStrVolumeOrder;
    private String mStrOrder;
    private String mStrExam;
    private String mStrLink;
    private String mStrDateLastUpdate;
    private int mIsFavorite;

//    public SpecialtiesInfo(long longTimeFormId, long longDegree, String strSpecialty, String strApplications,
//                           String strAccepted, String strRecommended, String strLicensedOrder, String strVolumeOrder,
//                           String strExam, String strLink, String strDateLastUpdate, int isFavorite) {
//        mLongTimeFormId = longTimeFormId;
//        mLongDegree = longDegree;
//        mStrSpecialty = strSpecialty;
//        mStrApplications = strApplications;
//        mStrAccepted = strAccepted;
//        mStrRecommended = strRecommended;
//        mStrLicensedOrder = strLicensedOrder;
//        mStrVolumeOrder = strVolumeOrder;
//        mStrExam = strExam;
//        mStrLink = strLink;
//        mStrDateLastUpdate = strDateLastUpdate;
//        mIsFavorite = isFavorite;
//    }


    public SpecialtiesInfo(long longTimeFormId, long longDegree, String strSpecialty, String strApplications,
                           String strOrder, String strExam, String strLink, String strDateLastUpdate, int isFavorite) {
        mLongTimeFormId = longTimeFormId;
        mLongDegree = longDegree;
        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrOrder = strOrder;
        mStrExam = strExam;
        mStrLink = strLink;
        mStrDateLastUpdate = strDateLastUpdate;
        mIsFavorite = isFavorite;
    }

    public SpecialtiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID)));
        mLongTimeFormId = cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID));
        mLongDegree = cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DEGREE));
        mStrSpecialty = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY));
        mStrApplications = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION));
//        mStrAccepted = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED));
//        mStrRecommended = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_RECOMMENDED));
//        mStrLicensedOrder = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LICENSED_ORDERS));
//        mStrVolumeOrder = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_VOLUME_ORDERS));
        mStrOrder = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ORDERS));
        mStrExam = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_EXAMS));
        mStrLink = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DATE_UPDATE));
        mIsFavorite = cursor.getInt(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_FAVORITE));
    }

    private SpecialtiesInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongTimeFormId = parcel.readLong();
        mLongDegree = parcel.readLong();
        mStrSpecialty = parcel.readString();
        mStrApplications = parcel.readString();
//        mStrAccepted = parcel.readString();
//        mStrRecommended = parcel.readString();
//        mStrLicensedOrder = parcel.readString();
//        mStrVolumeOrder = parcel.readString();
        mStrOrder = parcel.readString();
        mStrExam = parcel.readString();
        mStrLink = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
        mIsFavorite = parcel.readInt();
    }

    public String getStrSpecialty() {
        return mStrSpecialty;
    }

    public String getStrApplications() {
        return mStrApplications;
    }

//    public String getStrAccepted() {
//        return mStrAccepted;
//    }

    public String getStrLink() {
        return mStrLink;
    }

    public long getLongTimeFormId() {
        return mLongTimeFormId;
    }

    public long getLongDegree() {
        return mLongDegree;
    }

//    public String getStrRecommended() {
//        return mStrRecommended;
//    }

//    public String getStrLicensedOrder() {
//        return mStrLicensedOrder;
//    }

//    public String getStrVolumeOrder() {
//        return mStrVolumeOrder;
//    }


    public String getStrOrder() {
        return mStrOrder;
    }

    public String getStrExam() {
        return mStrExam;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    public int getIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        mIsFavorite = isFavorite;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID, getLongTimeFormId());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DEGREE, getLongDegree());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY, getStrSpecialty());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION, getStrApplications());
//        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED, getStrAccepted());
//        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_RECOMMENDED, getStrRecommended());
//        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LICENSED_ORDERS, getStrLicensedOrder());
//        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_VOLUME_ORDERS, getStrVolumeOrder());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ORDERS, getStrOrder());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_EXAMS, getStrExam());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK, getStrLink());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_FAVORITE, getIsFavorite());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongTimeFormId);
        dest.writeLong(mLongDegree);
        dest.writeString(mStrSpecialty);
        dest.writeString(mStrApplications);
//        dest.writeString(mStrAccepted);
//        dest.writeString(mStrRecommended);
//        dest.writeString(mStrLicensedOrder);
//        dest.writeString(mStrVolumeOrder);
        dest.writeString(mStrOrder);
        dest.writeString(mStrExam);
        dest.writeString(mStrLink);
        dest.writeString(mStrDateLastUpdate);
        dest.writeInt(mIsFavorite);
    }

    public static final Parcelable.Creator<SpecialtiesInfo> CREATOR
            = new Parcelable.Creator<SpecialtiesInfo>() {
        public SpecialtiesInfo createFromParcel(Parcel in) {
            return new SpecialtiesInfo(in);
        }

        public SpecialtiesInfo[] newArray(int size) {
            return new SpecialtiesInfo[size];
        }
    };
}
