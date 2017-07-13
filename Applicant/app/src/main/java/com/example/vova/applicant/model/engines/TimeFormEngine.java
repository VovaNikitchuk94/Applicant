package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

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

    public ArrayList<TimeFormInfo> getAllTimeFormsById(long nId) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        return timeFormDBWrapper.getAllTimeFormsById(nId);
    }

    public void updateTimeForm(TimeFormInfo timeFormInfo) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        timeFormDBWrapper.updateTimeForm(timeFormInfo);
    }

    public void addTimeForm(TimeFormInfo timeFormInfo) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        timeFormDBWrapper.addTimeForm(timeFormInfo);
    }

    public TimeFormInfo getTimeFormById(long nId) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        return timeFormDBWrapper.getTimeFormById(nId);
    }

    public void addAllTimeForms(ArrayList<TimeFormInfo> timeFormsItems) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        timeFormDBWrapper.addAllItems(timeFormsItems);
    }

    public void updateAllTimeForms(ArrayList<TimeFormInfo> timeFormsItems) {
        TimeFormDBWrapper timeFormDBWrapper = new TimeFormDBWrapper(getContext());
        timeFormDBWrapper.updateAllItems(timeFormsItems);
    }
}
