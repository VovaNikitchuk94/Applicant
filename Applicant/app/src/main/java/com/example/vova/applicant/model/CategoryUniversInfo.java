package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstans.CategoryUniversTable;

public class CategoryUniversInfo extends BaseEntity implements Parcelable{

    private long mLongCityId;
    private String mStrCategoryUniversName;
    private String mStrCategoryUniversLink;

    public CategoryUniversInfo(long longCityId, String strCategoryUniversName,
                               String strCategoryUniversLink) {
        mLongCityId = longCityId;
        mStrCategoryUniversName = strCategoryUniversName;
        mStrCategoryUniversLink = strCategoryUniversLink;
    }

    public CategoryUniversInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_ID)));
        mLongCityId = cursor.getLong(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID));
        mStrCategoryUniversName = cursor.getString(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_NAME));
        mStrCategoryUniversLink = cursor.getString(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_LINK));
    }

    private CategoryUniversInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongCityId = parcel.readLong();
        mStrCategoryUniversName = parcel.readString();
        mStrCategoryUniversLink = parcel.readString();
    }

    public long getLongCityId() {
        return mLongCityId;
    }

    public void setLongCityId(long longCityId) {
        mLongCityId = longCityId;
    }

    public String getStrCategoryUniversName() {
        return mStrCategoryUniversName;
    }

    public void setStrCategoryUniversName(String strCategoryUniversName) {
        mStrCategoryUniversName = strCategoryUniversName;
    }

    public String getStrCategoryUniversLink() {
        return mStrCategoryUniversLink;
    }

    public void setStrCategoryUniversLink(String strCategoryUniversLink) {
        mStrCategoryUniversLink = strCategoryUniversLink;
    }



    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID, getLongCityId());
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_NAME, getStrCategoryUniversName());
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_LINK, getStrCategoryUniversLink());
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
    }

    public static final Parcelable.Creator<CategoryUniversInfo> CREATOR
            = new Parcelable.Creator<CategoryUniversInfo>() {
        public CategoryUniversInfo createFromParcel(Parcel in) {
            return new CategoryUniversInfo(in);
        }

        public CategoryUniversInfo[] newArray(int size) {
            return new CategoryUniversInfo[size];
        }
    };
}
