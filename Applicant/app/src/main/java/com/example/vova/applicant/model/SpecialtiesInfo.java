package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstans.SpecialitiesTable;

public class SpecialtiesInfo extends BaseEntity implements Parcelable {

    private long mLongDetailUNVId;
    private String mStrSpecialty;
    private String mStrApplications;
    private String mStrAccepted;
    private String mStrAmount;
    private String mStrLink;

    public SpecialtiesInfo(long detailUnvId, String strSpecialty, String strApplications,
                           String strAccepted, String strAmount, String link) {
        mLongDetailUNVId = detailUnvId;
        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrAccepted = strAccepted;
        mStrAmount = strAmount;
        mStrLink = link;
    }

    public SpecialtiesInfo(long detailUnvId, String strSpecialty, String strApplications,
                           String strAmount, String link) {
        mLongDetailUNVId = detailUnvId;
        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrAmount = strAmount;
        mStrLink = link;
    }

    public SpecialtiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID)));
        mLongDetailUNVId = cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID));
        mStrSpecialty = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY));
        mStrApplications = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION));
        mStrAccepted = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED));
        mStrAmount = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_AMOUNT));
        mStrLink = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK));
    }

    private SpecialtiesInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongDetailUNVId = parcel.readLong();
        mStrSpecialty = parcel.readString();
        mStrApplications = parcel.readString();
        mStrAccepted = parcel.readString();
        mStrAmount = parcel.readString();
        mStrLink = parcel.readString();
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

    public String getStrLink() {
        return mStrLink;
    }

    public long getLongDetailUNVId() {
        return mLongDetailUNVId;
    }

    public void setLongDetailUNVId(long longDetailUNVId) {
        mLongDetailUNVId = longDetailUNVId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID, getLongDetailUNVId());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY, getStrSpecialty());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION, getStrApplications());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED, getStrAccepted());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_AMOUNT, getStrAmount());
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK, getStrLink());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongDetailUNVId);
        dest.writeString(mStrSpecialty);
        dest.writeString(mStrApplications);
        dest.writeString(mStrAccepted);
        dest.writeString(mStrAmount);
        dest.writeString(mStrLink);

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
