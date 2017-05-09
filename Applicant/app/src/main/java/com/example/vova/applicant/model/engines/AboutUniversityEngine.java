package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.AboutUniversityInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.AboutUniversityDBWrapper;

import java.util.ArrayList;

public class AboutUniversityEngine extends BaseEngine {

    public AboutUniversityEngine(Context context) {
        super(context);
    }

    public ArrayList<AboutUniversityInfo> getAboutAllUnivesities() {
        AboutUniversityDBWrapper aboutUniversityDBWrapper = new AboutUniversityDBWrapper(getContext());
        return aboutUniversityDBWrapper.getAboutAllUnivesities();
    }

    public ArrayList<AboutUniversityInfo> getAboutAllUnivesitiesById(long nId) {
        AboutUniversityDBWrapper aboutUniversityDBWrapper = new AboutUniversityDBWrapper(getContext());
        return aboutUniversityDBWrapper.getAboutAllUnivesitiesById(nId);
    }

    public void addAboutUniversity(AboutUniversityInfo aboutUniversityInfo) {
        AboutUniversityDBWrapper aboutUniversityDBWrapper = new AboutUniversityDBWrapper(getContext());
        aboutUniversityDBWrapper.addAboutUniversity(aboutUniversityInfo);
    }

    public AboutUniversityInfo getAboutUniversityById(long nId) {
        AboutUniversityDBWrapper aboutUniversityDBWrapper = new AboutUniversityDBWrapper(getContext());
        return aboutUniversityDBWrapper.getAboutUniversityById(nId);
    }
}
