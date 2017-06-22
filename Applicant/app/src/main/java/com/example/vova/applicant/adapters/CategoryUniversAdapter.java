package com.example.vova.applicant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vova.applicant.R;
import com.example.vova.applicant.model.CategoryUniversInfo;

import java.util.ArrayList;

//TODO rename all fields
public class CategoryUniversAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoryUniversInfo> mCategoryUniversInfos = new ArrayList<>();
    private OnClickCategoryUniversItem mOnClickCategoryUniversItem = null;

    public CategoryUniversAdapter(ArrayList<CategoryUniversInfo> categoryUniversInfos) {
        mCategoryUniversInfos = categoryUniversInfos;
    }

    public void setOnClickCategoryUniversItem(OnClickCategoryUniversItem onClickCategoryUniversItem){
        mOnClickCategoryUniversItem = onClickCategoryUniversItem;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_info, parent, false);
        viewHolder = new CategoryUniversInfoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryUniversInfoViewHolder categoryUniversInfoViewHolder = (CategoryUniversInfoViewHolder) holder;
        final CategoryUniversInfo categoryUniversInfo = mCategoryUniversInfos.get(position);
        categoryUniversInfoViewHolder.nameTextView.setText(categoryUniversInfo.getStrCategoryName());
        categoryUniversInfoViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickCategoryUniversItem != null){
                    mOnClickCategoryUniversItem.onClickCategoryItem(categoryUniversInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryUniversInfos.size();
    }

    public class CategoryUniversInfoViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView nameTextView;

        public CategoryUniversInfoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.textViewCategoryInfo);
        }
    }

    public interface OnClickCategoryUniversItem {
        void onClickCategoryItem(CategoryUniversInfo categoryUniversInfo);
    }
}
