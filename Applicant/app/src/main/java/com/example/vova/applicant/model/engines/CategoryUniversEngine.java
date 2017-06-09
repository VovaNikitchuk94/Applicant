package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.CategoryUniversInfoDBWrapper;
import com.example.vova.applicant.model.wrappers.dbWrappers.TimeFormDBWrapper;

import java.util.ArrayList;

public class CategoryUniversEngine extends BaseEngine {

    public CategoryUniversEngine(Context context) {
        super(context);
    }

    public ArrayList<CategoryUniversInfo> getAllCategoryById(long nId) {
        Log.d("My", "CategoryUniversEngine ----------> getAllCategoryById");
        CategoryUniversInfoDBWrapper categoryUniversInfoDBWrapper = new CategoryUniversInfoDBWrapper(getContext());
        return categoryUniversInfoDBWrapper.getAllCategoryById(nId);
    }

    public void updateCategory(CategoryUniversInfo categoryUniversInfo) {
        CategoryUniversInfoDBWrapper categoryUniversInfoDBWrapper = new CategoryUniversInfoDBWrapper(getContext());
        categoryUniversInfoDBWrapper.updateCategory(categoryUniversInfo);
    }

    public void addCategory(CategoryUniversInfo categoryUniversInfo) {
        Log.d("My","addCategory");
        CategoryUniversInfoDBWrapper categoryUniversInfoDBWrapper = new CategoryUniversInfoDBWrapper(getContext());
        categoryUniversInfoDBWrapper.addCategory(categoryUniversInfo);
    }

    public CategoryUniversInfo getCategoryById(long nId) {
        CategoryUniversInfoDBWrapper categoryUniversInfoDBWrapper = new CategoryUniversInfoDBWrapper(getContext());
        return categoryUniversInfoDBWrapper.getCategoryById(nId);
    }
}
