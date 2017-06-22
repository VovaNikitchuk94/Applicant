package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.CategoryUniversTable;

public class CategoryUniversInfo extends BaseEntity implements Parcelable{

    private long mLongCityId;
    private String mStrCategoryName;
    private String mStrDateLastUpdate;

    public CategoryUniversInfo(long longCityId, String strCategoryName, String strDateLastUpdate) {
        mLongCityId = longCityId;
        mStrCategoryName = strCategoryName;
        mStrDateLastUpdate = strDateLastUpdate;
    }

    public CategoryUniversInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_ID)));
        mLongCityId = cursor.getLong(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID));
        mStrCategoryName = cursor.getString(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_NAME));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_DATE_UPDATE));
    }

    private CategoryUniversInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongCityId = parcel.readLong();
        mStrCategoryName = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
    }

    public long getLongCityId() {
        return mLongCityId;
    }

    public String getStrCategoryName() {
        return mStrCategoryName;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID, getLongCityId());
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_NAME, getStrCategoryName());
        values.put(CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
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
        dest.writeString(mStrCategoryName);
        dest.writeString(mStrDateLastUpdate);
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
