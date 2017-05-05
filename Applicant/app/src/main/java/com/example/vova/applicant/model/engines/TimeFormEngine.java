package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.TimeFormDBWrapper;

import java.util.ArrayList;

public class TimeFormEngine extends BaseEngine {

    public TimeFormEngine(Context context) {
        super(context);
    }

    public ArrayList<TimeFormInfo> getAllTimeForms() {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        return timeFormDBWrapper.getAllTimeForms();
    }

    public void addTimeForm(TimeFormInfo timeFormInfo) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        timeFormDBWrapper.addTimeForm(timeFormInfo);
    }

    public TimeFormInfo getTimeFormById(long nId){
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        TimeFormInfo timeFormInfo = timeFormDBWrapper.getTimeFormById(nId);
        return timeFormInfo;
    }
}
