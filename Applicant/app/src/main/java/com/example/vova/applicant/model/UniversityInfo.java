package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityTable;

public class UniversityInfo extends BaseEntity implements Parcelable {

    private long mLongCityId;
    private String mStrCategoryUniversName;
    private String mStrCategoryUniversLink;
    private String mStrUniversityName;
    private String mStrUniversityLink;

    public UniversityInfo(long longCityId, String strCategoryUniversName, String strCategoryUniversLink,
                          String strUniversityName, String strUniversityLink) {
        mLongCityId = longCityId;
        mStrCategoryUniversName = strCategoryUniversName;
        mStrCategoryUniversLink = strCategoryUniversLink;
        mStrUniversityName = strUniversityName;
        mStrUniversityLink = strUniversityLink;
    }

    public UniversityInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_ID)));
        mLongCityId = cursor.getLong(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID));
        mStrCategoryUniversName = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_NAME));
        mStrCategoryUniversLink = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_LINK));
        mStrUniversityName = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME));
        mStrUniversityLink = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK));
    }

    private UniversityInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongCityId = parcel.readLong();
        mStrCategoryUniversName = parcel.readString();
        mStrCategoryUniversLink = parcel.readString();
        mStrUniversityName = parcel.readString();
        mStrUniversityLink = parcel.readString();
    }

    public String getStrUniversityName() {
        return mStrUniversityName;
    }

    public String getStrUniversityLink() {
        return mStrUniversityLink;
    }

    public long getLongCityId() {
        return mLongCityId;
    }

    public String getStrCategoryUniversName() {
        return mStrCategoryUniversName;
    }


    public String getStrCategoryUniversLink() {
        return mStrCategoryUniversLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID, getLongCityId());
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_NAME, getStrCategoryUniversName());
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_LINK, getStrCategoryUniversLink());
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME, getStrUniversityName());
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK, getStrUniversityLink());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongCityId);
        dest.writeString(mStrCategoryUniversName);
        dest.writeString(mStrCategoryUniversLink);
        dest.writeString(mStrUniversityName);
        dest.writeString(mStrUniversityLink);
    }

    public static final Parcelable.Creator<UniversityInfo> CREATOR
            = new Parcelable.Creator<UniversityInfo>() {
        public UniversityInfo createFromParcel(Parcel in) {
            return new UniversityInfo(in);
        }

        public UniversityInfo[] newArray(int size) {
            return new UniversityInfo[size];
        }
    };
}
