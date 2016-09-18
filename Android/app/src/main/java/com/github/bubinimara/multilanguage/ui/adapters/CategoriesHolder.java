package com.github.bubinimara.multilanguage.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.bubinimara.multilanguage.R;
import com.github.bubinimara.multilanguage.model.CategoryModel;

/**
 * Created by davide on 15/09/16.
 */
public class CategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View root;
    TextView textView;

    private OnItemClickListener<CategoriesHolder> listener;
    private CategoryModel categoryModel;

    @Override
    public void onClick(View view) {
        listener.onItemClick(this);
    }

    public CategoriesHolder(View itemView) {
        super(itemView);
        root = itemView;
        textView = (TextView) itemView.findViewById(R.id.textView);
    }

    public void setListener(OnItemClickListener<CategoriesHolder> onItemClickListener) {
        this.listener = onItemClickListener;
        root.setOnClickListener(this);
    }

    void set(CategoryModel category){
        categoryModel = category;
        textView.setText(category.getName());
    }

    public CategoryModel getCategory() {
        return categoryModel;
    }

    public static class Builder{
        CategoriesHolder holder;

        public Builder(View view) {
            holder = new CategoriesHolder(view);
        }
        public Builder setListener(OnItemClickListener<CategoriesHolder> listener){
            holder.setListener(listener);
            return this;

        }
        public CategoriesHolder build(){
            return holder;
        }
    }
}
