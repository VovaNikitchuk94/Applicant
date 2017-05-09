package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.os.Parcelable;


public abstract class BaseEntity {

    private long mId = -1;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public abstract ContentValues getContentValues();
}
